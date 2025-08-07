package com.redjujubetree.fs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.redjujubetree.fs.domain.entity.UploadedChunkEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * MyBatis-Plus mapper for uploaded chunk operations
 */
@Mapper
@Repository
public interface UploadedChunkMapper extends BaseMapper<UploadedChunkEntity> {
    
    /**
     * Find chunks by storage ID ordered by chunk index
     */
    @Select("SELECT * FROM uploaded_chunks WHERE storage_id = #{storageId} ORDER BY chunk_index")
    List<UploadedChunkEntity> findByStorageIdOrderByChunkIndex(@Param("storageId") String storageId);
    
    /**
     * Find chunk indexes by storage ID
     */
    @Select("SELECT chunk_index FROM uploaded_chunks WHERE storage_id = #{storageId} ORDER BY chunk_index")
    List<Integer> findChunkIndexesByStorageId(@Param("storageId") String storageId);
    
    /**
     * Check if chunk exists
     */
    @Select("SELECT COUNT(*) > 0 FROM uploaded_chunks WHERE storage_id = #{storageId} AND chunk_index = #{chunkIndex}")
    boolean existsByStorageIdAndChunkIndex(@Param("storageId") String storageId, @Param("chunkIndex") Integer chunkIndex);
    
    /**
     * Delete chunks by storage ID
     */
    @Delete("DELETE FROM uploaded_chunks WHERE storage_id = #{storageId}")
    int deleteByStorageId(@Param("storageId") String storageId);
    
    /**
     * Count chunks by storage ID
     */
    @Select("SELECT COUNT(*) FROM uploaded_chunks WHERE storage_id = #{storageId}")
    long countByStorageId(@Param("storageId") String storageId);
    
    /**
     * Batch insert chunks
     */
    @Insert("INSERT INTO uploaded_chunks (storage_id, chunk_index, chunk_size, chunk_checksum, chunk_offset, uploaded_time, create_time, update_time)\n" +
            "        VALUES\n" +
            "        <foreach collection=\"chunks\" item=\"chunk\" separator=\",\">\n" +
            "            (#{chunk.storageId}, #{chunk.chunkIndex}, #{chunk.chunkSize}, #{chunk.chunkChecksum}, #{chunk.chunkOffset}, #{chunk.uploadedTime}, NOW(), NOW())\n" +
            "        </foreach>")
    int batchInsertChunks(@Param("chunks") List<UploadedChunkEntity> chunks);
    
    /**
     * Get upload statistics for a session
     */
    @Select("SELECT \n" +
            "            COUNT(*) as uploaded_count,\n" +
            "            SUM(chunk_size) as uploaded_size,\n" +
            "            MIN(uploaded_time) as first_upload_time,\n" +
            "            MAX(uploaded_time) as last_upload_time\n" +
            "        FROM uploaded_chunks \n" +
            "        WHERE storage_id = #{storageId}")
    Map<String, Object> getUploadStatistics(@Param("storageId") String storageId);
}