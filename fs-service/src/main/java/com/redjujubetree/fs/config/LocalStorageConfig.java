package com.redjujubetree.fs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.concurrent.locks.Condition;

/**
 * 本地文件存储配置
 */
@Configuration
@ConfigurationProperties(prefix = "file.storage.local")
@Validated
public class LocalStorageConfig {
    private final Condition newData = null;

    /**
     * 临时文件目录
     */
    @NotBlank
    private String tempDir = "./temp";
    
    /**
     * 最终文件存储目录
     */
    @NotBlank
    private String finalDir = "./uploads";
    
    /**
     * 是否自动创建目录
     */
    private boolean createDirs = true;
    
    /**
     * 分片缓冲区大小
     */
    @Min(1024)
    private int chunkBufferSize = 8192;
    
    /**
     * 是否启用文件锁
     */
    private boolean enableFileLock = true;
    
    /**
     * 会话超时时间（毫秒）
     */
    @Min(60000) // 最少1分钟
    private long sessionTimeoutMs = 2 * 60 * 60 * 1000; // 2小时
    
    /**
     * 清理任务执行间隔（毫秒）
     */
    @Min(60000) // 最少1分钟
    private long cleanupIntervalMs = 5 * 60 * 1000; // 5分钟
    
    /**
     * 健康检查间隔（毫秒）
     */
    @Min(30000) // 最少30秒
    private long healthCheckIntervalMs = 60 * 1000; // 1分钟
    
    /**
     * 最小可用磁盘空间（字节）
     */
    @Min(1024 * 1024) // 最少1MB
    private long minFreeSpaceBytes = 100 * 1024 * 1024; // 100MB
    
    /**
     * 最大并发上传数
     */
    @Min(1)
    private int maxConcurrentUploads = 100;
    
    /**
     * 分片大小配置
     */
    @NotNull
    private ChunkSizeConfig chunkSize = new ChunkSizeConfig();
    
    /**
     * 文件名配置
     */
    @NotNull
    private FileNameConfig fileName = new FileNameConfig();
    
    /**
     * 分片大小配置
     */
    public static class ChunkSizeConfig {
        /**
         * 小文件分片大小（10MB以下）
         */
        @Min(1024)
        private int smallFileChunkSize = 256 * 1024; // 256KB
        
        /**
         * 中等文件分片大小（100MB以下）
         */
        @Min(1024)
        private int mediumFileChunkSize = 1024 * 1024; // 1MB
        
        /**
         * 大文件分片大小（1GB以下）
         */
        @Min(1024)
        private int largeFileChunkSize = 2 * 1024 * 1024; // 2MB
        
        /**
         * 超大文件分片大小（1GB以上）
         */
        @Min(1024)
        private int extraLargeFileChunkSize = 4 * 1024 * 1024; // 4MB
        
        /**
         * 小文件阈值
         */
        @Min(1024)
        private long smallFileThreshold = 10 * 1024 * 1024; // 10MB
        
        /**
         * 中等文件阈值
         */
        @Min(1024)
        private long mediumFileThreshold = 100 * 1024 * 1024; // 100MB
        
        /**
         * 大文件阈值
         */
        @Min(1024)
        private long largeFileThreshold = 1024 * 1024 * 1024; // 1GB
        
        // Getters and Setters
        public int getSmallFileChunkSize() { return smallFileChunkSize; }
        public void setSmallFileChunkSize(int smallFileChunkSize) { this.smallFileChunkSize = smallFileChunkSize; }
        
        public int getMediumFileChunkSize() { return mediumFileChunkSize; }
        public void setMediumFileChunkSize(int mediumFileChunkSize) { this.mediumFileChunkSize = mediumFileChunkSize; }
        
        public int getLargeFileChunkSize() { return largeFileChunkSize; }
        public void setLargeFileChunkSize(int largeFileChunkSize) { this.largeFileChunkSize = largeFileChunkSize; }
        
        public int getExtraLargeFileChunkSize() { return extraLargeFileChunkSize; }
        public void setExtraLargeFileChunkSize(int extraLargeFileChunkSize) { this.extraLargeFileChunkSize = extraLargeFileChunkSize; }
        
        public long getSmallFileThreshold() { return smallFileThreshold; }
        public void setSmallFileThreshold(long smallFileThreshold) { this.smallFileThreshold = smallFileThreshold; }
        
