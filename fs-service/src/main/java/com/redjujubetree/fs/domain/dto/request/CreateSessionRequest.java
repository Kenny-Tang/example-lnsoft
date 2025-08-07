package com.redjujubetree.fs.domain.dto.request;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.HashMap;
import java.util.Map;

/**
 * Create session request DTO
 */
@Data
@Validated
public class CreateSessionRequest {
    @NotBlank
    private String storageId;
    
    @NotBlank
    private String fileName;
    
    @NotNull
    @Positive
    private Long fileSize;
    
    private String expectedChecksum;
    
    private Map<String, String> customMetadata = new HashMap<>();
}

