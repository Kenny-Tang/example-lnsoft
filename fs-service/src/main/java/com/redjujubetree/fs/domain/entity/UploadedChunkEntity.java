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
 * Uploaded chunk entity for MyBatis-Plus
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("uploaded_chunks")
public class UploadedChunkEntity {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    @TableField("storage_id")
    private String storageId;
    
    @TableField("chunk_index")
    private Integer chunkIndex;
    
    @TableField("chunk_size")
    private Long chunkSize;
    
    @TableField("chunk_checksum")
    private String chunkChecksum;
    
    @TableField("uploaded_time")
    private Long uploadedTime;
    
    @TableField("chunk_offset")
    private Long chunkOffset;
    
}