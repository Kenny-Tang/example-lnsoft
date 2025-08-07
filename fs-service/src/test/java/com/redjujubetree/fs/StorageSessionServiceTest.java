package com.redjujubetree.fs;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redjujubetree.fs.domain.dto.ChunkUploadResult;
import com.redjujubetree.fs.domain.dto.SessionStatistics;
import com.redjujubetree.fs.domain.dto.StorageSessionDto;
import com.redjujubetree.fs.domain.entity.StorageSessionEntity;
import com.redjujubetree.fs.domain.entity.UploadedChunkEntity;
import com.redjujubetree.fs.enums.StorageSessionStatus;
import com.redjujubetree.fs.mapper.StorageSessionMapper;
import com.redjujubetree.fs.mapper.UploadedChunkMapper;
import com.redjujubetree.fs.service.impl.StorageSessionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for StorageSessionService
 */
@SpringBootTest
class StorageSessionServiceTest {
    
    @Mock
    private StorageSessionMapper sessionMapper;
    
    @Mock
    private UploadedChunkMapper chunkMapper;
    
    @Mock
    private ObjectMapper objectMapper;
    
    @Resource
    private StorageSessionServiceImpl storageSessionService;
    
    private StorageSessionEntity testSessionEntity;
    private StorageSessionDto testSessionDto;
    private UploadedChunkEntity testChunkEntity;
    
    @BeforeEach
    void setUp() {
        // Set default chunk size for testing
        ReflectionTestUtils.setField(storageSessionService, "defaultChunkSize", 1048576);
        
        // Create test session entity
        testSessionEntity = StorageSessionEntity.builder()
            .storageId("test-session-001")
            .originalFileName("test-file.pdf")
            .fileSize(10485760L)
            .expectedChecksum("d41d8cd98f00b204e9800998ecf8427e")
            .totalChunks(10)
            .chunkSize(1048576)
            .createdTime(System.currentTimeMillis())
            .lastAccessTime(System.currentTimeMillis())
            .status(StorageSessionStatus.ACTIVE.getCode())
            .customMetadata("{\"key\":\"value\"}")
            .build();
        
        // Create test session DTO
        testSessionDto = StorageSessionDto.builder()
            .storageId("test-session-001")
            .originalFileName("test-file.pdf")
            .fileSize(10485760L)
            .expectedChecksum("d41d8cd98f00b204e9800998ecf8427e")
            .totalChunks(10)
            .chunkSize(1048576)
            .createdTime(LocalDateTime.now())
            .lastAccessTime(LocalDateTime.now())
            .status(StorageSessionStatus.ACTIVE)
            .customMetadata(Map.of("key", "value"))
            .uploadedChunks(Arrays.asList(0, 1, 2))
            .build();
        
        // Create test chunk entity
        testChunkEntity = UploadedChunkEntity.builder()
            .id(1L)
            .storageId("test-session-001")
            .chunkIndex(0)
            .chunkSize(1048576L)
            .chunkChecksum("chunk-checksum")
            .chunkOffset(0L)
            .uploadedTime(System.currentTimeMillis())
            .build();
    }
    
    @Test
    void testCreateSession_Success() throws Exception {

        Map<String, String> customMetadata = Map.of("key", "value");
        
        // When
        StorageSessionDto result = storageSessionService.createSession(
            "test-session-001", "test-file.pdf", 10485760L, 
            "d41d8cd98f00b204e9800998ecf8427e", customMetadata);
        
        // Then
        assertNotNull(result);
        assertEquals("test-session-001", result.getStorageId());
        assertEquals("test-file.pdf", result.getOriginalFileName());
        assertEquals(10485760L, result.getFileSize());
        assertEquals(10, result.getTotalChunks());
        
        verify(sessionMapper).selectById("test-session-001");
        verify(sessionMapper).insert(any(StorageSessionEntity.class));
    }
    
