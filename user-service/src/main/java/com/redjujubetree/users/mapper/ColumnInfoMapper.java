package com.redjujubetree.users.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.redjujubetree.users.domain.entity.ColumnInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 专栏信息 Mapper 接口
 * </p>
 *
 * @author tanjianwei
 * @since 2025-05-15
 */
public interface ColumnInfoMapper extends BaseMapper<ColumnInfo> {

	default List<ColumnInfo> getColumnInfosWithDisplayOrder() {
		LambdaQueryWrapper<ColumnInfo> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.orderByAsc(ColumnInfo::getDisplayOrder);
		List<ColumnInfo> list = selectList(queryWrapper);
		return list;
	}
}
