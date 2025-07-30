package com.redjujubetree.fs.service.impl;

import com.redjujubetree.fs.domain.dto.StorageSessionDto;
import com.redjujubetree.fs.enums.StorageSessionStatus;
import com.redjujubetree.fs.service.FileStorageService;
import com.redjujubetree.fs.service.StorageSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * 重构后的本地文件系统存储实现
 * 完全基于数据库的会话管理，与业务层保持一致
 */
@Slf4j
@Service
@Primary
public class LocalFileStorageService implements FileStorageService {
    
    // 配置参数
    @Value("${file.storage.local.temp-dir:temp}")
    private String tempDir;
    
    @Value("${file.storage.local.final-dir:uploads}")
    private String finalDir;
    
    @Value("${file.storage.local.create-dirs:true}")
    private boolean createDirs;
    
    @Value("${file.storage.local.chunk-buffer-size:8192}")
    private int chunkBufferSize;

    // 存储路径
    private Path tempDirPath;
    private Path finalDirPath;
    
    // 注入会话服务 - 统一使用数据库管理
    @Autowired
    private StorageSessionService sessionService;
    
    // 文件锁管理（仍需要，用于并发控制）
    private final Map<String, ReentrantReadWriteLock> fileLocks = new ConcurrentHashMap<>();
    
    @PostConstruct
    public void init() throws IOException {
        log.info("initializing LocalFileStorageService with tempDir: {}, finalDir: {}", tempDir, finalDir);
        // 初始化目录
        tempDirPath = Paths.get(tempDir);
        finalDirPath = Paths.get(finalDir);
        
        if (createDirs) {
            Files.createDirectories(tempDirPath);
            Files.createDirectories(finalDirPath);
            log.info("Created storage directories - Temp: {}, Final: {}", tempDirPath, finalDirPath);
        }
        
        // 验证目录可访问性
        if (!Files.exists(tempDirPath) || !Files.isWritable(tempDirPath)) {
            throw new IOException("Temp directory not accessible: " + tempDirPath);
        }
        
        if (!Files.exists(finalDirPath) || !Files.isWritable(finalDirPath)) {
            throw new IOException("Final directory not accessible: " + finalDirPath);
        }
        
        log.info("LocalFileStorageService initialized - Temp: {}, Final: {}", tempDirPath, finalDirPath);
    }
    
    @Override
    public boolean initializeStorage(String storageId, String fileName, long fileSize,
                                   String expectedChecksum, Map<String, String> customMetadata) {
        log.debug("Initializing storage - ID: {}, File: {}, Size: {}", storageId, fileName, fileSize);
        
        try {
            // 检查会话是否已存在（从数据库）
            Optional<StorageSessionDto> existingSession = sessionService.getSession(storageId);
            if (existingSession.isPresent()) {
                StorageSessionDto session = existingSession.get();
                log.info("Storage session already exists: {}, status: {}", storageId, session.getStatus());
                
                // 更新最后访问时间
                sessionService.updateLastAccess(storageId);
                
                // 检查文件结构是否完整
                if (validateStorageStructure(session)) {
                    return true;
                } else {
                    // 文件结构损坏，重新创建
                    log.warn("Storage structure corrupted for session: {}, recreating...", storageId);
                }
            }
            
            // 创建工作目录结构
            createStorageStructure(storageId);
            
            // 写入元数据文件
            writeMetadataFile(storageId, fileName, fileSize, expectedChecksum, customMetadata);
            
            log.info("Storage initialized successfully - ID: {}", storageId);
            
            return true;
            
        } catch (Exception e) {
            log.error("Failed to initialize storage for: {}", storageId, e);
            cleanupStorageFiles(storageId);
            return false;
        }
    }
    
