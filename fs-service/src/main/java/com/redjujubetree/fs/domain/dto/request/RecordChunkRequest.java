package com.redjujubetree.fs.domain.dto.request;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * Record chunk request DTO
 */
@Data
@Validated
public class RecordChunkRequest {
    @NotNull
    @Min(0)
    private Integer chunkIndex;
    
    @NotNull
    @Positive
    private Long chunkSize;
    
    private String chunkChecksum;
    
    @NotNull
    @Min(0)
    private Long chunkOffset;
}