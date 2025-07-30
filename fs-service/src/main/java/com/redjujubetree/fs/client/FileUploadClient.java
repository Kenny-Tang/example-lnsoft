package com.redjujubetree.fs.client;

import com.redjujubetree.fs.grpc.FileTransferProto.*;
import com.redjujubetree.fs.grpc.FileTransferServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 文件上传客户端 - 与FileTransferServiceImpl完全兼容
 * 支持分片上传、断点续传、进度跟踪、错误重试
 */
@Slf4j
public class FileUploadClient {
    
    private final ManagedChannel channel;
    private final FileTransferServiceGrpc.FileTransferServiceBlockingStub blockingStub;
    private final FileTransferServiceGrpc.FileTransferServiceStub asyncStub;
    private final String clientId;
    
    // 默认配置
    private static final int DEFAULT_CHUNK_SIZE = 64 * 1024; // 64KB，与服务端限制匹配
    private static final int MAX_CHUNK_SIZE = 10 * 1024 * 1024; // 10MB，服务端限制
    private static final long UPLOAD_TIMEOUT_SECONDS = 600; // 10分钟
    private static final int MAX_RETRY_COUNT = 3; // 最大重试次数
    
    public FileUploadClient(String host, int port, String clientId) {
        channel = NettyChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .keepAliveTime(30, TimeUnit.SECONDS)           // 每 30 秒发一次 ping
                .keepAliveTimeout(10, TimeUnit.SECONDS)        // 超时 10 秒视为失败
                .keepAliveWithoutCalls(true)                   // 即使空闲也保持连接
                .build();

        this.blockingStub = FileTransferServiceGrpc.newBlockingStub(channel);
        this.asyncStub = FileTransferServiceGrpc.newStub(channel);
        this.clientId = clientId;
        
        log.info("FileUploadClient initialized - Host: {}, Port: {}, ClientId: {}", host, port, clientId);
    }
    
    public FileUploadClient(String host, int port) {
        this(host, port, "client-" + System.currentTimeMillis());
    }
    
    /**
     * 上传文件 - 主入口方法
     */
    public UploadResult uploadFile(String filePath, Integer chunkSize, ProgressCallback progressCallback) {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            return UploadResult.failure("File not found: " + filePath);
        }
        
        // 验证分片大小
        int actualChunkSize = validateChunkSize(chunkSize);
        
