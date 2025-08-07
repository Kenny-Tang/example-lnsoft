package com.redjujubetree.fs.service;

import java.util.Map;

/**
 * OSS存储配置
 */
//@Configuration
public class OSSStorageConfiguration implements StorageConfiguration {
    private final String endpoint;
    private final String accessKeyId;
    private final String accessKeySecret;
    private final String bucketName;
    private final String basePath;
    private final boolean enableMultipartUpload;
    private final long multipartThreshold;

    public OSSStorageConfiguration(String endpoint, String accessKeyId, String accessKeySecret,
                                 String bucketName, String basePath, boolean enableMultipartUpload,
                                 long multipartThreshold) {
        this.endpoint = endpoint;
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.bucketName = bucketName;
        this.basePath = basePath;
        this.enableMultipartUpload = enableMultipartUpload;
        this.multipartThreshold = multipartThreshold;
    }

    @Override
    public Map<String, Object> getConfiguration() {
        Map<String, Object> config = new java.util.HashMap<>();
        config.put("endpoint", endpoint);
        config.put("accessKeyId", accessKeyId);
        config.put("bucketName", bucketName);
        config.put("basePath", basePath);
        config.put("enableMultipartUpload", enableMultipartUpload);
        config.put("multipartThreshold", multipartThreshold);
        // 不暴露敏感信息
        return config;
    }

    @Override
    public boolean validateConfiguration() {
        return endpoint != null && !endpoint.trim().isEmpty() &&
               accessKeyId != null && !accessKeyId.trim().isEmpty() &&
               accessKeySecret != null && !accessKeySecret.trim().isEmpty() &&
               bucketName != null && !bucketName.trim().isEmpty();
    }

    // Getters (不包含敏感信息的getter)
    public String getEndpoint() { return endpoint; }
    public String getAccessKeyId() { return accessKeyId; }
    public String getAccessKeySecret() { return accessKeySecret; }
    public String getBucketName() { return bucketName; }
    public String getBasePath() { return basePath; }
    public boolean isEnableMultipartUpload() { return enableMultipartUpload; }
    public long getMultipartThreshold() { return multipartThreshold; }
}