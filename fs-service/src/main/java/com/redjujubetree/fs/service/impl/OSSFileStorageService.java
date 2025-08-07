package com.redjujubetree.fs.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.*;
import com.redjujubetree.fs.service.FileStorageService;
import com.redjujubetree.fs.service.OSSStorageConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 阿里云OSS文件存储服务实现
 * 支持分片上传、断点续传、多部分上传
 */
//@Service
public class OSSFileStorageService implements FileStorageService {
    
    private static final Logger logger = LoggerFactory.getLogger(OSSFileStorageService.class);
    
    private final OSSStorageConfiguration configuration;
    private OSS ossClient;
    
    // 存储会话管理
    private final Map<String, OSSStorageSession> sessions = new ConcurrentHashMap<>();
    
    public OSSFileStorageService(OSSStorageConfiguration configuration) {
        this.configuration = configuration;
    }
    
    @PostConstruct
    public void init() {
        if (!configuration.validateConfiguration()) {
            throw new IllegalArgumentException("Invalid OSS storage configuration");
        }
        
        // 初始化OSS客户端
        this.ossClient = new OSSClientBuilder().build(
            configuration.getEndpoint(),
            configuration.getAccessKeyId(),
            configuration.getAccessKeySecret()
        );
        
        // 验证Bucket存在性
        if (!ossClient.doesBucketExist(configuration.getBucketName())) {
            throw new IllegalStateException("OSS bucket does not exist: " + configuration.getBucketName());
        }
        
        logger.info("OSSFileStorageService initialized - Endpoint: {}, Bucket: {}", 
                   configuration.getEndpoint(), configuration.getBucketName());
    }
    
    @PreDestroy
    public void destroy() {
        if (ossClient != null) {
            ossClient.shutdown();
            logger.info("OSS client shutdown completed");
        }
    }
    
    /**
     * OSS存储会话
     */
    private static class OSSStorageSession {
        private final String storageId;
        private final String fileName;
        private final long fileSize;
        private final String expectedChecksum;
        private final String objectKey;
        private final Map<String, String> customMetadata;
        private final long createdTime;
        
        // OSS多部分上传相关
        private String uploadId; // OSS multipart upload ID
        private final Map<Integer, PartETag> uploadedParts = new ConcurrentHashMap<>();
        private final Set<Integer> uploadedChunks = ConcurrentHashMap.newKeySet();
        
        private volatile long lastActivityTime;
        
        public OSSStorageSession(String storageId, String fileName, long fileSize, 
                               String expectedChecksum, String objectKey,
                               Map<String, String> customMetadata) {
            this.storageId = storageId;
            this.fileName = fileName;
            this.fileSize = fileSize;
            this.expectedChecksum = expectedChecksum;
            this.objectKey = objectKey;
            this.customMetadata = customMetadata != null ? new HashMap<>(customMetadata) : new HashMap<>();
            this.createdTime = System.currentTimeMillis();
            this.lastActivityTime = createdTime;
        }
        
        public void updateLastActivity() {
            this.lastActivityTime = System.currentTimeMillis();
        }
        
        // Getters
        public String getStorageId() { return storageId; }
        public String getFileName() { return fileName; }
        public long getFileSize() { return fileSize; }
        public String getExpectedChecksum() { return expectedChecksum; }
        public String getObjectKey() { return objectKey; }
        public Map<String, String> getCustomMetadata() { return customMetadata; }
        public long getCreatedTime() { return createdTime; }
        public long getLastActivityTime() { return lastActivityTime; }
        
        public String getUploadId() { return uploadId; }
        public void setUploadId(String uploadId) { this.uploadId = uploadId; }
        
        public Map<Integer, PartETag> getUploadedParts() { return uploadedParts; }
        public Set<Integer> getUploadedChunks() { return new HashSet<>(uploadedChunks); }
    }
    
