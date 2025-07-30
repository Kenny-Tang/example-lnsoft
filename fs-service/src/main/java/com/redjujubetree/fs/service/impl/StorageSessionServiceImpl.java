package com.redjujubetree.fs.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redjujubetree.fs.domain.dto.ChunkUploadResult;
import com.redjujubetree.fs.domain.dto.SessionQueryParams;
import com.redjujubetree.fs.domain.dto.SessionStatistics;
import com.redjujubetree.fs.domain.dto.StorageSessionDto;
import com.redjujubetree.fs.domain.entity.StorageSessionEntity;
import com.redjujubetree.fs.domain.entity.UploadedChunkEntity;
import com.redjujubetree.fs.enums.StorageSessionStatus;
import com.redjujubetree.fs.mapper.StorageSessionMapper;
import com.redjujubetree.fs.mapper.UploadedChunkMapper;
import com.redjujubetree.fs.service.StorageSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Storage session service implementation with MyBatis-Plus
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class StorageSessionServiceImpl implements StorageSessionService {
    
    private final StorageSessionMapper sessionMapper;
    private final UploadedChunkMapper chunkMapper;
    private final ObjectMapper objectMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public StorageSessionDto createSession(String storageId, String fileName, Long fileSize,
                                           String expectedChecksum, Map<String, String> customMetadata) {
        try {
            // Check if session already exists
            StorageSessionEntity existing = sessionMapper.selectById(storageId);
            if (existing != null) {
                log.warn("Storage session already exists: {}", storageId);
                return getSession(storageId).orElse(null);
            }
            
            // Calculate chunk size based on file size
            int chunkSize = calculateChunkSize(fileSize);
            int totalChunks = (int) Math.ceil((double) fileSize / chunkSize);
            
            // Serialize custom metadata
            String metadataJson = null;
            if (customMetadata != null && !customMetadata.isEmpty()) {
                metadataJson = objectMapper.writeValueAsString(customMetadata);
            }
            
            // Create entity
            StorageSessionEntity entity = StorageSessionEntity.builder()
                .storageId(storageId)
                .originalFileName(fileName)
                .fileSize(fileSize)
                .expectedChecksum(expectedChecksum)
                .totalChunks(totalChunks)
                .chunkSize(chunkSize)
                .createdTime(System.currentTimeMillis())
                .lastAccessTime(System.currentTimeMillis())
                .status(StorageSessionStatus.ACTIVE.getCode())
                .customMetadata(metadataJson)
                .build();
            
            // Save to database
            int inserted = sessionMapper.insert(entity);
            if (inserted > 0) {
                log.info("Created storage session: {}, chunks: {}, size: {} bytes", 
                        storageId, totalChunks, fileSize);
                return convertToDto(entity, Collections.emptyList());
            } else {
                throw new RuntimeException("Failed to insert storage session");
            }
            
        } catch (Exception e) {
            log.error("Failed to create storage session: {}", storageId, e);
            throw new RuntimeException("Failed to create storage session", e);
        }
    }
    
    @Override
    public Optional<StorageSessionDto> getSession(String storageId) {
        try {
            StorageSessionEntity entity = sessionMapper.selectById(storageId);
            if (entity == null) {
                return Optional.empty();
            }
            
            List<Integer> uploadedChunks = chunkMapper.findChunkIndexesByStorageId(storageId);
            return Optional.of(convertToDto(entity, uploadedChunks));
            
        } catch (Exception e) {
            log.error("Failed to get storage session: {}", storageId, e);
            return Optional.empty();
        }
    }
    
    @Override
    public boolean updateLastAccess(String storageId) {
        try {
            int updated = sessionMapper.updateLastAccessTime(storageId, LocalDateTime.now());
            return updated > 0;
        } catch (Exception e) {
            log.error("Failed to update last access time for session: {}", storageId, e);
            return false;
        }
    }
    
    @Override
    public boolean updateSessionStatus(String storageId, StorageSessionStatus status) {
        try {
            int updated = sessionMapper.updateStatus(storageId, status.getCode());
            if (updated > 0) {
                log.info("Updated session status: {} -> {}", storageId, status);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("Failed to update session status: {}", storageId, e);
            return false;
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ChunkUploadResult recordChunkUpload(String storageId, Integer chunkIndex,
                                               Long chunkSize, String chunkChecksum, Long chunkOffset) {
        try {
            // Check if session exists and is active
            StorageSessionEntity session = sessionMapper.selectById(storageId);
            if (session == null) {
                return ChunkUploadResult.builder()
                    .success(false)
                    .message("Storage session not found")
                    .storageId(storageId)
                    .chunkIndex(chunkIndex)
                    .errorCode("SESSION_NOT_FOUND")
                    .build();
            }
            
            if (!StorageSessionStatus.ACTIVE.getCode().equals(session.getStatus())) {
                return ChunkUploadResult.builder()
                    .success(false)
                    .message("Storage session is not active")
                    .storageId(storageId)
                    .chunkIndex(chunkIndex)
                    .errorCode("SESSION_NOT_ACTIVE")
                    .build();
            }
            
            // Check if chunk already exists
            if (chunkMapper.existsByStorageIdAndChunkIndex(storageId, chunkIndex)) {
                return ChunkUploadResult.builder()
                    .success(true)
                    .message("Chunk already exists")
                    .storageId(storageId)
                    .chunkIndex(chunkIndex)
                    .chunkSize(chunkSize)
                    .chunkChecksum(chunkChecksum)
                    .build();
            }
            
            // Record chunk upload
            UploadedChunkEntity chunkEntity = UploadedChunkEntity.builder()
                .storageId(storageId)
                .chunkIndex(chunkIndex)
                .chunkSize(chunkSize)
                .chunkChecksum(chunkChecksum)
                .chunkOffset(chunkOffset)
                .uploadedTime(System.currentTimeMillis())
                .build();
            
            int inserted = chunkMapper.insert(chunkEntity);
            if (inserted > 0) {
                // Update session last access time
                updateLastAccess(storageId);
                
                log.debug("Recorded chunk upload: session={}, chunk={}, size={}", 
                         storageId, chunkIndex, chunkSize);
                
                return ChunkUploadResult.builder()
                    .success(true)
                    .message("Chunk uploaded successfully")
                    .storageId(storageId)
                    .chunkIndex(chunkIndex)
                    .chunkSize(chunkSize)
                    .chunkChecksum(chunkChecksum)
                    .uploadTime(chunkEntity.getUploadedTime())
                    .build();
            } else {
                throw new RuntimeException("Failed to insert chunk record");
            }
                
        } catch (Exception e) {
            log.error("Failed to record chunk upload: session={}, chunk={}", storageId, chunkIndex, e);
            return ChunkUploadResult.builder()
                .success(false)
                .message("Error recording chunk upload: " + e.getMessage())
                .storageId(storageId)
                .chunkIndex(chunkIndex)
                .errorCode("INTERNAL_ERROR")
                .build();
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ChunkUploadResult> batchRecordChunkUploads(List<UploadedChunkEntity> chunks) {
        List<ChunkUploadResult> results = new ArrayList<>();
        
        if (chunks.isEmpty()) {
            return results;
        }
        
        try {
            // Group chunks by storage ID for validation
            Map<String, List<UploadedChunkEntity>> chunksBySession = chunks.stream()
                .collect(Collectors.groupingBy(UploadedChunkEntity::getStorageId));
            
            // Validate all sessions
            for (String storageId : chunksBySession.keySet()) {
                StorageSessionEntity session = sessionMapper.selectById(storageId);
                if (session == null || !StorageSessionStatus.ACTIVE.getCode().equals(session.getStatus())) {
                    // Mark all chunks for this session as failed
                    chunksBySession.get(storageId).forEach(chunk -> 
                        results.add(ChunkUploadResult.builder()
                            .success(false)
                            .message("Session not found or not active")
                            .storageId(storageId)
                            .chunkIndex(chunk.getChunkIndex())
                            .errorCode("SESSION_INVALID")
                            .build())
                    );
                    continue;
                }
                
                // Filter out existing chunks
                List<UploadedChunkEntity> newChunks = chunksBySession.get(storageId).stream()
                    .filter(chunk -> !chunkMapper.existsByStorageIdAndChunkIndex(storageId, chunk.getChunkIndex()))
                    .collect(Collectors.toList());
                
                if (!newChunks.isEmpty()) {
                    // Set upload time for new chunks
                    newChunks.forEach(chunk -> chunk.setUploadedTime(System.currentTimeMillis()));
                    
                    // Batch insert
                    int inserted = chunkMapper.batchInsertChunks(newChunks);
                    
                    // Generate results
                    for (UploadedChunkEntity chunk : newChunks) {
                        results.add(ChunkUploadResult.builder()
                            .success(true)
                            .message("Chunk uploaded successfully")
                            .storageId(chunk.getStorageId())
                            .chunkIndex(chunk.getChunkIndex())
                            .chunkSize(chunk.getChunkSize())
                            .chunkChecksum(chunk.getChunkChecksum())
                            .uploadTime(chunk.getUploadedTime())
                            .build());
                    }
                    
                    // Update session last access time
                    updateLastAccess(storageId);
                    
                    log.info("Batch uploaded {} chunks for session: {}", inserted, storageId);
                }
                
                // Handle existing chunks
                chunksBySession.get(storageId).stream()
                    .filter(chunk -> chunkMapper.existsByStorageIdAndChunkIndex(storageId, chunk.getChunkIndex()))
                    .forEach(chunk -> 
                        results.add(ChunkUploadResult.builder()
                            .success(true)
                            .message("Chunk already exists")
                            .storageId(chunk.getStorageId())
                            .chunkIndex(chunk.getChunkIndex())
                            .chunkSize(chunk.getChunkSize())
                            .chunkChecksum(chunk.getChunkChecksum())
                            .build())
                    );
            }
            
        } catch (Exception e) {
            log.error("Failed to batch record chunk uploads", e);
            // Mark remaining chunks as failed
            chunks.forEach(chunk -> {
                if (results.stream().noneMatch(r -> r.getStorageId().equals(chunk.getStorageId()) && 
                                                  r.getChunkIndex().equals(chunk.getChunkIndex()))) {
                    results.add(ChunkUploadResult.builder()
                        .success(false)
                        .message("Batch upload error: " + e.getMessage())
                        .storageId(chunk.getStorageId())
                        .chunkIndex(chunk.getChunkIndex())
                        .errorCode("BATCH_ERROR")
                        .build());
                }
            });
        }
        
        return results;
    }
    
    @Override
    public boolean chunkExists(String storageId, Integer chunkIndex) {
        return chunkMapper.existsByStorageIdAndChunkIndex(storageId, chunkIndex);
    }
    
    @Override
    public List<Integer> getUploadedChunks(String storageId) {
        return chunkMapper.findChunkIndexesByStorageId(storageId);
    }
    
    @Override
    public double getUploadProgress(String storageId) {
        Optional<StorageSessionDto> sessionOpt = getSession(storageId);
        return sessionOpt.map(StorageSessionDto::calculateUploadProgress).orElse(0.0);
    }
    
    @Override
    public boolean isUploadComplete(String storageId) {
        Optional<StorageSessionDto> sessionOpt = getSession(storageId);
        return sessionOpt.map(StorageSessionDto::isComplete).orElse(false);
    }
    
    @Override
    public List<Integer> getMissingChunks(String storageId) {
        Optional<StorageSessionDto> sessionOpt = getSession(storageId);
        return sessionOpt.map(StorageSessionDto::getMissingChunks).orElse(Collections.emptyList());
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteSession(String storageId) {
        try {
            // Delete chunks first (foreign key constraint)
            int deletedChunks = chunkMapper.deleteByStorageId(storageId);
            
            // Delete session
            int deletedSessions = sessionMapper.deleteById(storageId);
            
            log.info("Deleted storage session: {}, chunks deleted: {}", storageId, deletedChunks);
            return deletedSessions > 0;
            
        } catch (Exception e) {
            log.error("Failed to delete storage session: {}", storageId, e);
            return false;
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int cleanupExpiredSessions(Duration timeout) {
        try {
            LocalDateTime cutoffTime = LocalDateTime.now().minus(timeout);
            List<StorageSessionEntity> expiredSessions = sessionMapper
                .findByStatusAndLastAccessTimeBefore(StorageSessionStatus.ACTIVE.getCode(), cutoffTime);
            
            int cleanedCount = 0;
            for (StorageSessionEntity session : expiredSessions) {
                try {
                    // Update status to expired
                    updateSessionStatus(session.getStorageId(), StorageSessionStatus.EXPIRED);
                    
                    // Optionally delete the session completely
                    // deleteSession(session.getStorageId());
                    
                    cleanedCount++;
                    log.info("Marked expired session: {}", session.getStorageId());
                    
                } catch (Exception e) {
                    log.error("Failed to cleanup expired session: {}", session.getStorageId(), e);
                }
            }
            
            log.info("Cleaned up {} expired sessions", cleanedCount);
            return cleanedCount;
            
        } catch (Exception e) {
            log.error("Failed to cleanup expired sessions", e);
            return 0;
        }
    }
    
    @Override
    public List<StorageSessionDto> getSessionsByStatus(StorageSessionStatus status) {
        try {
            List<StorageSessionEntity> entities = sessionMapper.findByStatus(status.getCode());
            return entities.stream()
                .map(entity -> {
                    List<Integer> uploadedChunks = chunkMapper.findChunkIndexesByStorageId(entity.getStorageId());
                    return convertToDto(entity, uploadedChunks);
                })
                .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to get sessions by status: {}", status, e);
            return Collections.emptyList();
        }
    }
    
    @Override
    public IPage<StorageSessionDto> querySessions(SessionQueryParams params) {
        try {
            // Build query wrapper
            LambdaQueryWrapper<StorageSessionEntity> queryWrapper = new LambdaQueryWrapper<>();
            
            if (params.getStatus() != null) {
                queryWrapper.eq(StorageSessionEntity::getStatus, params.getStatus().getCode());
            }
            
            if (params.getCreatedAfter() != null) {
                queryWrapper.ge(StorageSessionEntity::getCreatedTime, params.getCreatedAfter());
            }
            
            if (params.getCreatedBefore() != null) {
                queryWrapper.le(StorageSessionEntity::getCreatedTime, params.getCreatedBefore());
            }
            
            if (params.getLastAccessAfter() != null) {
                queryWrapper.ge(StorageSessionEntity::getLastAccessTime, params.getLastAccessAfter());
            }
            
            if (params.getLastAccessBefore() != null) {
                queryWrapper.le(StorageSessionEntity::getLastAccessTime, params.getLastAccessBefore());
            }
            
            if (StringUtils.hasText(params.getOriginalFileName())) {
                queryWrapper.like(StorageSessionEntity::getOriginalFileName, params.getOriginalFileName());
            }
            
            if (params.getMinFileSize() != null) {
                queryWrapper.ge(StorageSessionEntity::getFileSize, params.getMinFileSize());
            }
            
            if (params.getMaxFileSize() != null) {
                queryWrapper.le(StorageSessionEntity::getFileSize, params.getMaxFileSize());
            }
            
            // Order by
            if ("created_time".equals(params.getOrderBy())) {
                if ("ASC".equalsIgnoreCase(params.getOrderDirection())) {
                    queryWrapper.orderByAsc(StorageSessionEntity::getCreatedTime);
                } else {
                    queryWrapper.orderByDesc(StorageSessionEntity::getCreatedTime);
                }
            } else if ("last_access_time".equals(params.getOrderBy())) {
                if ("ASC".equalsIgnoreCase(params.getOrderDirection())) {
                    queryWrapper.orderByAsc(StorageSessionEntity::getLastAccessTime);
                } else {
                    queryWrapper.orderByDesc(StorageSessionEntity::getLastAccessTime);
                }
            } else if ("file_size".equals(params.getOrderBy())) {
                if ("ASC".equalsIgnoreCase(params.getOrderDirection())) {
                    queryWrapper.orderByAsc(StorageSessionEntity::getFileSize);
                } else {
                    queryWrapper.orderByDesc(StorageSessionEntity::getFileSize);
                }
            }
            
            // Pagination
            Page<StorageSessionEntity> page = new Page<>(params.getPageNum(), params.getPageSize());
            IPage<StorageSessionEntity> entityPage = sessionMapper.selectPage(page, queryWrapper);
            
            // Convert to DTOs
            List<StorageSessionDto> dtos = entityPage.getRecords().stream()
                .map(entity -> {
                    List<Integer> uploadedChunks = chunkMapper.findChunkIndexesByStorageId(entity.getStorageId());
                    return convertToDto(entity, uploadedChunks);
                })
                .collect(Collectors.toList());
            
            // Create result page
            IPage<StorageSessionDto> resultPage = new Page<>(params.getPageNum(), params.getPageSize());
            resultPage.setRecords(dtos);
            resultPage.setTotal(entityPage.getTotal());
            resultPage.setPages(entityPage.getPages());
            
            return resultPage;
            
        } catch (Exception e) {
            log.error("Failed to query sessions", e);
            return new Page<>();
        }
    }
    
    @Override
    public SessionStatistics getSessionStatistics() {
        try {
            long activeSessions = sessionMapper.countByStatus(StorageSessionStatus.ACTIVE.getCode());
            long completedSessions = sessionMapper.countByStatus(StorageSessionStatus.COMPLETED.getCode());
            long failedSessions = sessionMapper.countByStatus(StorageSessionStatus.FAILED.getCode());
            long expiredSessions = sessionMapper.countByStatus(StorageSessionStatus.EXPIRED.getCode());
            long totalSessions = sessionMapper.selectCount(null);
            
            // Calculate average progress for active sessions
            List<StorageSessionDto> activeDtos = getSessionsByStatus(StorageSessionStatus.ACTIVE);
            double avgProgress = activeDtos.stream()
                .mapToDouble(StorageSessionDto::calculateUploadProgress)
                .average()
                .orElse(0.0);
            
            // Get oldest active session
            LambdaQueryWrapper<StorageSessionEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(StorageSessionEntity::getStatus, StorageSessionStatus.ACTIVE.getCode())
                   .orderByAsc(StorageSessionEntity::getCreatedTime)
                   .last("LIMIT 1");
            
            StorageSessionEntity oldestActive = sessionMapper.selectOne(wrapper);
            Long oldestActiveTime = oldestActive != null ? oldestActive.getCreatedTime() : null;
            
            return SessionStatistics.builder()
                .activeSessions(activeSessions)
                .completedSessions(completedSessions)
                .failedSessions(failedSessions)
                .expiredSessions(expiredSessions)
                .totalSessions(totalSessions)
                .averageUploadProgress(avgProgress)
                .oldestActiveSession(oldestActiveTime)
                .build();
                
        } catch (Exception e) {
            log.error("Failed to get session statistics", e);
            return SessionStatistics.builder().build();
        }
    }
    
    @Override
    public Map<String, Object> getUploadStatistics(String storageId) {
        try {
            return chunkMapper.getUploadStatistics(storageId);
        } catch (Exception e) {
            log.error("Failed to get upload statistics for session: {}", storageId, e);
            return Collections.emptyMap();
        }
    }
    
    // ==================== Private Helper Methods ====================
    
    private StorageSessionDto convertToDto(StorageSessionEntity entity, List<Integer> uploadedChunks) {
        Map<String, String> customMetadata = null;
        if (entity.getCustomMetadata() != null) {
            try {
                customMetadata = objectMapper.readValue(entity.getCustomMetadata(), 
                    new TypeReference<>() {});
            } catch (Exception e) {
                log.warn("Failed to deserialize custom metadata for session: {}", entity.getStorageId(), e);
                customMetadata = new HashMap<>();
            }
        }
        StorageSessionDto dto = StorageSessionDto.builder()
            .storageId(entity.getStorageId())
            .originalFileName(entity.getOriginalFileName())
            .fileSize(entity.getFileSize())
            .expectedChecksum(entity.getExpectedChecksum())
            .totalChunks(entity.getTotalChunks())
            .chunkSize(entity.getChunkSize())
            .createdTime(LocalDateTimeUtil.of(entity.getCreatedTime()))
            .lastAccessTime(LocalDateTimeUtil.of(entity.getLastAccessTime()))
            .status(StorageSessionStatus.fromCode(entity.getStatus()))
            .tempFilePath(entity.getTempFilePath())
            .chunkDirPath(entity.getChunkDirPath())
            .metadataFilePath(entity.getMetadataFilePath())
            .customMetadata(customMetadata)
            .uploadedChunks(uploadedChunks)
            .build();
            
        // Calculate upload progress
        dto.setUploadProgress(dto.calculateUploadProgress());
        
        return dto;
    }
    
    private int calculateChunkSize(long fileSize) {
        // Dynamic chunk size calculation based on file size
        if (fileSize <= 10 * 1024 * 1024) { // 10MB
            return 256 * 1024; // 256KB
        } else if (fileSize <= 100 * 1024 * 1024) { // 100MB
            return 1024 * 1024; // 1MB
        } else if (fileSize <= 1024L * 1024 * 1024) { // 1GB
            return 2 * 1024 * 1024; // 2MB
        } else {
            return 4 * 1024 * 1024; // 4MB
        }
    }
}