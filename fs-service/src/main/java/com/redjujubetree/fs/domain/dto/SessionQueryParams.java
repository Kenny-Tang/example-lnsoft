package com.redjujubetree.fs.domain.dto;

import com.redjujubetree.fs.enums.StorageSessionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Session query parameters
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionQueryParams {
    private StorageSessionStatus status;
    private LocalDateTime createdAfter;
    private LocalDateTime createdBefore;
    private LocalDateTime lastAccessAfter;
    private LocalDateTime lastAccessBefore;
    private String originalFileName;
    private Long minFileSize;
    private Long maxFileSize;
    private Integer pageNum = 1;
    private Integer pageSize = 20;
    private String orderBy = "created_time";
    private String orderDirection = "DESC";
}