package com.redjujubetree.users.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redjujubetree.users.domain.entity.UserVisitTrace;
import com.redjujubetree.users.mapper.UserVisitTraceMapper;
import com.redjujubetree.users.service.UserVisitTraceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tanjianwei
 * @since 2025-05-13
 */
@Service
public class UserVisitTraceServiceImpl extends ServiceImpl<UserVisitTraceMapper, UserVisitTrace> implements UserVisitTraceService {

    @Transactional(rollbackFor = Throwable.class)
	public void saveUserVisitTrace(UserVisitTrace userVisitTrace) {

		UserVisitTrace entity = new UserVisitTrace();
		BeanUtil.copyProperties(userVisitTrace, entity);
		Date date = new Date();
		entity.setUpdateTime(date);
		entity.setCreateTime(date);
		entity.setVersion(0);
		save(entity);
	}

    public void updateUserVisitTraceById(UserVisitTrace userVisitTrace) {
        userVisitTrace.setUpdateTime(new Date());
        updateById(userVisitTrace);
    }
}
