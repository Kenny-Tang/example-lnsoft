package com.redjujubetree.users.service.impl;

import com.redjujubetree.users.domain.entity.ColumnInfo;
import com.redjujubetree.users.mapper.ColumnInfoMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import cn.hutool.core.bean.BeanUtil;
import java.util.Date;
import com.redjujubetree.users.service.ColumnInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 专栏信息 服务实现类
 * </p>
 *
 * @author tanjianwei
 * @since 2025-05-15
 */
@Service
public class ColumnInfoServiceImpl extends ServiceImpl<ColumnInfoMapper, ColumnInfo> implements ColumnInfoService {

    @Transactional(rollbackFor = Throwable.class)
	public void saveColumnInfo(ColumnInfo columnInfo) {

		ColumnInfo entity = new ColumnInfo();
		BeanUtil.copyProperties(columnInfo, entity);
		Date date = new Date();
		entity.setUpdateTime(date);
		entity.setCreateTime(date);
		entity.setVersion(0);
		save(entity);
	}

    public void updateColumnInfoById(ColumnInfo columnInfo) {
        columnInfo.setUpdateTime(new Date());
        updateById(columnInfo);
    }
}
