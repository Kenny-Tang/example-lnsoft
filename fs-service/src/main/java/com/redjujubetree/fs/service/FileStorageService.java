package com.redjujubetree.fs.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 文件存储服务接口
 * 支持本地存储、分布式文件系统、云存储等多种实现
 */
public interface FileStorageService {

    /**
     * 存储类型枚举
     */
    enum StorageType {
        LOCAL_TEMP,      // 本地临时存储
        LOCAL_PERSISTENT, // 本地持久化存储
        OSS,             // 阿里云OSS
        S3,              // AWS S3
        MINIO,           // MinIO
        HDFS             // Hadoop分布式文件系统
    }

    /**
     * 文件存储元数据
     */
    class StorageMetadata {
        private final String storageId;
        private final String originalFileName;
        private final long fileSize;
        private final String contentType;
        private final String checksum;
        private final long createdTime;
        private final Map<String, String> customMetadata;

        public StorageMetadata(String storageId, String originalFileName, long fileSize, 
                              String contentType, String checksum, long createdTime,
                              Map<String, String> customMetadata) {
            this.storageId = storageId;
            this.originalFileName = originalFileName;
            this.fileSize = fileSize;
            this.contentType = contentType;
            this.checksum = checksum;
            this.createdTime = createdTime;
            this.customMetadata = customMetadata;
        }

        // Getters
        public String getStorageId() { return storageId; }
        public String getOriginalFileName() { return originalFileName; }
        public long getFileSize() { return fileSize; }
        public String getContentType() { return contentType; }
        public String getChecksum() { return checksum; }
        public long getCreatedTime() { return createdTime; }
        public Map<String, String> getCustomMetadata() { return customMetadata; }
    }

    /**
     * 分片写入结果
     */
    class ChunkWriteResult {
        private final boolean success;
        private final String message;
        private final long writtenBytes;
        private final String chunkChecksum;

        public ChunkWriteResult(boolean success, String message, long writtenBytes, String chunkChecksum) {
            this.success = success;
            this.message = message;
            this.writtenBytes = writtenBytes;
            this.chunkChecksum = chunkChecksum;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public long getWrittenBytes() { return writtenBytes; }
        public String getChunkChecksum() { return chunkChecksum; }
    }

    /**
     * 文件完成结果
     */
    class FileCompleteResult {
        private final boolean success;
        private final String message;
        private final String finalPath;
        private final String actualChecksum;
        private final StorageMetadata metadata;

        public FileCompleteResult(boolean success, String message, String finalPath, 
                                String actualChecksum, StorageMetadata metadata) {
            this.success = success;
            this.message = message;
            this.finalPath = finalPath;
            this.actualChecksum = actualChecksum;
            this.metadata = metadata;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public String getFinalPath() { return finalPath; }
        public String getActualChecksum() { return actualChecksum; }
        public StorageMetadata getMetadata() { return metadata; }
    }

    // ==================== 核心接口方法 ====================

    /**
     * 初始化文件存储
     * @param storageId 存储标识符
     * @param fileName 原始文件名
     * @param fileSize 文件总大小
     * @param expectedChecksum 期望的文件校验和
     * @param customMetadata 自定义元数据
     * @return 是否初始化成功
     */
    boolean initializeStorage(String storageId, String fileName, long fileSize, 
                            String expectedChecksum, Map<String, String> customMetadata) throws IOException;

    /**
     * 写入文件分片
     * @param storageId 存储标识符
     * @param chunkIndex 分片索引
     * @param offset 文件偏移量
     * @param data 分片数据
     * @param chunkChecksum 分片校验和
     * @return 写入结果
     */
    ChunkWriteResult writeChunk(String storageId, int chunkIndex, long offset, 
                              byte[] data, String chunkChecksum) throws IOException;

    /**
     * 批量写入分片（支持并发优化）
     * @param storageId 存储标识符
     * @param chunks 分片数据列表
     * @return 写入结果列表
     */
    List<ChunkWriteResult> writeChunks(String storageId, 
                                     List<ChunkData> chunks) throws IOException;

    /**
     * 完成文件上传（合并分片，移动到最终位置）
     * @param storageId 存储标识符
     * @return 完成结果
     */
    FileCompleteResult completeUpload(String storageId) throws IOException;

    /**
     * 取消文件上传（清理临时文件）
     * @param storageId 存储标识符
     * @return 是否取消成功
     */
    boolean cancelUpload(String storageId) throws IOException;

    /**
     * 检查分片是否已存在
     * @param storageId 存储标识符
     * @param chunkIndex 分片索引
     * @return 是否存在
     */
    boolean chunkExists(String storageId, int chunkIndex) throws IOException;

    /**
     * 获取已上传的分片列表
     * @param storageId 存储标识符
     * @return 已上传分片索引列表
     */
    List<Integer> getUploadedChunks(String storageId) throws IOException;

    /**
     * 获取文件存储元数据
     * @param storageId 存储标识符
     * @return 存储元数据
     */
    StorageMetadata getStorageMetadata(String storageId) throws IOException;

    /**
     * 删除文件
     * @param storageId 存储标识符
     * @return 是否删除成功
     */
    boolean deleteFile(String storageId) throws IOException;

    /**
     * 获取文件输入流
     * @param storageId 存储标识符
     * @return 文件输入流
     */
    InputStream getFileInputStream(String storageId) throws IOException;

    /**
     * 验证文件完整性
     * @param storageId 存储标识符
     * @param expectedChecksum 期望校验和
     * @return 验证结果
     */
    boolean verifyFileIntegrity(String storageId, String expectedChecksum) throws IOException;

    /**
     * 清理过期的临时文件
     * @param timeoutMs 超时时间（毫秒）
     * @return 清理的文件数量
     */
    int cleanupExpiredFiles(long timeoutMs) throws IOException;

    /**
     * 获取存储类型
     * @return 存储类型
     */
    StorageType getStorageType();

    /**
     * 健康检查
     * @return 是否健康
     */
    boolean healthCheck();

    // ==================== 辅助数据类 ====================

    /**
     * 分片数据
     */
    class ChunkData {
        private final int chunkIndex;
        private final long offset;
        private final byte[] data;
        private final String checksum;

        public ChunkData(int chunkIndex, long offset, byte[] data, String checksum) {
            this.chunkIndex = chunkIndex;
            this.offset = offset;
            this.data = data;
            this.checksum = checksum;
        }

        public int getChunkIndex() { return chunkIndex; }
        public long getOffset() { return offset; }
        public byte[] getData() { return data; }
        public String getChecksum() { return checksum; }
    }
}

// ==================== 存储配置接口 ====================

