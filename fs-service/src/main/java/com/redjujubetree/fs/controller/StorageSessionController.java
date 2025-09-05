package com.redjujubetree.fs.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redjujubetree.fs.domain.dto.ChunkUploadResult;
import com.redjujubetree.fs.domain.dto.SessionQueryParams;
import com.redjujubetree.fs.domain.dto.SessionStatistics;
import com.redjujubetree.fs.domain.dto.StorageSessionDto;
import com.redjujubetree.fs.domain.dto.request.CreateSessionRequest;
import com.redjujubetree.fs.domain.dto.request.RecordChunkRequest;
import com.redjujubetree.fs.domain.dto.response.ApiResponse;
import com.redjujubetree.fs.service.StorageSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for storage session management
 */
@RestController
@RequestMapping("/api/storage/sessions")
@Slf4j
@RequiredArgsConstructor
@Validated
public class StorageSessionController {
    
    private final StorageSessionService sessionService;
    
    @PostMapping
    public ResponseEntity<ApiResponse<StorageSessionDto>> createSession(
            @RequestBody @Valid CreateSessionRequest request) {
        try {
            StorageSessionDto session = sessionService.createSession(
                request.getStorageId(),
                request.getFileName(),
                request.getFileSize(),
                request.getExpectedChecksum(),
                request.getCustomMetadata()
            );
            
            return ResponseEntity.ok(ApiResponse.success(session));
            
        } catch (Exception e) {
            log.error("Failed to create session", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to create session: " + e.getMessage()));
        }
    }
    
    @GetMapping("/{storageId}")
    public ResponseEntity<ApiResponse<StorageSessionDto>> getSession(
            @PathVariable String storageId) {
        Optional<StorageSessionDto> session = sessionService.getSession(storageId);
        
        if (session.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success(session.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Session not found"));
        }
    }
    
    @PostMapping("/{storageId}/chunks")
    public ResponseEntity<ApiResponse<ChunkUploadResult>> recordChunk(
            @PathVariable String storageId,
            @RequestBody @Valid RecordChunkRequest request) {
        
        ChunkUploadResult result = sessionService.recordChunkUpload(
            storageId,
            request.getChunkIndex(),
            request.getChunkSize(),
            request.getChunkChecksum(),
            request.getChunkOffset()
        );
        
        return ResponseEntity.ok(ApiResponse.success(result));
    }
    
    @GetMapping("/{storageId}/progress")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getProgress(
            @PathVariable String storageId) {
        
        double progress = sessionService.getUploadProgress(storageId);
        List<Integer> uploadedChunks = sessionService.getUploadedChunks(storageId);
        List<Integer> missingChunks = sessionService.getMissingChunks(storageId);
        boolean isComplete = sessionService.isUploadComplete(storageId);

        Map<String, Object> progressInfo = new HashMap<String, Object>() {{
            put("progress", progress);
            put("uploadedChunks", uploadedChunks);
            put("missingChunks", missingChunks);
            put("isComplete", isComplete);
        }};

        
        return ResponseEntity.ok(ApiResponse.success(progressInfo));
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<IPage<StorageSessionDto>>> querySessions(
            @ModelAttribute SessionQueryParams params) {
        
        IPage<StorageSessionDto> sessions = sessionService.querySessions(params);
        return ResponseEntity.ok(ApiResponse.success(sessions));
    }
    
    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<SessionStatistics>> getStatistics() {
        SessionStatistics stats = sessionService.getSessionStatistics();
        return ResponseEntity.ok(ApiResponse.success(stats));
    }
    
    @DeleteMapping("/{storageId}")
    public ResponseEntity<ApiResponse<Void>> deleteSession(@PathVariable String storageId) {
        boolean deleted = sessionService.deleteSession(storageId);
        
        if (deleted) {
            return ResponseEntity.ok(ApiResponse.success(null));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Session not found or could not be deleted"));
        }
    }
    
    @PostMapping("/cleanup")
    public ResponseEntity<ApiResponse<Integer>> cleanupExpiredSessions(
            @RequestParam(defaultValue = "PT6H") String timeout) {
        
        try {
            Duration timeoutDuration = Duration.parse(timeout);
            int cleanedCount = sessionService.cleanupExpiredSessions(timeoutDuration);
            return ResponseEntity.ok(ApiResponse.success(cleanedCount));
            
        } catch (Exception e) {
            log.error("Failed to cleanup sessions", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Cleanup failed: " + e.getMessage()));
        }
    }
}