    @Override
    public ChunkWriteResult writeChunk(String storageId, int chunkIndex, long offset,
                                     byte[] data, String chunkChecksum) {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		// 从数据库获取会话信息
        Optional<StorageSessionDto> sessionOpt = sessionService.getSession(storageId);
        if (!sessionOpt.isPresent()) {
            return new ChunkWriteResult(false, "Storage session not found", 0, null);
        }
        
        StorageSessionDto session = sessionOpt.get();
        
        // 验证分片参数
        if (chunkIndex < 0 || chunkIndex >= session.getTotalChunks()) {
            return new ChunkWriteResult(false, "Invalid chunk index", 0, null);
        }
        
        if (data == null || data.length == 0) {
            return new ChunkWriteResult(false, "Empty chunk data", 0, null);
        }
        
        ReentrantReadWriteLock lock = getFileLock(storageId);
        lock.writeLock().lock();
        
        try {
            // 检查分片是否已存在（从数据库查询）
            if (sessionService.chunkExists(storageId, chunkIndex)) {
                log.debug("Chunk {} already exists for storage: {}", chunkIndex, storageId);
                return new ChunkWriteResult(true, "Chunk already exists", data.length, chunkChecksum);
            }
            
            // 验证分片校验和
            String actualChecksum = calculateChecksum(data);
            if (chunkChecksum != null && !chunkChecksum.equals(actualChecksum)) {
                return new ChunkWriteResult(false, "Chunk checksum mismatch", 0, actualChecksum);
            }
            
            // 构建文件路径
            Path chunkDirPath = getChunkDirPath(storageId);
            Path chunkPath = chunkDirPath.resolve("chunk_" + chunkIndex);
            Path tempFilePath = getTempFilePath(storageId);
            
            // 确保目录存在
            Files.createDirectories(chunkDirPath);
            
            // 写入分片文件
            Files.write(chunkPath, data, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            
            // 同时写入到临时文件的正确位置
            try (RandomAccessFile raf = new RandomAccessFile(tempFilePath.toFile(), "rw")) {
                raf.seek(offset);
                raf.write(data);
            }
            
            // 更新数据库中的分片记录（替代内存管理）
            sessionService.recordChunkUpload(storageId, chunkIndex, (long) data.length, actualChecksum, offset);
            
            // 更新会话最后访问时间
            sessionService.updateLastAccess(storageId);
            
            log.info("Chunk {} written successfully for storage: {}, size: {}",
                        chunkIndex, storageId, data.length);
            
            return new ChunkWriteResult(true, "Chunk written successfully", data.length, actualChecksum);
            
        } catch (Exception e) {
            log.error("Failed to write chunk {} for storage: {}", chunkIndex, storageId, e);
            return new ChunkWriteResult(false, "Error writing chunk: " + e.getMessage(), 0, null);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public List<ChunkWriteResult> writeChunks(String storageId, List<ChunkData> chunks) throws IOException {
        // 从数据库获取会话信息
        Optional<StorageSessionDto> sessionOpt = sessionService.getSession(storageId);
        if (!sessionOpt.isPresent()) {
            return chunks.stream()
                    .map(chunk -> new ChunkWriteResult(false, "Storage session not found", 0, null))
                    .collect(Collectors.toList());
        }
        
        StorageSessionDto session = sessionOpt.get();
        List<ChunkWriteResult> results = new ArrayList<>();
        ReentrantReadWriteLock lock = getFileLock(storageId);
        lock.writeLock().lock();
        
        try {
            Path tempFilePath = getTempFilePath(storageId);
            Path chunkDirPath = getChunkDirPath(storageId);
            
            try (RandomAccessFile raf = new RandomAccessFile(tempFilePath.toFile(), "rw")) {
                for (ChunkData chunk : chunks) {
                    try {
                        // 验证分片
                        if (chunk.getChunkIndex() < 0 || chunk.getChunkIndex() >= session.getTotalChunks()) {
                            results.add(new ChunkWriteResult(false, "Invalid chunk index", 0, null));
                            continue;
                        }
                        
                        // 检查是否已存在（从数据库查询）
                        if (sessionService.chunkExists(storageId, chunk.getChunkIndex())) {
                            results.add(new ChunkWriteResult(true, "Chunk already exists", 
                                        chunk.getData().length, chunk.getChecksum()));
                            continue;
                        }
                        
                        // 验证校验和
                        String actualChecksum = calculateChecksum(chunk.getData());
                        if (chunk.getChecksum() != null && !chunk.getChecksum().equals(actualChecksum)) {
                            results.add(new ChunkWriteResult(false, "Chunk checksum mismatch", 0, actualChecksum));
                            continue;
                        }
                        
                        // 写入分片文件
                        Path chunkPath = chunkDirPath.resolve("chunk_" + chunk.getChunkIndex());
                        Files.write(chunkPath, chunk.getData(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
                        
                        // 写入临时文件
                        raf.seek(chunk.getOffset());
                        raf.write(chunk.getData());
                        
                        // 更新数据库记录
                        sessionService.recordChunkUpload(storageId, chunk.getChunkIndex(), 
                                (long) chunk.getData().length, actualChecksum, chunk.getOffset());
                        
                        results.add(new ChunkWriteResult(true, "Chunk written successfully", 
                                chunk.getData().length, actualChecksum));
                        
                    } catch (Exception e) {
                        log.error("Failed to write chunk {} in batch for storage: {}",
                                chunk.getChunkIndex(), storageId, e);
                        results.add(new ChunkWriteResult(false, "Error writing chunk: " + e.getMessage(), 0, null));
                    }
                }
            }
            
            // 更新会话最后访问时间
            sessionService.updateLastAccess(storageId);
            
        } finally {
            lock.writeLock().unlock();
        }
        
        return results;
    }
    
    @Override
    public FileCompleteResult completeUpload(String storageId) {
        // 从数据库获取会话信息
        Optional<StorageSessionDto> sessionOpt = sessionService.getSession(storageId);
        if (!sessionOpt.isPresent()) {
            return new FileCompleteResult(false, "Storage session not found", null, null, null);
        }
        
        StorageSessionDto session = sessionOpt.get();
        
        ReentrantReadWriteLock lock = getFileLock(storageId);
        lock.writeLock().lock();
        
        try {
            // 验证所有分片是否已上传（从数据库查询）
            if (!sessionService.isUploadComplete(storageId)) {
                List<Integer> missingChunks = sessionService.getMissingChunks(storageId);
                return new FileCompleteResult(false, 
                    "Upload incomplete, missing chunks: " + missingChunks, null, null, null);
            }
            
            Path tempFilePath = getTempFilePath(storageId);
            
            // 验证文件完整性
            String actualChecksum = calculateFileChecksum(tempFilePath);
            if (session.getExpectedChecksum() != null && 
                !session.getExpectedChecksum().equals(actualChecksum)) {
                return new FileCompleteResult(false, "File checksum verification failed", 
                                            null, actualChecksum, null);
            }
            
            // 生成最终文件路径
            String finalFileName = generateFinalFileName(session.getOriginalFileName(), storageId);
            Path finalPath = finalDirPath.resolve(finalFileName);
            
            // 确保目标目录存在
            Files.createDirectories(finalPath.getParent());
            
            // 移动临时文件到最终位置
            Files.move(tempFilePath, finalPath, StandardCopyOption.REPLACE_EXISTING);
            
            // 创建存储元数据
            StorageMetadata metadata = new StorageMetadata(
                storageId,
                session.getOriginalFileName(),
                session.getFileSize(),
                detectContentType(session.getOriginalFileName()),
                actualChecksum,
                session.getCreatedTime().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli(),
                session.getCustomMetadata()
            );
            
            // 清理临时文件
            cleanupTempFiles(storageId);
            
            // 移除文件锁
            fileLocks.remove(storageId);
            
            log.info("Upload completed successfully - Storage: {}, Final path: {}, Checksum verified: {}",
                       storageId, finalPath, actualChecksum.equals(session.getExpectedChecksum()));
            
            return new FileCompleteResult(true, "Upload completed successfully", 
                                        finalPath.toString(), actualChecksum, metadata);
            
        } catch (Exception e) {
            log.error("Failed to complete upload for storage: {}", storageId, e);
            return new FileCompleteResult(false, "Error completing upload: " + e.getMessage(), null, null, null);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public boolean cancelUpload(String storageId) {
        log.info("Cancelling upload for storage: {}", storageId);
        
        try {
            // 清理存储文件
            cleanupStorageFiles(storageId);
            
            // 移除文件锁
            fileLocks.remove(storageId);
            
            log.info("Upload cancelled and cleaned up - Storage: {}", storageId);
            return true;
        } catch (Exception e) {
            log.error("Error cleaning up cancelled upload: {}", storageId, e);
            return false;
        }
    }
    
    @Override
    public boolean chunkExists(String storageId, int chunkIndex) {
        // 直接委托给会话服务查询数据库
        return sessionService.chunkExists(storageId, chunkIndex);
    }
    
    @Override
    public List<Integer> getUploadedChunks(String storageId) throws IOException {
        // 直接委托给会话服务查询数据库
        return sessionService.getUploadedChunks(storageId);
    }
    
    @Override
    public StorageMetadata getStorageMetadata(String storageId) throws IOException {
        // 从数据库获取会话信息
        Optional<StorageSessionDto> sessionOpt = sessionService.getSession(storageId);
        if (!sessionOpt.isPresent()) {
            throw new IOException("Storage session not found: " + storageId);
        }
        
        StorageSessionDto session = sessionOpt.get();
        
        return new StorageMetadata(
            storageId,
            session.getOriginalFileName(),
            session.getFileSize(),
            detectContentType(session.getOriginalFileName()),
            session.getExpectedChecksum(),
            session.getCreatedTime().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli(),
            session.getCustomMetadata()
        );
    }
    
    @Override
    public boolean deleteFile(String storageId) {
        log.info("Delete file request for storage: {}", storageId);
        return cleanupStorageFiles(storageId);
    }
    
    @Override
    public InputStream getFileInputStream(String storageId) throws IOException {
        // 从数据库获取会话信息
        Optional<StorageSessionDto> sessionOpt = sessionService.getSession(storageId);
        if (!sessionOpt.isPresent()) {
            throw new IOException("Storage session not found: " + storageId);
        }
        
        StorageSessionDto session = sessionOpt.get();
        
        // 根据状态返回对应的文件流
        if (session.getStatus() == StorageSessionStatus.COMPLETED) {
            // 如果已完成，查找最终文件
            String finalFileName = generateFinalFileName(session.getOriginalFileName(), storageId);
            Path finalPath = finalDirPath.resolve(finalFileName);
            if (Files.exists(finalPath)) {
                return Files.newInputStream(finalPath);
            }
        }
        
        // 否则返回临时文件流
        Path tempFilePath = getTempFilePath(storageId);
        if (Files.exists(tempFilePath)) {
            return Files.newInputStream(tempFilePath);
        }
        
        throw new IOException("File not found for storage: " + storageId);
    }
    
    @Override
    public boolean verifyFileIntegrity(String storageId, String expectedChecksum) throws IOException {
        Path tempFilePath = getTempFilePath(storageId);
        
        if (!Files.exists(tempFilePath)) {
            return false;
        }
        
        String actualChecksum = calculateFileChecksum(tempFilePath);
        return expectedChecksum.equals(actualChecksum);
    }
    
    @Override
    public int cleanupExpiredFiles(long timeoutMs) {
        int cleanedCount = 0;
        
        // 委托给会话服务清理过期会话
        java.time.Duration timeout = java.time.Duration.ofMillis(timeoutMs);
        int expiredSessions = sessionService.cleanupExpiredSessions(timeout);
        
        // 清理孤立的临时文件
        cleanedCount += cleanupOrphanedTempFiles(timeoutMs);
        
        log.info("Cleanup completed - Expired sessions: {}, Orphaned files: {}",
                   expiredSessions, cleanedCount);
        
        return cleanedCount + expiredSessions;
    }
    
    @Override
    public StorageType getStorageType() {
        return StorageType.LOCAL_PERSISTENT;
    }
    
    @Override
    public boolean healthCheck() {
        try {
            // 检查目录可访问性
            if (!Files.exists(tempDirPath) || !Files.isWritable(tempDirPath)) {
                log.error("Health check failed: temp directory not accessible");
                return false;
            }
            
            if (!Files.exists(finalDirPath) || !Files.isWritable(finalDirPath)) {
                log.error("Health check failed: final directory not accessible");
                return false;
            }
            
            // 检查磁盘空间
            long freeSpace = tempDirPath.toFile().getFreeSpace();
            long minFreeSpace = 100 * 1024 * 1024; // 100MB minimum
            
            if (freeSpace < minFreeSpace) {
                log.warn("Health check warning: low disk space - {} bytes available", freeSpace);
            }
            
            return true;
            
        } catch (Exception e) {
            log.error("Health check failed", e);
            return false;
        }
    }
    
    // ==================== 私有辅助方法 ====================
    
    /**
     * 创建存储目录结构
     */
    private void createStorageStructure(String storageId) throws IOException {
        Path tempFilePath = getTempFilePath(storageId);
        Path chunkDirPath = getChunkDirPath(storageId);
        
        // 创建分片目录
        Files.createDirectories(chunkDirPath);
        
        // 创建临时文件
        if (!Files.exists(tempFilePath)) {
            Files.createFile(tempFilePath);
        }
    }
    
    /**
     * 验证存储结构完整性
     */
    private boolean validateStorageStructure(StorageSessionDto session) {
        try {
            Path tempFilePath = getTempFilePath(session.getStorageId());
            Path chunkDirPath = getChunkDirPath(session.getStorageId());
            Path metadataFilePath = getMetadataFilePath(session.getStorageId());
            
            return Files.exists(tempFilePath) && 
                   Files.exists(chunkDirPath) && 
                   Files.isDirectory(chunkDirPath) &&
                   Files.exists(metadataFilePath);
                   
        } catch (Exception e) {
            log.warn("Error validating storage structure for: {}", session.getStorageId(), e);
            return false;
        }
    }
    
    /**
     * 写入元数据文件
     */
    private void writeMetadataFile(String storageId, String fileName, long fileSize, 
                                 String expectedChecksum, Map<String, String> customMetadata) throws IOException {
        Path metadataFilePath = getMetadataFilePath(storageId);
        
        Properties props = new Properties();
        props.setProperty("storageId", storageId);
        props.setProperty("fileName", fileName);
        props.setProperty("fileSize", String.valueOf(fileSize));
        props.setProperty("expectedChecksum", expectedChecksum != null ? expectedChecksum : "");
        props.setProperty("createdTime", String.valueOf(System.currentTimeMillis()));
        
        // 添加自定义元数据
        if (customMetadata != null) {
            customMetadata.forEach((key, value) -> props.setProperty("meta." + key, value));
        }
        
        try (OutputStream os = Files.newOutputStream(metadataFilePath)) {
            props.store(os, "Storage metadata");
        }
    }
    
    /**
     * 清理存储文件
     */
    private boolean cleanupStorageFiles(String storageId) {
        try {
            Path tempFilePath = getTempFilePath(storageId);
            Path chunkDirPath = getChunkDirPath(storageId);
            Path metadataFilePath = getMetadataFilePath(storageId);
            
            // 删除临时文件
            if (Files.exists(tempFilePath)) {
                Files.delete(tempFilePath);
            }
            
            // 删除分片目录
            if (Files.exists(chunkDirPath)) {
                deleteDirectory(chunkDirPath);
            }
            
            // 删除元数据文件
            if (Files.exists(metadataFilePath)) {
                Files.delete(metadataFilePath);
            }
            
            log.debug("Cleaned up storage files for: {}", storageId);
            return true;
            
        } catch (Exception e) {
            log.error("Error cleaning up storage files for: {}", storageId, e);
            return false;
        }
    }
    
    /**
     * 清理临时文件
     */
    private void cleanupTempFiles(String storageId) throws IOException {
        Path chunkDirPath = getChunkDirPath(storageId);
        Path metadataFilePath = getMetadataFilePath(storageId);
        
        // 删除分片目录
        if (Files.exists(chunkDirPath)) {
            deleteDirectory(chunkDirPath);
        }
        
        // 删除元数据文件
        if (Files.exists(metadataFilePath)) {
            Files.delete(metadataFilePath);
        }
    }
    
    /**
     * 递归删除目录
     */
    private void deleteDirectory(Path dir) throws IOException {
        if (Files.exists(dir)) {
            Files.walk(dir)
                    .sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            log.warn("Failed to delete: {}", path, e);
                        }
                    });
        }
    }
    
    /**
     * 清理孤立的临时文件
     */
    private int cleanupOrphanedTempFiles(long timeoutMs) {
        java.util.concurrent.atomic.AtomicInteger cleanedCount = new java.util.concurrent.atomic.AtomicInteger();
        long cutoffTime = System.currentTimeMillis() - timeoutMs;
        
        try {
            Files.list(tempDirPath)
                    .filter(path -> {
                        try {
                            return Files.getLastModifiedTime(path).toMillis() < cutoffTime;
                        } catch (IOException e) {
                            return false;
                        }
                    })
                    .forEach(path -> {
                        try {
                            String fileName = path.getFileName().toString();
                            if (fileName.endsWith(".tmp") || fileName.endsWith(".meta") || fileName.endsWith("_chunks")) {
                                if (Files.isDirectory(path)) {
                                    deleteDirectory(path);
                                } else {
                                    Files.delete(path);
                                }
                                cleanedCount.getAndIncrement();
                                log.debug("Cleaned up orphaned temp file: {}", path);
                            }
                        } catch (Exception e) {
                            log.warn("Failed to cleanup orphaned file: {}", path, e);
                        }
                    });
                    
        } catch (Exception e) {
            log.error("Error cleaning up orphaned temp files", e);
        }
        
        return cleanedCount.get();
    }
    
    // 路径生成方法
    private Path getTempFilePath(String storageId) {
        return tempDirPath.resolve(storageId + ".tmp");
    }
    
    private Path getChunkDirPath(String storageId) {
        return tempDirPath.resolve(storageId + "_chunks");
    }
    
    private Path getMetadataFilePath(String storageId) {
        return tempDirPath.resolve(storageId + ".meta");
    }
    
    private ReentrantReadWriteLock getFileLock(String storageId) {
        return fileLocks.computeIfAbsent(storageId, k -> new ReentrantReadWriteLock());
    }
    
    private String calculateChecksum(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(data);
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not available", e);
        }
    }
    
    private String calculateFileChecksum(Path filePath) throws IOException {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            try (InputStream is = Files.newInputStream(filePath);
                 BufferedInputStream bis = new BufferedInputStream(is)) {
                
                byte[] buffer = new byte[chunkBufferSize];
                int bytesRead;
                
                while ((bytesRead = bis.read(buffer)) != -1) {
                    md.update(buffer, 0, bytesRead);
                }
            }
            
            byte[] hash = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
            
        } catch (NoSuchAlgorithmException e) {
            throw new IOException("MD5 algorithm not available", e);
        }
    }
    
    private String generateFinalFileName(String originalFileName, String storageId) {
        // 生成唯一的最终文件名，避免冲突
        String timestamp = String.valueOf(System.currentTimeMillis());
        String extension = getFileExtension(originalFileName);
        String baseName = getFileBaseName(originalFileName);
        
        // 格式：原文件名_时间戳_存储ID.扩展名
        if (extension.isEmpty()) {
            return String.format("%s_%s_%s", baseName, timestamp, storageId.substring(0, Math.min(8, storageId.length())));
        } else {
            return String.format("%s_%s_%s.%s", baseName, timestamp, storageId.substring(0, Math.min(8, storageId.length())), extension);
        }
    }
    
    private String getFileExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot > 0 && lastDot < fileName.length() - 1) {
            return fileName.substring(lastDot + 1);
        }
        return "";
    }
    
    private String getFileBaseName(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot > 0) {
            return fileName.substring(0, lastDot);
        }
        return fileName;
    }
    
    private String detectContentType(String fileName) {
        try {
            Path path = Paths.get(fileName);
            String contentType = Files.probeContentType(path);
            return contentType != null ? contentType : "application/octet-stream";
        } catch (Exception e) {
            return "application/octet-stream";
        }
    }
}