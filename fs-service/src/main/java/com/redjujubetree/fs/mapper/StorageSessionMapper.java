package com.redjujubetree.fs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.redjujubetree.fs.domain.entity.StorageSessionEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * MyBatis-Plus mapper for storage session operations
 */
@Mapper
@Repository
public interface StorageSessionMapper extends BaseMapper<StorageSessionEntity> {
    
    /**
     * Find sessions by status
     */
    @Select("SELECT * FROM storage_sessions WHERE status = #{status}")
    List<StorageSessionEntity> findByStatus(@Param("status") String status);
    
    /**
     * Find sessions by last access time before given time
     */
    @Select("SELECT * FROM storage_sessions WHERE last_access_time < #{cutoffTime}")
    List<StorageSessionEntity> findByLastAccessTimeBefore(@Param("cutoffTime") LocalDateTime cutoffTime);
    
    /**
     * Find sessions by status and last access time
     */
    @Select("SELECT * FROM storage_sessions WHERE status = #{status} AND last_access_time < #{cutoffTime}")
    List<StorageSessionEntity> findByStatusAndLastAccessTimeBefore(
        @Param("status") String status, @Param("cutoffTime") LocalDateTime cutoffTime);
    
    /**
     * Update last access time
     */
    @Update("UPDATE storage_sessions SET last_access_time = #{accessTime} WHERE storage_id = #{storageId}")
    int updateLastAccessTime(@Param("storageId") String storageId, @Param("accessTime") LocalDateTime accessTime);
    
    /**
     * Update session status
     */
    @Update("UPDATE storage_sessions SET status = #{status} WHERE storage_id = #{storageId}")
    int updateStatus(@Param("storageId") String storageId, @Param("status") String status);
    
    /**
     * Count sessions by status
     */
    @Select("SELECT COUNT(*) FROM storage_sessions WHERE status = #{status}")
    long countByStatus(@Param("status") String status);
    
    /**
     * Get session with upload progress
     */
    @Select("SELECT s.*, \n" +
            "               COALESCE(c.uploaded_count, 0) as uploaded_count,\n" +
            "               CASE \n" +
            "                   WHEN s.total_chunks = 0 THEN 0 \n" +
            "                   ELSE ROUND(COALESCE(c.uploaded_count, 0) * 100.0 / s.total_chunks, 2) \n" +
            "               END as upload_progress\n" +
            "        FROM storage_sessions s\n" +
            "        LEFT JOIN (\n" +
            "            SELECT storage_id, COUNT(*) as uploaded_count \n" +
            "            FROM uploaded_chunks \n" +
            "            GROUP BY storage_id\n" +
            "        ) c ON s.storage_id = c.storage_id\n" +
            "        WHERE s.storage_id = #{storageId}")
    @Results({
        @Result(property = "storageId", column = "storage_id"),
        @Result(property = "originalFileName", column = "original_filename"),
        @Result(property = "fileSize", column = "file_size"),
        @Result(property = "expectedChecksum", column = "expected_checksum"),
        @Result(property = "totalChunks", column = "total_chunks"),
        @Result(property = "chunkSize", column = "chunk_size"),
        @Result(property = "createdTime", column = "created_time"),
        @Result(property = "lastAccessTime", column = "last_access_time"),
        @Result(property = "tempFilePath", column = "temp_file_path"),
        @Result(property = "chunkDirPath", column = "chunk_dir_path"),
        @Result(property = "metadataFilePath", column = "metadata_file_path"),
        @Result(property = "customMetadata", column = "custom_metadata")
    })
    Map<String, Object> getSessionWithProgress(@Param("storageId") String storageId);
}