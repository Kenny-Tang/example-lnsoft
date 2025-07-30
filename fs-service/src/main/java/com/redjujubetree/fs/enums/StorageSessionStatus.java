package com.redjujubetree.fs.enums;

/**
 * Storage session status enumeration
 */
public enum StorageSessionStatus {
    ACTIVE("ACTIVE", "活跃状态"),
    COMPLETING("COMPLETING", "完成中"),
    COMPLETED("COMPLETED", "已完成"),
    CANCELLED("CANCELLED", "已取消"),
    EXPIRED("EXPIRED", "已过期"),
    FAILED("FAILED", "失败");
    
    private final String code;
    private final String description;
    
    StorageSessionStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public static StorageSessionStatus fromCode(String code) {
        for (StorageSessionStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status code: " + code);
    }
}