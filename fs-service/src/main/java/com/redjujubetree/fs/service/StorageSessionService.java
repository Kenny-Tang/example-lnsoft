package com.redjujubetree.fs.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redjujubetree.fs.domain.dto.ChunkUploadResult;
import com.redjujubetree.fs.domain.dto.SessionQueryParams;
import com.redjujubetree.fs.domain.dto.SessionStatistics;
import com.redjujubetree.fs.domain.dto.StorageSessionDto;
import com.redjujubetree.fs.domain.entity.UploadedChunkEntity;
import com.redjujubetree.fs.enums.StorageSessionStatus;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Storage session service interface
 */
public interface StorageSessionService {
    
    /**
     * Create a new storage session
     */
    StorageSessionDto createSession(String storageId, String fileName, Long fileSize,
                                    String expectedChecksum, Map<String, String> customMetadata);
    
    /**
     * Get storage session by ID
     */
    Optional<StorageSessionDto> getSession(String storageId);
    
    /**
     * Update session last access time
     */
    boolean updateLastAccess(String storageId);
    
    /**
     * Update session status
     */
    boolean updateSessionStatus(String storageId, StorageSessionStatus status);
    
    /**
     * Record chunk upload
     */
    ChunkUploadResult recordChunkUpload(String storageId, Integer chunkIndex,
                                        Long chunkSize, String chunkChecksum, Long chunkOffset);
    
    /**
     * Batch record chunk uploads
     */
    List<ChunkUploadResult> batchRecordChunkUploads(List<UploadedChunkEntity> chunks);
    
    /**
     * Check if chunk exists
     */
    boolean chunkExists(String storageId, Integer chunkIndex);
    
    /**
     * Get uploaded chunks for a session
     */
    List<Integer> getUploadedChunks(String storageId);
    
    /**
     * Get upload progress
     */
    double getUploadProgress(String storageId);
    
    /**
     * Check if upload is complete
     */
    boolean isUploadComplete(String storageId);
    
    /**
     * Get missing chunks
     */
    List<Integer> getMissingChunks(String storageId);
    
    /**
     * Delete session and all related data
     */
    boolean deleteSession(String storageId);
    
    /**
     * Clean up expired sessions
     */
    int cleanupExpiredSessions(Duration timeout);
    
    /**
     * Get sessions by status
     */
    List<StorageSessionDto> getSessionsByStatus(StorageSessionStatus status);
    
    /**
     * Query sessions with pagination
     */
    IPage<StorageSessionDto> querySessions(SessionQueryParams params);
    
    /**
     * Get session statistics
     */
    SessionStatistics getSessionStatistics();
    
    /**
     * Get upload statistics for a session
     */
    Map<String, Object> getUploadStatistics(String storageId);
}