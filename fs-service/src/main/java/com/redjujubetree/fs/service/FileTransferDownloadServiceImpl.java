// FileTransferDownloadServiceImpl.java
package com.redjujubetree.fs.service;

import com.redjujubetree.fs.grpc.FileTransferDownloadProto.*;
import com.redjujubetree.fs.grpc.FileTransferDownloadServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class FileTransferDownloadServiceImpl extends FileTransferDownloadServiceGrpc.FileTransferDownloadServiceImplBase {
    
    private static final Logger logger = LoggerFactory.getLogger(FileTransferDownloadServiceImpl.class);
    
    // 连接池相关
    private final Map<String, ClientConnection> activeConnections = new ConcurrentHashMap<>();
    private final ScheduledExecutorService connectionCleanupExecutor = Executors.newSingleThreadScheduledExecutor();
    private final ExecutorService downloadExecutor = Executors.newFixedThreadPool(50);
    
    // 下载会话管理
    private final Map<String, DownloadSession> downloadSessions = new ConcurrentHashMap<>();
    private final AtomicInteger sessionIdGenerator = new AtomicInteger(0);
    
    // 配置参数
    private static final int DEFAULT_CHUNK_SIZE = 64 * 1024; // 64KB
    private static final int MAX_CHUNK_SIZE = 1024 * 1024;   // 1MB
    private static final long CONNECTION_TIMEOUT_MS = 300000; // 5分钟
    private static final String FILE_STORAGE_PATH = "/var/fileserver/files"; // 文件存储路径
    
    @PostConstruct
    public void init() {
        // 启动连接清理任务
        connectionCleanupExecutor.scheduleAtFixedRate(this::cleanupExpiredConnections, 60, 60, TimeUnit.SECONDS);
        logger.info("FileTransferDownloadService initialized");
    }
    
    @PreDestroy
    public void destroy() {
        connectionCleanupExecutor.shutdown();
        downloadExecutor.shutdown();
        logger.info("FileTransferDownloadService destroyed");
    }
    
    // 客户端连接管理
    private static class ClientConnection {
        private final String clientId;
        private final long createTime;
        private volatile long lastAccessTime;
        private final Set<String> activeSessions;
        
        public ClientConnection(String clientId) {
            this.clientId = clientId;
            this.createTime = System.currentTimeMillis();
            this.lastAccessTime = createTime;
            this.activeSessions = ConcurrentHashMap.newKeySet();
        }
        
        public void updateLastAccess() {
            this.lastAccessTime = System.currentTimeMillis();
        }
        
        public boolean isExpired(long timeoutMs) {
            return System.currentTimeMillis() - lastAccessTime > timeoutMs;
        }
    }
    
    // 下载会话
    private static class DownloadSession {
        private final String downloadId;
        private final String fileId;
        private final String clientId;
        private final FileInfo fileInfo;
        private final long startOffset;
        private final long endOffset;
        private final int chunkSize;
        private final int totalChunks;
        private final long downloadSize;
        private volatile String status; // DOWNLOADING, COMPLETED, FAILED, CANCELLED, PAUSED
        private final Set<Integer> downloadedChunks;
        private final Set<Integer> pendingChunks;
        private final AtomicLong downloadedBytes;
        private final long createTime;
        private volatile long lastUpdateTime;
        private final AtomicLong totalBytesTransferred;
        
        public DownloadSession(String downloadId, String fileId, String clientId, FileInfo fileInfo,
                             long startOffset, long endOffset, int chunkSize) {
            this.downloadId = downloadId;
            this.fileId = fileId;
            this.clientId = clientId;
            this.fileInfo = fileInfo;
            this.startOffset = startOffset;
            this.endOffset = endOffset;
            this.chunkSize = chunkSize;
            this.downloadSize = endOffset - startOffset;
            this.totalChunks = (int) Math.ceil((double) downloadSize / chunkSize);
            this.status = "DOWNLOADING";
            this.downloadedChunks = ConcurrentHashMap.newKeySet();
            this.pendingChunks = ConcurrentHashMap.newKeySet();
            this.downloadedBytes = new AtomicLong(0);
            this.createTime = System.currentTimeMillis();
            this.lastUpdateTime = createTime;
            this.totalBytesTransferred = new AtomicLong(0);
            
            // 初始化待下载分片
            for (int i = 0; i < totalChunks; i++) {
                pendingChunksSet().add(i);
            }
        }
        
        public Set<Integer> pendingChunksSet() {
            return pendingChunks;
        }
        
        public void markChunkDownloaded(int chunkIndex, int bytesTransferred) {
            pendingChunks.remove(chunkIndex);
            downloadedChunks.add(chunkIndex);
            downloadedBytes.addAndGet(bytesTransferred);
            totalBytesTransferred.addAndGet(bytesTransferred);
            lastUpdateTime = System.currentTimeMillis();
            
            if (pendingChunks.isEmpty()) {
                status = "COMPLETED";
            }
        }
        
        public double getProgress() {
            return totalChunks > 0 ? (double) downloadedChunks.size() / totalChunks : 0.0;
        }
        
        public double getDownloadSpeed() {
            long timeDiff = System.currentTimeMillis() - createTime;
            return timeDiff > 0 ? (double) totalBytesTransferred.get() * 1000 / timeDiff : 0.0;
        }
    }
    
    @Override
    public void initiateDownload(InitiateDownloadRequest request, StreamObserver<InitiateDownloadResponse> responseObserver) {
        try {
            logger.info("Initiating download for file: {}, client: {}", request.getFileId(), request.getClientId());
            
            // 更新客户端连接
            updateClientConnection(request.getClientId());
            
            // 获取文件信息
            FileInfo fileInfo = getFileInfoById(request.getFileId());
            if (fileInfo == null) {
                responseObserver.onNext(InitiateDownloadResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("File not found: " + request.getFileId())
                    .build());
                responseObserver.onCompleted();
                return;
            }
            
            // 计算下载范围
            long startOffset = request.getStartOffset();
            long endOffset = request.getEndOffset() == -1 ? fileInfo.getFileSize() : Math.min(request.getEndOffset(), fileInfo.getFileSize());
            
            if (startOffset >= fileInfo.getFileSize() || startOffset >= endOffset) {
                responseObserver.onNext(InitiateDownloadResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Invalid download range")
                    .build());
                responseObserver.onCompleted();
                return;
            }
            
            // 确定分片大小
            int chunkSize = request.getPreferredChunkSize() > 0 ? 
                Math.min(request.getPreferredChunkSize(), MAX_CHUNK_SIZE) : DEFAULT_CHUNK_SIZE;
            
            // 创建下载会话
            String downloadId = generateDownloadId();
            DownloadSession session = new DownloadSession(downloadId, request.getFileId(), request.getClientId(),
                fileInfo, startOffset, endOffset, chunkSize);
            
            downloadSessions.put(downloadId, session);
            
            // 将会话添加到客户端连接
            ClientConnection connection = activeConnections.get(request.getClientId());
            if (connection != null) {
                connection.activeSessions.add(downloadId);
            }
            
            InitiateDownloadResponse response = InitiateDownloadResponse.newBuilder()
                .setSuccess(true)
                .setDownloadId(downloadId)
                .setMessage("Download session created successfully")
                .setFileInfo(fileInfo)
                .setTotalChunks(session.totalChunks)
                .setChunkSize(chunkSize)
                .setDownloadSize(session.downloadSize)
                .build();
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            
            logger.info("Download session created: {}, chunks: {}", downloadId, session.totalChunks);
            
        } catch (Exception e) {
            logger.error("Error initiating download", e);
            responseObserver.onNext(InitiateDownloadResponse.newBuilder()
                .setSuccess(false)
                .setMessage("Internal error: " + e.getMessage())
                .build());
            responseObserver.onCompleted();
        }
    }
    
    @Override
    public void downloadChunk(DownloadChunkRequest request, StreamObserver<DownloadChunkResponse> responseObserver) {
        DownloadSession session = downloadSessions.get(request.getDownloadId());
        if (session == null) {
            responseObserver.onNext(DownloadChunkResponse.newBuilder()
                .setSuccess(false)
                .setDownloadId(request.getDownloadId())
                .setMessage("Download session not found")
                .build());
            responseObserver.onCompleted();
            return;
        }
        
        // 更新客户端连接
        updateClientConnection(session.clientId);
        
        if (!"DOWNLOADING".equals(session.status)) {
            responseObserver.onNext(DownloadChunkResponse.newBuilder()
                .setSuccess(false)
                .setDownloadId(request.getDownloadId())
                .setMessage("Download session is not active")
                .build());
            responseObserver.onCompleted();
            return;
        }
        
        // 处理请求的分片
        List<Integer> requestedChunks = request.getChunkIndexesList();
        int maxConcurrent = Math.min(request.getMaxConcurrentChunks(), 10); // 限制最大并发数
        
        // 使用线程池并发处理分片
        CompletableFuture<Void> allChunks = CompletableFuture.allOf(
            requestedChunks.stream()
                .limit(maxConcurrent)
                .map(chunkIndex -> processChunkAsync(session, chunkIndex, responseObserver))
                .toArray(CompletableFuture[]::new)
        );
        
        allChunks.whenComplete((result, throwable) -> {
            if (throwable != null) {
                logger.error("Error processing chunks for session: " + session.downloadId, throwable);
            }
            responseObserver.onCompleted();
        });
    }
    
    private CompletableFuture<Void> processChunkAsync(DownloadSession session, int chunkIndex, 
                                                     StreamObserver<DownloadChunkResponse> responseObserver) {
        return CompletableFuture.runAsync(() -> {
            try {
                processChunk(session, chunkIndex, responseObserver);
            } catch (Exception e) {
                logger.error("Error processing chunk {} for session {}", chunkIndex, session.downloadId, e);
                responseObserver.onNext(DownloadChunkResponse.newBuilder()
                    .setSuccess(false)
                    .setDownloadId(session.downloadId)
                    .setChunkIndex(chunkIndex)
                    .setMessage("Error processing chunk: " + e.getMessage())
                    .build());
            }
        }, downloadExecutor);
    }
    
    private void processChunk(DownloadSession session, int chunkIndex, 
                            StreamObserver<DownloadChunkResponse> responseObserver) throws IOException {
        
        if (chunkIndex >= session.totalChunks || chunkIndex < 0) {
            responseObserver.onNext(DownloadChunkResponse.newBuilder()
                .setSuccess(false)
                .setDownloadId(session.downloadId)
                .setChunkIndex(chunkIndex)
                .setMessage("Invalid chunk index")
                .build());
            return;
        }
        
        // 计算分片偏移和大小
        long chunkOffset = session.startOffset + (long) chunkIndex * session.chunkSize;
        int actualChunkSize = (int) Math.min(session.chunkSize, 
            session.startOffset + session.downloadSize - chunkOffset);
        
        // 读取文件数据
        Path filePath = Paths.get(FILE_STORAGE_PATH, session.fileId);
        byte[] chunkData = new byte[actualChunkSize];
        
        try (RandomAccessFile file = new RandomAccessFile(filePath.toFile(), "r")) {
            file.seek(chunkOffset);
            int bytesRead = file.read(chunkData);
            
            if (bytesRead != actualChunkSize) {
                throw new IOException("Failed to read expected chunk size");
            }
            
            // 计算分片哈希
            String chunkHash = calculateHash(chunkData);
            
            // 发送分片响应
            DownloadChunkResponse response = DownloadChunkResponse.newBuilder()
                .setSuccess(true)
                .setDownloadId(session.downloadId)
                .setChunkIndex(chunkIndex)
                .setChunkOffset(chunkOffset)
                .setChunkSize(actualChunkSize)
                .setChunkData(com.google.protobuf.ByteString.copyFrom(chunkData))
                .setChunkHash(chunkHash)
                .setIsLastChunk(chunkIndex == session.totalChunks - 1)
                .setMessage("Chunk delivered successfully")
                .setSentTimestamp(System.currentTimeMillis())
                .build();
            
            responseObserver.onNext(response);
            
            // 更新会话状态
            session.markChunkDownloaded(chunkIndex, actualChunkSize);
            
            logger.debug("Chunk {} delivered for session {}", chunkIndex, session.downloadId);
            
        } catch (IOException e) {
            logger.error("Error reading file chunk", e);
            responseObserver.onNext(DownloadChunkResponse.newBuilder()
                .setSuccess(false)
                .setDownloadId(session.downloadId)
                .setChunkIndex(chunkIndex)
                .setMessage("Error reading file: " + e.getMessage())
                .build());
        }
    }
    
    @Override
    public void getDownloadStatus(GetDownloadStatusRequest request, StreamObserver<GetDownloadStatusResponse> responseObserver) {
        DownloadSession session = downloadSessions.get(request.getDownloadId());
        if (session == null) {
            responseObserver.onNext(GetDownloadStatusResponse.newBuilder()
                .setSuccess(false)
                .setDownloadId(request.getDownloadId())
                .setMessage("Download session not found")
                .build());
            responseObserver.onCompleted();
            return;
        }
        
        // 更新客户端连接
        updateClientConnection(session.clientId);
        
        GetDownloadStatusResponse response = GetDownloadStatusResponse.newBuilder()
            .setSuccess(true)
            .setDownloadId(session.downloadId)
            .setStatus(session.status)
            .addAllDownloadedChunks(session.downloadedChunks)
            .addAllPendingChunks(session.pendingChunks)
            .setTotalChunks(session.totalChunks)
            .setDownloadedCount(session.downloadedChunks.size())
            .setProgress(session.getProgress())
            .setDownloadedBytes(session.downloadedBytes.get())
            .setTotalBytes(session.downloadSize)
            .setDownloadSpeed(session.getDownloadSpeed())
            .setMessage("Status retrieved successfully")
            .build();
        
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    
    @Override
    public void cancelDownload(CancelDownloadRequest request, StreamObserver<CancelDownloadResponse> responseObserver) {
        DownloadSession session = downloadSessions.get(request.getDownloadId());
        if (session == null) {
            responseObserver.onNext(CancelDownloadResponse.newBuilder()
                .setSuccess(false)
                .setMessage("Download session not found")
                .build());
            responseObserver.onCompleted();
            return;
        }
        
        session.status = "CANCELLED";
        downloadSessions.remove(request.getDownloadId());
        
        // 从客户端连接中移除会话
        ClientConnection connection = activeConnections.get(session.clientId);
        if (connection != null) {
            connection.activeSessions.remove(request.getDownloadId());
        }
        
        responseObserver.onNext(CancelDownloadResponse.newBuilder()
            .setSuccess(true)
            .setMessage("Download cancelled successfully")
            .build());
        responseObserver.onCompleted();
        
        logger.info("Download session cancelled: {}", request.getDownloadId());
    }
    
    @Override
    public void listFiles(ListFilesRequest request, StreamObserver<ListFilesResponse> responseObserver) {
        try {
            // 更新客户端连接
            updateClientConnection(request.getClientId());
            
            // 获取文件列表（这里需要根据实际存储实现）
            List<FileInfo> files = getAvailableFiles(request);
            
            // 分页处理
            int pageSize = request.getPageSize() > 0 ? Math.min(request.getPageSize(), 100) : 20;
            int startIndex = parsePageToken(request.getPageToken());
            int endIndex = Math.min(startIndex + pageSize, files.size());
            
            List<FileInfo> pageFiles = files.subList(startIndex, endIndex);
            String nextPageToken = endIndex < files.size() ? String.valueOf(endIndex) : "";
            
            ListFilesResponse response = ListFilesResponse.newBuilder()
                .setSuccess(true)
                .addAllFiles(pageFiles)
                .setNextPageToken(nextPageToken)
                .setTotalCount(files.size())
                .setMessage("Files listed successfully")
                .build();
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            
        } catch (Exception e) {
            logger.error("Error listing files", e);
            responseObserver.onNext(ListFilesResponse.newBuilder()
                .setSuccess(false)
                .setMessage("Error listing files: " + e.getMessage())
                .build());
            responseObserver.onCompleted();
        }
    }
    
    @Override
    public void getFileInfo(GetFileInfoRequest request, StreamObserver<GetFileInfoResponse> responseObserver) {
        try {
            // 更新客户端连接
            updateClientConnection(request.getClientId());
            
            FileInfo fileInfo = getFileInfoById(request.getFileId());
            if (fileInfo == null) {
                responseObserver.onNext(GetFileInfoResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("File not found: " + request.getFileId())
                    .build());
                responseObserver.onCompleted();
                return;
            }
            
            GetFileInfoResponse response = GetFileInfoResponse.newBuilder()
                .setSuccess(true)
                .setFileInfo(fileInfo)
                .setMessage("File info retrieved successfully")
                .build();
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            
        } catch (Exception e) {
            logger.error("Error getting file info", e);
            responseObserver.onNext(GetFileInfoResponse.newBuilder()
                .setSuccess(false)
                .setMessage("Error getting file info: " + e.getMessage())
                .build());
            responseObserver.onCompleted();
        }
    }
    
    // 辅助方法
    private void updateClientConnection(String clientId) {
        ClientConnection connection = activeConnections.computeIfAbsent(clientId, ClientConnection::new);
        connection.updateLastAccess();
    }
    
    private void cleanupExpiredConnections() {
        activeConnections.entrySet().removeIf(entry -> {
            ClientConnection connection = entry.getValue();
            if (connection.isExpired(CONNECTION_TIMEOUT_MS)) {
                logger.info("Cleaning up expired connection: {}", entry.getKey());
                // 清理相关的下载会话
                connection.activeSessions.forEach(sessionId -> {
                    DownloadSession session = downloadSessions.get(sessionId);
                    if (session != null) {
                        session.status = "CANCELLED";
                        downloadSessions.remove(sessionId);
                    }
                });
                return true;
            }
            return false;
        });
    }
    
    private String generateDownloadId() {
        return "download_" + System.currentTimeMillis() + "_" + sessionIdGenerator.incrementAndGet();
    }
    
    private FileInfo getFileInfoById(String fileId) {
        try {
            Path filePath = Paths.get(FILE_STORAGE_PATH, fileId);
            if (!Files.exists(filePath)) {
                return null;
            }
            
            long fileSize = Files.size(filePath);
            String fileName = filePath.getFileName().toString();
            String contentType = Files.probeContentType(filePath);
            long createTime = Files.getLastModifiedTime(filePath).toMillis();
            
            // 计算文件哈希
            String fileHash = calculateFileHash(filePath);
            
            return FileInfo.newBuilder()
                .setFileId(fileId)
                .setFileName(fileName)
                .setFileSize(fileSize)
                .setFileHash(fileHash)
                .setContentType(contentType != null ? contentType : "application/octet-stream")
                .setCreatedTimestamp(createTime)
                .setModifiedTimestamp(createTime)
                .setOwnerId("system")
                .build();
                
        } catch (IOException e) {
            logger.error("Error getting file info for: " + fileId, e);
            return null;
        }
    }
    
    private List<FileInfo> getAvailableFiles(ListFilesRequest request) {
        // 这里需要根据实际的文件存储系统实现
        // 示例实现：扫描文件目录
        try {
            Path storageDir = Paths.get(FILE_STORAGE_PATH);
            if (!Files.exists(storageDir)) {
                return new ArrayList<>();
            }
            
            return Files.list(storageDir)
                .filter(Files::isRegularFile)
                .map(path -> getFileInfoById(path.getFileName().toString()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
                
        } catch (IOException e) {
            logger.error("Error listing files", e);
            return new ArrayList<>();
        }
    }
    
    private int parsePageToken(String pageToken) {
        try {
            return pageToken.isEmpty() ? 0 : Integer.parseInt(pageToken);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    private String calculateHash(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(data);
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            logger.error("Error calculating hash", e);
            return "";
        }
    }
    
    private String calculateFileHash(Path filePath) {
        try (FileInputStream fis = new FileInputStream(filePath.toFile())) {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                md.update(buffer, 0, bytesRead);
            }
            byte[] hash = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            logger.error("Error calculating file hash", e);
            return "";
        }
    }
}