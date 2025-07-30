package com.redjujubetree.fs.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Storage session entity for MyBatis-Plus
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("storage_sessions")
public class StorageSessionEntity {
    
    @TableId(value = "storage_id", type = IdType.INPUT)
    private String storageId;
    
    @TableField("original_filename")
    private String originalFileName;
    
    @TableField("file_size")
    private Long fileSize;
    
    @TableField("expected_checksum")
    private String expectedChecksum;
    
    @TableField("total_chunks")
    private Integer totalChunks;
    
    @TableField("chunk_size")
    private Integer chunkSize;
    
    @TableField("created_time")
    private Long createdTime;
    
    @TableField("last_access_time")
    private Long lastAccessTime;
    
    @TableField("status")
    private String status;
    
    @TableField("temp_file_path")
    private String tempFilePath;
    
    @TableField("chunk_dir_path")
    private String chunkDirPath;
    
    @TableField("metadata_file_path")
    private String metadataFilePath;
    
    @TableField("custom_metadata")
    private String customMetadata;
    
}