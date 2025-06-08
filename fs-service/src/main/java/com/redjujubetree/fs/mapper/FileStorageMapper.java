package com.redjujubetree.fs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.redjujubetree.fs.domain.entity.FileStorage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author tanjianwei
 * @since 2025-06-06
 */
public interface FileStorageMapper extends BaseMapper<FileStorage> {
	@Select("SELECT * FROM file_storage WHERE id = #{id}")
	FileStorage getById(@Param("id") Long id);
}