        try {
            log.info("Starting upload - File: {}, Size: {} bytes, ChunkSize: {} bytes", 
                    file.getName(), file.length(), actualChunkSize);
            
            // 1. 计算文件哈希
            String fileHash = calculateFileHash(file);
            log.debug("File hash calculated: {}", fileHash);
            
            // 2. 初始化上传
            InitiateUploadResponse initResponse = initiateUpload(file, fileHash, actualChunkSize);
            if (!initResponse.getSuccess()) {
                return UploadResult.failure("Failed to initiate upload: " + initResponse.getMessage());
            }
            
            // 注意：服务端返回的是storageId作为uploadId
            String uploadId = initResponse.getUploadId();
            Set<Integer> uploadedChunks = new HashSet<>(initResponse.getUploadedChunksList());
            int totalChunks = initResponse.getTotalChunks();
            
            log.info("Upload initiated - UploadId: {}, TotalChunks: {}, AlreadyUploaded: {}", 
                    uploadId, totalChunks, uploadedChunks.size());
            
            // 3. 上传分片
            UploadResult uploadResult = uploadChunks(file, uploadId, actualChunkSize, 
                                                   uploadedChunks, totalChunks, progressCallback);
            if (!uploadResult.success) {
                return uploadResult;
            }
            
            // 4. 验证上传完整性
            if (!verifyUploadComplete(uploadId, totalChunks)) {
                return UploadResult.failure("Upload verification failed - some chunks missing");
            }
            
            // 5. 完成上传
            CompleteUploadResponse completeResponse = completeUpload(uploadId);
            if (!completeResponse.getSuccess()) {
                return UploadResult.failure("Failed to complete upload: " + completeResponse.getMessage());
            }
            
            log.info("Upload completed successfully - File: {}, Path: {}, Hash: {}", 
                    file.getName(), completeResponse.getFilePath(), completeResponse.getFileHash());
            
            return UploadResult.success(
                completeResponse.getMessage(), 
                completeResponse.getFilePath(), 
                completeResponse.getFileHash()
            );
            
        } catch (Exception e) {
            log.error("Upload failed for file: {}", filePath, e);
            return UploadResult.failure("Upload failed: " + e.getMessage(), e);
        }
    }
    
    /**
     * 简化调用方法
     */
    public UploadResult uploadFile(String filePath) {
        return uploadFile(filePath, null, null);
    }
    
    public UploadResult uploadFile(String filePath, ProgressCallback progressCallback) {
        return uploadFile(filePath, null, progressCallback);
    }
    
    /**
     * 初始化上传
     */
    private InitiateUploadResponse initiateUpload(File file, String fileHash, int chunkSize) {
        InitiateUploadRequest request = InitiateUploadRequest.newBuilder()
                .setFileName(file.getName())
                .setFileSize(file.length())
                .setFileHash(fileHash)
                .setChunkSize(chunkSize)
                .setClientId(clientId)
                .build();
        
        return blockingStub.initiateUpload(request);
    }
    
    /**
     * 上传分片 - 使用单个流连接
     */
    private UploadResult uploadChunks(File file, String uploadId, int chunkSize, 
                                    Set<Integer> uploadedChunks, int totalChunks,
                                    ProgressCallback progressCallback) throws IOException, InterruptedException {
        
        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean success = new AtomicBoolean(true);
        AtomicInteger completedChunks = new AtomicInteger(uploadedChunks.size());
        StringBuilder errorMessage = new StringBuilder();
        
        // 创建流观察器
        StreamObserver<UploadChunkRequest> requestObserver = asyncStub.uploadChunk(
            new StreamObserver<>() {
                @Override
                public void onNext(UploadChunkResponse response) {
                    if (response.getSuccess()) {
                        int completed = completedChunks.incrementAndGet();
                        
                        // 更新进度
                        if (progressCallback != null) {
                            double progress = (double) completed / totalChunks;
                            progressCallback.onProgress(progress, completed, totalChunks);
                        }
                        
                        log.info("Chunk {} uploaded successfully ({}/{})",
                                response.getChunkIndex(), completed, totalChunks);
                    } else {
                        success.set(false);
                        String error = String.format("Chunk %d failed: %s", 
                                response.getChunkIndex(), response.getMessage());
                        errorMessage.append(error).append("; ");
                        log.error(error);
                    }
                }
                
                @Override
                public void onError(Throwable t) {
                    success.set(false);
                    errorMessage.append("Stream error: ").append(t.getMessage());
                    log.error("Upload stream error", t);
                    latch.countDown();
                }
                
                @Override
                public void onCompleted() {
                    log.info("Upload stream completed");
                    latch.countDown();
                }
            }
        );
        
        try {
            // 发送分片
            sendChunks(file, uploadId, chunkSize, uploadedChunks, requestObserver);
            log.info("All chunks sent, waiting for completion...");
            // 等待完成
            if (!latch.await(UPLOAD_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                return UploadResult.failure("Upload timeout after " + UPLOAD_TIMEOUT_SECONDS + " seconds");
            }
            
            if (!success.get()) {
                return UploadResult.failure(errorMessage.toString());
            }
            
            return UploadResult.success("Chunks uploaded successfully", null, null);
            
        } catch (Exception e) {
            requestObserver.onError(e);
            throw e;
        }
    }
    
    /**
     * 发送文件分片
     */
    private void sendChunks(File file, String uploadId, int chunkSize, 
                           Set<Integer> uploadedChunks, 
                           StreamObserver<UploadChunkRequest> requestObserver) throws IOException {
        
        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis, chunkSize * 2)) {
            
            byte[] buffer = new byte[chunkSize];
            int chunkIndex = 0;
            long offset = 0;
            long fileSize = file.length();
            
            while (offset < fileSize) {
                // 跳过已上传的分片
                if (uploadedChunks.contains(chunkIndex)) {
                    long skipBytes = Math.min(chunkSize, fileSize - offset);
                    bis.skip(skipBytes);
                    offset += skipBytes;
                    chunkIndex++;
                    continue;
                }
                
                int bytesRead = bis.read(buffer);
                if (bytesRead <= 0) break;
                
                // 计算分片哈希 - 与服务端验证保持一致
                String chunkHash = calculateChunkHash(buffer, bytesRead);
                
                // 创建上传请求 - 严格按照服务端验证要求
                UploadChunkRequest chunkRequest = UploadChunkRequest.newBuilder()
                        .setUploadId(uploadId)
                        .setChunkIndex(chunkIndex)
                        .setChunkOffset(offset)
                        .setChunkSize(bytesRead)
                        .setChunkData(com.google.protobuf.ByteString.copyFrom(buffer, 0, bytesRead))
                        .setChunkHash(chunkHash)
                        .setTimestamp(System.currentTimeMillis())
                        .build();
                
                requestObserver.onNext(chunkRequest);
                
                offset += bytesRead;
                chunkIndex++;
                
                // 添加小延迟避免服务端过载
                if (chunkIndex % 10 == 0) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new IOException("Upload interrupted", e);
                    }
                }
            }
            
            requestObserver.onCompleted();
            log.info("All chunks sent - Total: {}", chunkIndex);
            
        } catch (Exception e) {
            requestObserver.onError(e);
            throw e;
        }
    }
    
    /**
     * 验证上传完整性
     */
    private boolean verifyUploadComplete(String uploadId, int totalChunks) {
        try {
            GetUploadStatusResponse status = getUploadStatus(uploadId);
            if (!status.getSuccess()) {
                log.error("Failed to get upload status: {}", status.getMessage());
                return false;
            }
            
            boolean isComplete = status.getUploadedCount() == totalChunks && 
                               status.getMissingChunksList().isEmpty();
            
            log.info("Upload verification - Complete: {}, Progress: {:.2f}%, Uploaded: {}/{}", 
                    isComplete, status.getProgress() * 100, status.getUploadedCount(), totalChunks);
            
            return isComplete;
            
        } catch (Exception e) {
            log.error("Error verifying upload completion", e);
            return false;
        }
    }
    
    /**
     * 完成上传
     */
    private CompleteUploadResponse completeUpload(String uploadId) {
        CompleteUploadRequest request = CompleteUploadRequest.newBuilder()
                .setUploadId(uploadId)
                .build();
        
        return blockingStub.completeUpload(request);
    }
    
    /**
     * 查询上传状态
     */
    public GetUploadStatusResponse getUploadStatus(String uploadId) {
        GetUploadStatusRequest request = GetUploadStatusRequest.newBuilder()
                .setUploadId(uploadId)
                .build();
        
        return blockingStub.getUploadStatus(request);
    }
    
    /**
     * 取消上传
     */
    public CancelUploadResponse cancelUpload(String uploadId) {
        CancelUploadRequest request = CancelUploadRequest.newBuilder()
                .setUploadId(uploadId)
                .build();
        
        return blockingStub.cancelUpload(request);
    }
    
    /**
     * 验证分片大小
     */
    private int validateChunkSize(Integer chunkSize) {
        if (chunkSize == null) {
            return DEFAULT_CHUNK_SIZE;
        }
        
        if (chunkSize <= 0 || chunkSize > MAX_CHUNK_SIZE) {
            log.warn("Invalid chunk size: {}, using default: {}", chunkSize, DEFAULT_CHUNK_SIZE);
            return DEFAULT_CHUNK_SIZE;
        }
        
        return chunkSize;
    }
    
    /**
     * 计算文件哈希
     */
    private String calculateFileHash(File file) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        
        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis)) {
            
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                md.update(buffer, 0, bytesRead);
            }
        }
        
        return bytesToHex(md.digest());
    }
    
    /**
     * 计算分片哈希
     */
    private String calculateChunkHash(byte[] data, int length) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(data, 0, length);
            return bytesToHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not available", e);
        }
    }
    
    /**
     * 字节数组转十六进制字符串
     */
    private String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
    
    /**
     * 关闭客户端
     */
    public void shutdown() {
        if (!channel.isShutdown()) {
            log.info("Shutting down FileUploadClient");
            channel.shutdown();
            try {
                if (!channel.awaitTermination(5, TimeUnit.SECONDS)) {
                    log.warn("Forcing channel shutdown");
                    channel.shutdownNow();
                }
            } catch (InterruptedException e) {
                channel.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
    
    /**
     * 进度回调接口
     */
    public interface ProgressCallback {
        void onProgress(double progress, int completedChunks, int totalChunks);
    }
    
    /**
     * 上传结果类
     */
    public static class UploadResult {
        public final boolean success;
        public final String message;
        public final String filePath;
        public final String fileHash;
        public final Throwable error;
        
        private UploadResult(boolean success, String message, String filePath, String fileHash, Throwable error) {
            this.success = success;
            this.message = message;
            this.filePath = filePath;
            this.fileHash = fileHash;
            this.error = error;
        }
        
        public static UploadResult success(String message, String filePath, String fileHash) {
            return new UploadResult(true, message, filePath, fileHash, null);
        }
        
        public static UploadResult failure(String message) {
            return new UploadResult(false, message, null, null, null);
        }
        
        public static UploadResult failure(String message, Throwable error) {
            return new UploadResult(false, message, null, null, error);
        }
        
        @Override
        public String toString() {
            return String.format("UploadResult{success=%s, message='%s', filePath='%s', fileHash='%s'}", 
                               success, message, filePath, fileHash);
        }
    }
    
    /**
     * 使用示例
     */
    public static void main(String[] args) {
        String serverHost = "localhost";
        int serverPort = 9090;
        String filePath = "test.pdf";
        
        FileUploadClient client = new FileUploadClient(serverHost, serverPort);
        
        try {
            log.info("开始上传文件: {}", filePath);
            
            // 带进度回调的上传
            UploadResult result = client.uploadFile(filePath, (progress, completed, total) -> {
                System.out.printf("\r进度: %.1f%% (%d/%d chunks)", 
                                progress * 100, completed, total);
                if (completed == total) {
                    System.out.println(); // 换行
                }
            });
            
            if (result.success) {
                System.out.println("✅ 文件上传成功!");
                System.out.println("消息: " + result.message);
                System.out.println("服务端路径: " + result.filePath);
                System.out.println("文件哈希: " + result.fileHash);
            } else {
                System.err.println("❌ 文件上传失败!");
                System.err.println("错误: " + result.message);
                if (result.error != null) {
                    result.error.printStackTrace();
                }
            }
            
        } catch (Exception e) {
            log.error("客户端异常", e);
        } finally {
            client.shutdown();
        }
    }
}