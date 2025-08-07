package com.redjujubetree.fs.domain.dto;

import com.redjujubetree.fs.enums.StorageSessionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Storage session DTO for service layer
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageSessionDto {
    private String storageId;
    private String originalFileName;
    private Long fileSize;
    private String expectedChecksum;
    private Integer totalChunks;
    private Integer chunkSize;
    private LocalDateTime createdTime;
    private LocalDateTime lastAccessTime;
    private StorageSessionStatus status;
    private String tempFilePath;
    private String chunkDirPath;
    private String metadataFilePath;
    private Map<String, String> customMetadata;
    private List<Integer> uploadedChunks;
    private Double uploadProgress;
    private Long uploadedSize;
    
    public boolean isExpired(Duration timeout) {
        return lastAccessTime != null && 
               LocalDateTime.now().minus(timeout).isAfter(lastAccessTime);
    }
    
    public double calculateUploadProgress() {
        if (totalChunks == null || totalChunks == 0) {
            return 0.0;
        }
        int uploadedCount = uploadedChunks != null ? uploadedChunks.size() : 0;
        return (double) uploadedCount / totalChunks * 100.0;
    }
    
    public boolean isComplete() {
        return uploadedChunks != null && 
               totalChunks != null && 
               uploadedChunks.size() == totalChunks;
    }
    
    public List<Integer> getMissingChunks() {
        if (totalChunks == null || uploadedChunks == null) {
            return Collections.emptyList();
        }
        
        List<Integer> missing = new ArrayList<>();
        Set<Integer> uploaded = new HashSet<>(uploadedChunks);
        
        for (int i = 0; i < totalChunks; i++) {
            if (!uploaded.contains(i)) {
                missing.add(i);
            }
        }
        return missing;
    }
}