        public long getMediumFileThreshold() { return mediumFileThreshold; }
        public void setMediumFileThreshold(long mediumFileThreshold) { this.mediumFileThreshold = mediumFileThreshold; }
        
        public long getLargeFileThreshold() { return largeFileThreshold; }
        public void setLargeFileThreshold(long largeFileThreshold) { this.largeFileThreshold = largeFileThreshold; }
    }
    
    /**
     * 文件名配置
     */
    public static class FileNameConfig {
        /**
         * 是否在文件名中包含时间戳
         */
        private boolean includeTimestamp = true;
        
        /**
         * 是否在文件名中包含存储ID
         */
        private boolean includeStorageId = true;
        
        /**
         * 存储ID截取长度
         */
        @Min(4)
        private int storageIdLength = 8;
        
        /**
         * 文件名分隔符
         */
        @NotBlank
        private String separator = "_";
        
        /**
         * 是否保持原始文件名
         */
        private boolean preserveOriginalName = true;
        
        /**
         * 最大文件名长度
         */
        @Min(10)
        private int maxFileNameLength = 255;
        
        // Getters and Setters
        public boolean isIncludeTimestamp() { return includeTimestamp; }
        public void setIncludeTimestamp(boolean includeTimestamp) { this.includeTimestamp = includeTimestamp; }
        
        public boolean isIncludeStorageId() { return includeStorageId; }
        public void setIncludeStorageId(boolean includeStorageId) { this.includeStorageId = includeStorageId; }
        
        public int getStorageIdLength() { return storageIdLength; }
        public void setStorageIdLength(int storageIdLength) { this.storageIdLength = storageIdLength; }
        
        public String getSeparator() { return separator; }
        public void setSeparator(String separator) { this.separator = separator; }
        
        public boolean isPreserveOriginalName() { return preserveOriginalName; }
        public void setPreserveOriginalName(boolean preserveOriginalName) { this.preserveOriginalName = preserveOriginalName; }
        
        public int getMaxFileNameLength() { return maxFileNameLength; }
        public void setMaxFileNameLength(int maxFileNameLength) { this.maxFileNameLength = maxFileNameLength; }
    }
    
    // Main class getters and setters
    public String getTempDir() { return tempDir; }
    public void setTempDir(String tempDir) { this.tempDir = tempDir; }
    
    public String getFinalDir() { return finalDir; }
    public void setFinalDir(String finalDir) { this.finalDir = finalDir; }
    
    public boolean isCreateDirs() { return createDirs; }
    public void setCreateDirs(boolean createDirs) { this.createDirs = createDirs; }
    
    public int getChunkBufferSize() { return chunkBufferSize; }
    public void setChunkBufferSize(int chunkBufferSize) { this.chunkBufferSize = chunkBufferSize; }
    
    public boolean isEnableFileLock() { return enableFileLock; }
    public void setEnableFileLock(boolean enableFileLock) { this.enableFileLock = enableFileLock; }
    
    public long getSessionTimeoutMs() { return sessionTimeoutMs; }
    public void setSessionTimeoutMs(long sessionTimeoutMs) { this.sessionTimeoutMs = sessionTimeoutMs; }
    
    public long getCleanupIntervalMs() { return cleanupIntervalMs; }
    public void setCleanupIntervalMs(long cleanupIntervalMs) { this.cleanupIntervalMs = cleanupIntervalMs; }
    
    public long getHealthCheckIntervalMs() { return healthCheckIntervalMs; }
    public void setHealthCheckIntervalMs(long healthCheckIntervalMs) { this.healthCheckIntervalMs = healthCheckIntervalMs; }
    
    public long getMinFreeSpaceBytes() { return minFreeSpaceBytes; }
    public void setMinFreeSpaceBytes(long minFreeSpaceBytes) { this.minFreeSpaceBytes = minFreeSpaceBytes; }
    
    public int getMaxConcurrentUploads() { return maxConcurrentUploads; }
    public void setMaxConcurrentUploads(int maxConcurrentUploads) { this.maxConcurrentUploads = maxConcurrentUploads; }
    
    public ChunkSizeConfig getChunkSize() { return chunkSize; }
    public void setChunkSize(ChunkSizeConfig chunkSize) { this.chunkSize = chunkSize; }
    
    public FileNameConfig getFileName() { return fileName; }
    public void setFileName(FileNameConfig fileName) { this.fileName = fileName; }
}