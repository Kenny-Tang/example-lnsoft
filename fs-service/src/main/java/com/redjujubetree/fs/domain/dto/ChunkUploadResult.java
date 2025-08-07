package com.redjujubetree.fs.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Chunk upload result DTO
 */
@Data
@Builder
@AllArgsConstructor
public class ChunkUploadResult {
    private boolean success;
    private String message;
    private String storageId;
    private Integer chunkIndex;
    private Long chunkSize;
    private String chunkChecksum;
    private Long uploadTime;
    private String errorCode;
}