    @Test
    void testCreateSession_AlreadyExists() {
        // Given
        when(sessionMapper.selectById("test-session-001")).thenReturn(testSessionEntity);
        when(chunkMapper.findChunkIndexesByStorageId("test-session-001")).thenReturn(Arrays.asList(0, 1, 2));
        
        // When
        StorageSessionDto result = storageSessionService.createSession(
            "test-session-001", "test-file.pdf", 10485760L, 
            "d41d8cd98f00b204e9800998ecf8427e", Map.of());
        
        // Then
        assertNotNull(result);
        assertEquals("test-session-001", result.getStorageId());
        
        verify(sessionMapper).selectById("test-session-001");
        verify(sessionMapper, never()).insert(any());
    }
    
    @Test
    void testGetSession_Success() {
        // Given
        when(sessionMapper.selectById("test-session-001")).thenReturn(testSessionEntity);
        when(chunkMapper.findChunkIndexesByStorageId("test-session-001")).thenReturn(Arrays.asList(0, 1, 2));
        
        // When
        Optional<StorageSessionDto> result = storageSessionService.getSession("test-session-001");
        
        // Then
        assertTrue(result.isPresent());
        assertEquals("test-session-001", result.get().getStorageId());
        assertEquals(3, result.get().getUploadedChunks().size());
        
        verify(sessionMapper).selectById("test-session-001");
        verify(chunkMapper).findChunkIndexesByStorageId("test-session-001");
    }
    
    @Test
    void testGetSession_NotFound() {
        // Given
        when(sessionMapper.selectById("non-existent")).thenReturn(null);
        
        // When
        Optional<StorageSessionDto> result = storageSessionService.getSession("non-existent");
        
        // Then
        assertFalse(result.isPresent());
        
        verify(sessionMapper).selectById("non-existent");
        verify(chunkMapper, never()).findChunkIndexesByStorageId(any());
    }
    
    @Test
    void testRecordChunkUpload_Success() {
        // Given
        when(sessionMapper.selectById("test-session-001")).thenReturn(testSessionEntity);
        when(chunkMapper.existsByStorageIdAndChunkIndex("test-session-001", 0)).thenReturn(false);
        when(chunkMapper.insert(any(UploadedChunkEntity.class))).thenReturn(1);
        when(sessionMapper.updateLastAccessTime(eq("test-session-001"), any(LocalDateTime.class))).thenReturn(1);
        
        // When
        ChunkUploadResult result = storageSessionService.recordChunkUpload(
            "test-session-001", 0, 1048576L, "chunk-checksum", 0L);
        
        // Then
        assertTrue(result.isSuccess());
        assertEquals("Chunk uploaded successfully", result.getMessage());
        assertEquals("test-session-001", result.getStorageId());
        assertEquals(0, result.getChunkIndex());
        
        verify(sessionMapper).selectById("test-session-001");
        verify(chunkMapper).existsByStorageIdAndChunkIndex("test-session-001", 0);
        verify(chunkMapper).insert(any(UploadedChunkEntity.class));
    }
    
    @Test
    void testRecordChunkUpload_SessionNotFound() {
        // Given
        when(sessionMapper.selectById("non-existent")).thenReturn(null);
        
        // When
        ChunkUploadResult result = storageSessionService.recordChunkUpload(
            "non-existent", 0, 1048576L, "chunk-checksum", 0L);
        
        // Then
        assertFalse(result.isSuccess());
        assertEquals("Storage session not found", result.getMessage());
        assertEquals("SESSION_NOT_FOUND", result.getErrorCode());
        
        verify(sessionMapper).selectById("non-existent");
        verify(chunkMapper, never()).insert(any());
    }
    
    @Test
    void testRecordChunkUpload_ChunkAlreadyExists() {
        // Given
        when(sessionMapper.selectById("test-session-001")).thenReturn(testSessionEntity);
        when(chunkMapper.existsByStorageIdAndChunkIndex("test-session-001", 0)).thenReturn(true);
        
        // When
        ChunkUploadResult result = storageSessionService.recordChunkUpload(
            "test-session-001", 0, 1048576L, "chunk-checksum", 0L);
        
        // Then
        assertTrue(result.isSuccess());
        assertEquals("Chunk already exists", result.getMessage());
        
        verify(sessionMapper).selectById("test-session-001");
        verify(chunkMapper).existsByStorageIdAndChunkIndex("test-session-001", 0);
        verify(chunkMapper, never()).insert(any());
    }
    