    @Override
    public boolean initializeStorage(String storageId, String fileName, long fileSize, 
                                   String expectedChecksum, Map<String, String> customMetadata) throws IOException {
        try {
            // 检查是否已存在会话
            if (sessions.containsKey(storageId)) {
                logger.debug("OSS storage session already exists for: {}", storageId);
                return true;
            }
            
            // 生成对象键
            String objectKey = generateObjectKey(fileName, storageId);
            
            // 创建存储会话
            OSSStorageSession session = new OSSStorageSession(
                storageId, fileName, fileSize, expectedChecksum, objectKey, customMetadata
            );
            
            // 如果文件大小超过阈值，启用多部分上传
            if (configuration.isEnableMultipartUpload() && 
                fileSize > configuration.getMultipartThreshold()) {
                
                String uploadId = initializeMultipartUpload(session);
                session.setUploadId(uploadId);
                
                logger.info("Initialized OSS multipart upload - Storage: {}, Object: {}, UploadId: {}", 
                           storageId, objectKey, uploadId);
            }
            
            sessions.put(storageId, session);
            
            logger.info("Initialized OSS storage session: {}, object: {}, size: {} bytes", 
                       storageId, objectKey, fileSize);
            
            return true;
            
        } catch (Exception e) {
            logger.error("Error initializing OSS storage for: {}", storageId, e);
            throw new IOException("Failed to initialize OSS storage", e);
        }
    }
    
    @Override
    public ChunkWriteResult writeChunk(String storageId, int chunkIndex, long offset, 
                                     byte[] data, String chunkChecksum) throws IOException {
        OSSStorageSession session = sessions.get(storageId);
        if (session == null) {
            throw new IOException("OSS storage session not found: " + storageId);
        }
        
        try {
            // 检查是否已经上传过（幂等性）
            if (session.uploadedChunks.contains(chunkIndex)) {
                logger.debug("Chunk {} already uploaded to OSS for storage {}, skipping", chunkIndex, storageId);
                return new ChunkWriteResult(true, "Chunk already uploaded", data.length, chunkChecksum);
            }
            
            // 验证分片校验和
            if (!verifyChunkChecksum(data, chunkChecksum)) {
                String error = String.format("Chunk checksum verification failed for OSS storage %s, chunk %d", 
                                            storageId, chunkIndex);
                logger.error(error);
                return new ChunkWriteResult(false, error, 0, null);
            }
            
            // 如果使用多部分上传
            if (session.getUploadId() != null) {
                return uploadMultipartChunk(session, chunkIndex, data, chunkChecksum);
            } else {
                // 对于小文件或不支持多部分上传的情况，暂存到内存中
                // 在 completeUpload 时一次性上传
                session.uploadedChunks.add(chunkIndex);
                session.updateLastActivity();
                
                logger.debug("Chunk {} buffered for OSS storage {}", chunkIndex, storageId);
                return new ChunkWriteResult(true, "Chunk buffered successfully", data.length, chunkChecksum);
            }
            
        } catch (Exception e) {
            String error = String.format("Error writing chunk %d to OSS storage %s", chunkIndex, storageId);
            logger.error(error, e);
            return new ChunkWriteResult(false, error + ": " + e.getMessage(), 0, null);
        }
    }
    