    @Test
    void testGetUploadProgress() {
        // Given
        when(sessionMapper.selectById("test-session-001")).thenReturn(testSessionEntity);
        when(chunkMapper.findChunkIndexesByStorageId("test-session-001")).thenReturn(Arrays.asList(0, 1, 2, 3, 4));
        
        // When
        double progress = storageSessionService.getUploadProgress("test-session-001");
        
        // Then
        assertEquals(50.0, progress, 0.01); // 5 out of 10 chunks = 50%
    }
    
    @Test
    void testIsUploadComplete_True() {
        // Given
        when(sessionMapper.selectById("test-session-001")).thenReturn(testSessionEntity);
        when(chunkMapper.findChunkIndexesByStorageId("test-session-001"))
            .thenReturn(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
        
        // When
        boolean isComplete = storageSessionService.isUploadComplete("test-session-001");
        
        // Then
        assertTrue(isComplete);
    }
    
    @Test
    void testIsUploadComplete_False() {
        // Given
        when(sessionMapper.selectById("test-session-001")).thenReturn(testSessionEntity);
        when(chunkMapper.findChunkIndexesByStorageId("test-session-001")).thenReturn(Arrays.asList(0, 1, 2));
        
        // When
        boolean isComplete = storageSessionService.isUploadComplete("test-session-001");
        
        // Then
        assertFalse(isComplete);
    }
    
    @Test
    void testGetMissingChunks() {
        // Given
        when(sessionMapper.selectById("test-session-001")).thenReturn(testSessionEntity);
        when(chunkMapper.findChunkIndexesByStorageId("test-session-001")).thenReturn(Arrays.asList(0, 2, 4, 6, 8));
        
        // When
        List<Integer> missingChunks = storageSessionService.getMissingChunks("test-session-001");
        
        // Then
        assertEquals(5, missingChunks.size());
        assertTrue(missingChunks.contains(1));
        assertTrue(missingChunks.contains(3));
        assertTrue(missingChunks.contains(5));
        assertTrue(missingChunks.contains(7));
        assertTrue(missingChunks.contains(9));
    }
    
    @Test
    void testUpdateSessionStatus_Success() {
        // Given
        when(sessionMapper.updateStatus("test-session-001", StorageSessionStatus.COMPLETED.getCode()))
            .thenReturn(1);
        
        // When
        boolean result = storageSessionService.updateSessionStatus("test-session-001", StorageSessionStatus.COMPLETED);
        
        // Then
        assertTrue(result);
        verify(sessionMapper).updateStatus("test-session-001", StorageSessionStatus.COMPLETED.getCode());
    }
    
    @Test
    void testUpdateSessionStatus_Failed() {
        // Given
        when(sessionMapper.updateStatus("non-existent", StorageSessionStatus.COMPLETED.getCode()))
            .thenReturn(0);
        
        // When
        boolean result = storageSessionService.updateSessionStatus("non-existent", StorageSessionStatus.COMPLETED);
        
        // Then
        assertFalse(result);
        verify(sessionMapper).updateStatus("non-existent", StorageSessionStatus.COMPLETED.getCode());
    }
    
    @Test
    void testUpdateLastAccess_Success() {
        // Given
        when(sessionMapper.updateLastAccessTime(eq("test-session-001"), any(LocalDateTime.class)))
            .thenReturn(1);
        
        // When
        boolean result = storageSessionService.updateLastAccess("test-session-001");
        
        // Then
        assertTrue(result);
        verify(sessionMapper).updateLastAccessTime(eq("test-session-001"), any(LocalDateTime.class));
    }
    
    @Test
    void testChunkExists_True() {
        // Given
        when(chunkMapper.existsByStorageIdAndChunkIndex("test-session-001", 0)).thenReturn(true);
        
        // When
        boolean exists = storageSessionService.chunkExists("test-session-001", 0);
        
        // Then
        assertTrue(exists);
        verify(chunkMapper).existsByStorageIdAndChunkIndex("test-session-001", 0);
    }
    
    @Test
    void testChunkExists_False() {
        // Given
        when(chunkMapper.existsByStorageIdAndChunkIndex("test-session-001", 5)).thenReturn(false);
        
        // When
        boolean exists = storageSessionService.chunkExists("test-session-001", 5);
        
        // Then
        assertFalse(exists);
        verify(chunkMapper).existsByStorageIdAndChunkIndex("test-session-001", 5);
    }
    
    @Test
    void testGetUploadedChunks() {
        // Given
        List<Integer> expectedChunks = Arrays.asList(0, 1, 2, 3, 4);
        when(chunkMapper.findChunkIndexesByStorageId("test-session-001")).thenReturn(expectedChunks);
        
        // When
        List<Integer> uploadedChunks = storageSessionService.getUploadedChunks("test-session-001");
        
        // Then
        assertEquals(expectedChunks, uploadedChunks);
        verify(chunkMapper).findChunkIndexesByStorageId("test-session-001");
    }
    
    @Test
    void testDeleteSession_Success() {
        // Given
        when(chunkMapper.deleteByStorageId("test-session-001")).thenReturn(5);
        when(sessionMapper.deleteById("test-session-001")).thenReturn(1);
        
        // When
        boolean result = storageSessionService.deleteSession("test-session-001");
        
        // Then
        assertTrue(result);
        verify(chunkMapper).deleteByStorageId("test-session-001");
        verify(sessionMapper).deleteById("test-session-001");
    }
    
    @Test
    void testDeleteSession_Failed() {
        // Given
        when(chunkMapper.deleteByStorageId("non-existent")).thenReturn(0);
        when(sessionMapper.deleteById("non-existent")).thenReturn(0);
        
        // When
        boolean result = storageSessionService.deleteSession("non-existent");
        
        // Then
        assertFalse(result);
        verify(chunkMapper).deleteByStorageId("non-existent");
        verify(sessionMapper).deleteById("non-existent");
    }
    
    @Test
    void testBatchRecordChunkUploads_Success() {
        // Given
        List<UploadedChunkEntity> chunks = Arrays.asList(
            UploadedChunkEntity.builder()
                .storageId("test-session-001")
                .chunkIndex(0)
                .chunkSize(1048576L)
                .chunkChecksum("checksum1")
                .chunkOffset(0L)
                .build(),
            UploadedChunkEntity.builder()
                .storageId("test-session-001")
                .chunkIndex(1)
                .chunkSize(1048576L)
                .chunkChecksum("checksum2")
                .chunkOffset(1048576L)
                .build()
        );
        
        when(sessionMapper.selectById("test-session-001")).thenReturn(testSessionEntity);
        when(chunkMapper.existsByStorageIdAndChunkIndex("test-session-001", 0)).thenReturn(false);
        when(chunkMapper.existsByStorageIdAndChunkIndex("test-session-001", 1)).thenReturn(false);
        when(chunkMapper.batchInsertChunks(anyList())).thenReturn(2);
        when(sessionMapper.updateLastAccessTime(eq("test-session-001"), any(LocalDateTime.class))).thenReturn(1);
        
        // When
        List<ChunkUploadResult> results = storageSessionService.batchRecordChunkUploads(chunks);
        
        // Then
        assertEquals(2, results.size());
        assertTrue(results.get(0).isSuccess());
        assertTrue(results.get(1).isSuccess());
        assertEquals("Chunk uploaded successfully", results.get(0).getMessage());
        assertEquals("Chunk uploaded successfully", results.get(1).getMessage());
        
        verify(sessionMapper).selectById("test-session-001");
        verify(chunkMapper).batchInsertChunks(anyList());
    }
    
    @Test
    void testBatchRecordChunkUploads_SessionNotFound() {
        // Given
        List<UploadedChunkEntity> chunks = Arrays.asList(
            UploadedChunkEntity.builder()
                .storageId("non-existent")
                .chunkIndex(0)
                .chunkSize(1048576L)
                .build()
        );
        
        when(sessionMapper.selectById("non-existent")).thenReturn(null);
        
        // When
        List<ChunkUploadResult> results = storageSessionService.batchRecordChunkUploads(chunks);
        
        // Then
        assertEquals(1, results.size());
        assertFalse(results.get(0).isSuccess());
        assertEquals("Session not found or not active", results.get(0).getMessage());
        assertEquals("SESSION_INVALID", results.get(0).getErrorCode());
        
        verify(sessionMapper).selectById("non-existent");
        verify(chunkMapper, never()).batchInsertChunks(anyList());
    }
    
    @Test
    void testBatchRecordChunkUploads_ExistingChunks() {
        // Given
        List<UploadedChunkEntity> chunks = Arrays.asList(
            UploadedChunkEntity.builder()
                .storageId("test-session-001")
                .chunkIndex(0)
                .chunkSize(1048576L)
                .chunkChecksum("checksum1")
                .chunkOffset(0L)
                .build()
        );
        
        when(sessionMapper.selectById("test-session-001")).thenReturn(testSessionEntity);
        when(chunkMapper.existsByStorageIdAndChunkIndex("test-session-001", 0)).thenReturn(true);
        
        // When
        List<ChunkUploadResult> results = storageSessionService.batchRecordChunkUploads(chunks);
        
        // Then
        assertEquals(1, results.size());
        assertTrue(results.get(0).isSuccess());
        assertEquals("Chunk already exists", results.get(0).getMessage());
        
        verify(sessionMapper).selectById("test-session-001");
        verify(chunkMapper, never()).batchInsertChunks(anyList());
    }
    
    @Test
    void testGetSessionsByStatus() {
        // Given
        List<StorageSessionEntity> entities = Arrays.asList(testSessionEntity);
        when(sessionMapper.findByStatus(StorageSessionStatus.ACTIVE.getCode())).thenReturn(entities);
        when(chunkMapper.findChunkIndexesByStorageId("test-session-001")).thenReturn(Arrays.asList(0, 1, 2));
        
        // When
        List<StorageSessionDto> sessions = storageSessionService.getSessionsByStatus(StorageSessionStatus.ACTIVE);
        
        // Then
        assertEquals(1, sessions.size());
        assertEquals("test-session-001", sessions.get(0).getStorageId());
        assertEquals(3, sessions.get(0).getUploadedChunks().size());
        
        verify(sessionMapper).findByStatus(StorageSessionStatus.ACTIVE.getCode());
        verify(chunkMapper).findChunkIndexesByStorageId("test-session-001");
    }
    
    @Test
    void testCleanupExpiredSessions() {
        // Given
        LocalDateTime cutoffTime = LocalDateTime.now().minusHours(6);
        StorageSessionEntity expiredSession = StorageSessionEntity.builder()
            .storageId("expired-session")
            .status(StorageSessionStatus.ACTIVE.getCode())
            .lastAccessTime(LocalDateTimeUtil.toEpochMilli(cutoffTime.minusHours(1)))
            .build();
        
        when(sessionMapper.findByStatusAndLastAccessTimeBefore(
            eq(StorageSessionStatus.ACTIVE.getCode()), any(LocalDateTime.class)))
            .thenReturn(Arrays.asList(expiredSession));
        when(sessionMapper.updateStatus("expired-session", StorageSessionStatus.EXPIRED.getCode()))
            .thenReturn(1);
        
        // When
        int cleanedCount = storageSessionService.cleanupExpiredSessions(java.time.Duration.ofHours(6));
        
        // Then
        assertEquals(1, cleanedCount);
        verify(sessionMapper).findByStatusAndLastAccessTimeBefore(
            eq(StorageSessionStatus.ACTIVE.getCode()), any(LocalDateTime.class));
        verify(sessionMapper).updateStatus("expired-session", StorageSessionStatus.EXPIRED.getCode());
    }
    
    @Test
    void testGetSessionStatistics() {
        // Given
        when(sessionMapper.countByStatus(StorageSessionStatus.ACTIVE.getCode())).thenReturn(5L);
        when(sessionMapper.countByStatus(StorageSessionStatus.COMPLETED.getCode())).thenReturn(10L);
        when(sessionMapper.countByStatus(StorageSessionStatus.FAILED.getCode())).thenReturn(2L);
        when(sessionMapper.countByStatus(StorageSessionStatus.EXPIRED.getCode())).thenReturn(3L);
        when(sessionMapper.selectCount(null)).thenReturn(20L);
        
        // Mock active sessions for progress calculation
        when(sessionMapper.findByStatus(StorageSessionStatus.ACTIVE.getCode()))
            .thenReturn(Arrays.asList(testSessionEntity));
        when(chunkMapper.findChunkIndexesByStorageId("test-session-001"))
            .thenReturn(Arrays.asList(0, 1, 2, 3, 4)); // 5 out of 10 = 50%
        
        // Mock oldest active session
        when(sessionMapper.selectOne(any())).thenReturn(testSessionEntity);
        
        // When
        SessionStatistics stats = storageSessionService.getSessionStatistics();
        
        // Then
        assertEquals(5L, stats.getActiveSessions());
        assertEquals(10L, stats.getCompletedSessions());
        assertEquals(2L, stats.getFailedSessions());
        assertEquals(3L, stats.getExpiredSessions());
        assertEquals(20L, stats.getTotalSessions());
        assertEquals(50.0, stats.getAverageUploadProgress(), 0.01);
        assertNotNull(stats.getOldestActiveSession());
    }
    
    @Test
    void testGetUploadStatistics() {
        // Given
        Map<String, Object> expectedStats = Map.of(
            "uploaded_count", 5L,
            "uploaded_size", 5242880L,
            "first_upload_time", LocalDateTime.now().minusHours(2),
            "last_upload_time", LocalDateTime.now()
        );
        when(chunkMapper.getUploadStatistics("test-session-001")).thenReturn(expectedStats);
        
        // When
        Map<String, Object> stats = storageSessionService.getUploadStatistics("test-session-001");
        
        // Then
        assertEquals(expectedStats, stats);
        verify(chunkMapper).getUploadStatistics("test-session-001");
    }
    
    @Test
    void testRecordChunkUpload_SessionNotActive() {
        // Given
        StorageSessionEntity inactiveSession = StorageSessionEntity.builder()
            .storageId("test-session-001")
            .status(StorageSessionStatus.COMPLETED.getCode())
            .build();
        
        when(sessionMapper.selectById("test-session-001")).thenReturn(inactiveSession);
        
        // When
        ChunkUploadResult result = storageSessionService.recordChunkUpload(
            "test-session-001", 0, 1048576L, "chunk-checksum", 0L);
        
        // Then
        assertFalse(result.isSuccess());
        assertEquals("Storage session is not active", result.getMessage());
        assertEquals("SESSION_NOT_ACTIVE", result.getErrorCode());
        
        verify(sessionMapper).selectById("test-session-001");
        verify(chunkMapper, never()).insert(any());
    }
    
    @Test
    void testCreateSession_SerializationError() throws Exception {
        // Given
        when(sessionMapper.selectById("test-session-001")).thenReturn(null);
        when(objectMapper.writeValueAsString(any())).thenThrow(new RuntimeException("Serialization error"));
        
        Map<String, String> customMetadata = Map.of("key", "value");
        
        // When & Then
        assertThrows(RuntimeException.class, () -> {
            storageSessionService.createSession(
                "test-session-001", "test-file.pdf", 10485760L, 
                "d41d8cd98f00b204e9800998ecf8427e", customMetadata);
        });
        
        verify(sessionMapper).selectById("test-session-001");
        verify(objectMapper).writeValueAsString(any());
        verify(sessionMapper, never()).insert(any());
    }
    
    @Test
    void testConvertToDto_MetadataDeserializationError() throws Exception {
        // Given
        StorageSessionEntity entityWithBadMetadata = StorageSessionEntity.builder()
            .storageId("test-session-001")
            .originalFileName("test-file.pdf")
            .fileSize(10485760L)
            .status(StorageSessionStatus.ACTIVE.getCode())
            .customMetadata("invalid-json")
            .build();
        
        when(sessionMapper.selectById("test-session-001")).thenReturn(entityWithBadMetadata);
        when(chunkMapper.findChunkIndexesByStorageId("test-session-001")).thenReturn(Arrays.asList());
        
        // When
        Optional<StorageSessionDto> result = storageSessionService.getSession("test-session-001");
        
        // Then
        assertTrue(result.isPresent());
        assertNotNull(result.get().getCustomMetadata());
        assertTrue(result.get().getCustomMetadata().isEmpty()); // Should default to empty map
    }
    
    @Test
    void testCalculateChunkSize() {
        // Test different file sizes and expected chunk sizes
        // This tests the private method indirectly through createSession
        
        // Test small file (5MB) - should use 256KB chunks
        when(sessionMapper.selectById("small-file")).thenReturn(null);
        when(sessionMapper.insert(any())).thenReturn(1);
        
        StorageSessionDto smallFileSession = storageSessionService.createSession(
            "small-file", "small.txt", 5 * 1024 * 1024L, "checksum", Map.of());
        
        assertEquals(256 * 1024, smallFileSession.getChunkSize()); // 256KB
        
        // Test medium file (50MB) - should use 1MB chunks
        when(sessionMapper.selectById("medium-file")).thenReturn(null);
        
        StorageSessionDto mediumFileSession = storageSessionService.createSession(
            "medium-file", "medium.txt", 50 * 1024 * 1024L, "checksum", Map.of());
        
        assertEquals(1024 * 1024, mediumFileSession.getChunkSize()); // 1MB
        
        // Test large file (500MB) - should use 2MB chunks
        when(sessionMapper.selectById("large-file")).thenReturn(null);
        
        StorageSessionDto largeFileSession = storageSessionService.createSession(
            "large-file", "large.txt", 500 * 1024 * 1024L, "checksum", Map.of());
        
        assertEquals(2 * 1024 * 1024, largeFileSession.getChunkSize()); // 2MB
        
        // Test very large file (2GB) - should use 4MB chunks
        when(sessionMapper.selectById("huge-file")).thenReturn(null);
        
        StorageSessionDto hugeFileSession = storageSessionService.createSession(
            "huge-file", "huge.txt", 2L * 1024 * 1024 * 1024, "checksum", Map.of());
        
        assertEquals(4 * 1024 * 1024, hugeFileSession.getChunkSize()); // 4MB
    }
    
    @Test
    void testStorageSessionDto_HelperMethods() {
        // Test isComplete method
        StorageSessionDto completeSession = StorageSessionDto.builder()
            .totalChunks(5)
            .uploadedChunks(Arrays.asList(0, 1, 2, 3, 4))
            .build();
        assertTrue(completeSession.isComplete());
        
        StorageSessionDto incompleteSession = StorageSessionDto.builder()
            .totalChunks(5)
            .uploadedChunks(Arrays.asList(0, 1, 2))
            .build();
        assertFalse(incompleteSession.isComplete());
        
        // Test calculateUploadProgress method
        assertEquals(100.0, completeSession.calculateUploadProgress(), 0.01);
        assertEquals(60.0, incompleteSession.calculateUploadProgress(), 0.01);
        
        // Test getMissingChunks method
        List<Integer> missingChunks = incompleteSession.getMissingChunks();
        assertEquals(2, missingChunks.size());
        assertTrue(missingChunks.contains(3));
        assertTrue(missingChunks.contains(4));
        
        // Test isExpired method
        StorageSessionDto expiredSession = StorageSessionDto.builder()
            .lastAccessTime(LocalDateTime.now().minusHours(7))
            .build();
        assertTrue(expiredSession.isExpired(java.time.Duration.ofHours(6)));
        
        StorageSessionDto activeSession = StorageSessionDto.builder()
            .lastAccessTime(LocalDateTime.now().minusHours(1))
            .build();
        assertFalse(activeSession.isExpired(java.time.Duration.ofHours(6)));
    }
}