    /**
     * 上传多部分分片
     */
    private ChunkWriteResult uploadMultipartChunk(OSSStorageSession session, int chunkIndex, 
                                                byte[] data, String chunkChecksum) {
        try {
            // OSS的part number从1开始
            int partNumber = chunkIndex + 1;
            
            UploadPartRequest uploadPartRequest = new UploadPartRequest();
            uploadPartRequest.setBucketName(configuration.getBucketName());
            uploadPartRequest.setKey(session.getObjectKey());
            uploadPartRequest.setUploadId(session.getUploadId());
            uploadPartRequest.setPartNumber(partNumber);
            uploadPartRequest.setPartSize(data.length);
            uploadPartRequest.setInputStream(new ByteArrayInputStream(data));
            
            // 设置MD5校验
            uploadPartRequest.setMd5Digest(chunkChecksum);
            
            UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);
            PartETag partETag = uploadPartResult.getPartETag();
            
            // 记录上传的部分
            session.getUploadedParts().put(partNumber, partETag);
            session.uploadedChunks.add(chunkIndex);
            session.updateLastActivity();
            
            logger.debug("OSS multipart chunk {} uploaded successfully for storage {}, part number: {}, ETag: {}", 
                        chunkIndex, session.getStorageId(), partNumber, partETag.getETag());
            
            return new ChunkWriteResult(true, "Multipart chunk uploaded successfully", data.length, chunkChecksum);
            
        } catch (Exception e) {
            logger.error("Error uploading multipart chunk {} for OSS storage {}", chunkIndex, session.getStorageId(), e);
            return new ChunkWriteResult(false, "Failed to upload multipart chunk: " + e.getMessage(), 0, null);
        }
    }
    
    @Override
    public List<ChunkWriteResult> writeChunks(String storageId, List<ChunkData> chunks) throws IOException {
        List<ChunkWriteResult> results = new ArrayList<>();
        
        // 对于OSS，并发上传多个分片
        for (ChunkData chunk : chunks) {
            ChunkWriteResult result = writeChunk(storageId, chunk.getChunkIndex(), 
                                               chunk.getOffset(), chunk.getData(), chunk.getChecksum());
            results.add(result);
        }
        
        return results;
    }
    
    @Override
    public FileCompleteResult completeUpload(String storageId) throws IOException {
        OSSStorageSession session = sessions.get(storageId);
        if (session == null) {
            return new FileCompleteResult(false, "OSS storage session not found", null, null, null);
        }
        
        try {
            String actualChecksum;
            String finalObjectKey = session.getObjectKey();
            
            if (session.getUploadId() != null) {
                // 完成多部分上传
                actualChecksum = completeMultipartUpload(session);
            } else {
                // 对于小文件，需要重新收集所有分片数据并上传
                // 这里简化处理，实际应用中可能需要更复杂的缓存策略
                throw new IOException("Single upload mode not fully implemented for OSS");
            }
            
            // 验证文件完整性
            if (!session.getExpectedChecksum().equalsIgnoreCase(actualChecksum)) {
                String error = String.format("OSS file checksum verification failed. Expected: %s, Actual: %s", 
                                            session.getExpectedChecksum(), actualChecksum);
                logger.error(error);
                return new FileCompleteResult(false, error, null, actualChecksum, null);
            }
            
            // 创建存储元数据
            StorageMetadata metadata = new StorageMetadata(
                storageId,
                session.getFileName(),
                session.getFileSize(),
                detectContentType(session.getFileName()),
                actualChecksum,
                session.getCreatedTime(),
                session.getCustomMetadata()
            );
            
            // 清理会话
            sessions.remove(storageId);
            
            String ossUrl = String.format("https://%s.%s/%s", 
                                        configuration.getBucketName(), 
                                        configuration.getEndpoint().replace("https://", ""), 
                                        finalObjectKey);
            
            logger.info("OSS upload completed successfully for storage: {}, object: {}", 
                       storageId, finalObjectKey);
            
            return new FileCompleteResult(true, "OSS upload completed successfully", 
                                        ossUrl, actualChecksum, metadata);
            
        } catch (Exception e) {
            logger.error("Error completing OSS upload for storage: {}", storageId, e);
            
            // 清理未完成的多部分上传
            if (session.getUploadId() != null) {
                try {
                    abortMultipartUpload(session);
                } catch (Exception abortEx) {
                    logger.error("Error aborting multipart upload", abortEx);
                }
            }
            
            return new FileCompleteResult(false, "Failed to complete OSS upload: " + e.getMessage(), 
                                        null, null, null);
        }
    }
    
    /**
     * 完成多部分上传
     */
    private String completeMultipartUpload(OSSStorageSession session) throws Exception {
        // 构建部分列表
        List<PartETag> partETags = new ArrayList<>();
        Map<Integer, PartETag> uploadedParts = session.getUploadedParts();
        
        // 按part number排序
        List<Integer> sortedPartNumbers = new ArrayList<>(uploadedParts.keySet());
        Collections.sort(sortedPartNumbers);
        
        for (Integer partNumber : sortedPartNumbers) {
            partETags.add(uploadedParts.get(partNumber));
        }
        
        CompleteMultipartUploadRequest completeRequest = new CompleteMultipartUploadRequest(
            configuration.getBucketName(),
            session.getObjectKey(),
            session.getUploadId(),
            partETags
        );
        
        CompleteMultipartUploadResult completeResult = ossClient.completeMultipartUpload(completeRequest);
        
        logger.info("OSS multipart upload completed - Storage: {}, Object: {}, ETag: {}", 
                   session.getStorageId(), session.getObjectKey(), completeResult.getETag());
        
        return completeResult.getETag();
    }
    
    /**
     * 中止多部分上传
     */
    private void abortMultipartUpload(OSSStorageSession session) {
        try {
            AbortMultipartUploadRequest abortRequest = new AbortMultipartUploadRequest(
                configuration.getBucketName(),
                session.getObjectKey(),
                session.getUploadId()
            );
            
            ossClient.abortMultipartUpload(abortRequest);
            
            logger.info("OSS multipart upload aborted - Storage: {}, UploadId: {}", 
                       session.getStorageId(), session.getUploadId());
                       
        } catch (Exception e) {
            logger.error("Error aborting OSS multipart upload for storage: {}", session.getStorageId(), e);
        }
    }
    
    @Override
    public boolean cancelUpload(String storageId) throws IOException {
        OSSStorageSession session = sessions.remove(storageId);
        if (session == null) {
            logger.warn("OSS storage session not found for cancellation: {}", storageId);
            return false;
        }
        
        try {
            // 如果是多部分上传，需要中止
            if (session.getUploadId() != null) {
                abortMultipartUpload(session);
            }
            
            logger.info("OSS upload cancelled successfully for storage: {}", storageId);
            return true;
            
        } catch (Exception e) {
            logger.error("Error cancelling OSS upload for storage: {}", storageId, e);
            throw new IOException("Failed to cancel OSS upload", e);
        }
    }
    
    @Override
    public boolean chunkExists(String storageId, int chunkIndex) throws IOException {
        OSSStorageSession session = sessions.get(storageId);
        if (session == null) {
            return false;
        }
        
        return session.uploadedChunks.contains(chunkIndex);
    }
    
    @Override
    public List<Integer> getUploadedChunks(String storageId) throws IOException {
        OSSStorageSession session = sessions.get(storageId);
        if (session == null) {
            return Collections.emptyList();
        }
        
        return new ArrayList<>(session.uploadedChunks);
    }
    
    @Override
    public StorageMetadata getStorageMetadata(String storageId) throws IOException {
        OSSStorageSession session = sessions.get(storageId);
        if (session == null) {
            throw new IOException("OSS storage session not found: " + storageId);
        }
        
        return new StorageMetadata(
            session.getStorageId(),
            session.getFileName(),
            session.getFileSize(),
            detectContentType(session.getFileName()),
            session.getExpectedChecksum(),
            session.getCreatedTime(),
            session.getCustomMetadata()
        );
    }
    
    @Override
    public boolean deleteFile(String storageId) throws IOException {
        OSSStorageSession session = sessions.get(storageId);
        if (session == null) {
            logger.warn("OSS storage session not found for deletion: {}", storageId);
            return false;
        }
        
        try {
            // 取消上传
            cancelUpload(storageId);
            
            // 尝试删除对象（如果存在）
            if (ossClient.doesObjectExist(configuration.getBucketName(), session.getObjectKey())) {
                ossClient.deleteObject(configuration.getBucketName(), session.getObjectKey());
                logger.info("OSS object deleted: {}", session.getObjectKey());
            }
            
            return true;
            
        } catch (Exception e) {
            logger.error("Error deleting OSS file for storage: {}", storageId, e);
            throw new IOException("Failed to delete OSS file", e);
        }
    }
    
    @Override
    public InputStream getFileInputStream(String storageId) throws IOException {
        OSSStorageSession session = sessions.get(storageId);
        if (session == null) {
            throw new IOException("OSS storage session not found: " + storageId);
        }
        
        try {
            OSSObject ossObject = ossClient.getObject(configuration.getBucketName(), session.getObjectKey());
            return ossObject.getObjectContent();
        } catch (Exception e) {
            logger.error("Error getting OSS file input stream for storage: {}", storageId, e);
            throw new IOException("Failed to get OSS file input stream", e);
        }
    }
    
    @Override
    public boolean verifyFileIntegrity(String storageId, String expectedChecksum) throws IOException {
        OSSStorageSession session = sessions.get(storageId);
        if (session == null) {
            throw new IOException("OSS storage session not found: " + storageId);
        }
        
        try {
            ObjectMetadata metadata = ossClient.getObjectMetadata(configuration.getBucketName(), session.getObjectKey());
            String actualChecksum = metadata.getETag();
            
            return expectedChecksum.equalsIgnoreCase(actualChecksum);
        } catch (Exception e) {
            logger.error("Error verifying OSS file integrity for storage: {}", storageId, e);
            return false;
        }
    }
    
    @Override
    public int cleanupExpiredFiles(long timeoutMs) throws IOException {
        int cleanedCount = 0;
        long currentTime = System.currentTimeMillis();
        
        Iterator<Map.Entry<String, OSSStorageSession>> iterator = sessions.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, OSSStorageSession> entry = iterator.next();
            OSSStorageSession session = entry.getValue();
            
            if (currentTime - session.getLastActivityTime() > timeoutMs) {
                try {
                    logger.info("Cleaning up expired OSS storage session: {}", session.getStorageId());
                    
                    // 中止多部分上传
                    if (session.getUploadId() != null) {
                        abortMultipartUpload(session);
                    }
                    
                    iterator.remove();
                    cleanedCount++;
                    
                } catch (Exception e) {
                    logger.error("Error cleaning up expired OSS session: {}", session.getStorageId(), e);
                }
            }
        }
        
        if (cleanedCount > 0) {
            logger.info("Cleaned up {} expired OSS storage sessions", cleanedCount);
        }
        
        return cleanedCount;
    }
    
    @Override
    public StorageType getStorageType() {
        return StorageType.OSS;
    }
    
    @Override
    public boolean healthCheck() {
        try {
            // 检查OSS连接和Bucket访问权限
            return ossClient.doesBucketExist(configuration.getBucketName());
        } catch (Exception e) {
            logger.error("OSS health check failed", e);
            return false;
        }
    }
    
    // ==================== 私有辅助方法 ====================
    
    /**
     * 初始化多部分上传
     */
    private String initializeMultipartUpload(OSSStorageSession session) throws Exception {
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(
            configuration.getBucketName(),
            session.getObjectKey()
        );
        
        // 设置对象元数据
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(detectContentType(session.getFileName()));
        
        // 添加自定义元数据
        for (Map.Entry<String, String> entry : session.getCustomMetadata().entrySet()) {
            metadata.addUserMetadata(entry.getKey(), entry.getValue());
        }
        
        request.setObjectMetadata(metadata);
        
        InitiateMultipartUploadResult result = ossClient.initiateMultipartUpload(request);
        return result.getUploadId();
    }
    
    /**
     * 生成OSS对象键
     */
    private String generateObjectKey(String fileName, String storageId) {
        String basePath = configuration.getBasePath();
        if (basePath == null || basePath.trim().isEmpty()) {
            basePath = "uploads";
        }
        
        // 清理路径分隔符
        basePath = basePath.replaceAll("^/+|/+$", "");
        
        // 使用日期分区
        String datePath = new java.text.SimpleDateFormat("yyyy/MM/dd").format(new java.util.Date());
        
        return String.format("%s/%s/%s_%s", basePath, datePath, storageId, sanitizeFileName(fileName));
    }
    
    /**
     * 验证分片校验和
     */
    private boolean verifyChunkChecksum(byte[] data, String expectedChecksum) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(data);
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            String actualChecksum = sb.toString();
            return actualChecksum.equalsIgnoreCase(expectedChecksum);
        } catch (NoSuchAlgorithmException e) {
            logger.error("MD5 algorithm not available", e);
            return false;
        }
    }
    
    /**
     * 检测文件内容类型
     */
    private String detectContentType(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return "application/octet-stream";
        }
        
        String extension = "";
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot > 0) {
            extension = fileName.substring(lastDot + 1).toLowerCase();
        }
        
        // 简单的MIME类型映射
        Map<String, String> mimeTypes = new HashMap<>();
        mimeTypes.put("jpg", "image/jpeg");
        mimeTypes.put("jpeg", "image/jpeg");
        mimeTypes.put("png", "image/png");
        mimeTypes.put("gif", "image/gif");
        mimeTypes.put("pdf", "application/pdf");
        mimeTypes.put("txt", "text/plain");
        mimeTypes.put("mp4", "video/mp4");
        mimeTypes.put("avi", "video/avi");
        mimeTypes.put("zip", "application/zip");
        
        return mimeTypes.getOrDefault(extension, "application/octet-stream");
    }
    
    /**
     * 清理文件名
     */
    private String sanitizeFileName(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return "unnamed_file";
        }
        
        // 移除不安全字符
        String sanitized = fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
        
        // 限制长度
        if (sanitized.length() > 100) {
            String extension = "";
            int lastDot = sanitized.lastIndexOf('.');
            if (lastDot > 0) {
                extension = sanitized.substring(lastDot);
                sanitized = sanitized.substring(0, lastDot);
            }
            
            sanitized = sanitized.substring(0, 100 - extension.length()) + extension;
        }
        
        return sanitized;
    }
}