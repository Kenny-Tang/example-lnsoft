package com.redjujubetree.users.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redjujubetree.users.domain.entity.UserInfo;
import com.redjujubetree.users.mapper.UserInfoMapper;
import com.redjujubetree.users.service.UserInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tanjianwei
 * @since 2025-05-07
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Transactional(rollbackFor = Throwable.class)
	public void saveUserInfo(UserInfo userInfo) {

		UserInfo entity = new UserInfo();
		BeanUtil.copyProperties(userInfo, entity);
		Date date = new Date();
		entity.setUpdateTime(date);
		entity.setCreateTime(date);
		entity.setVersion(0);
		save(entity);
	}

    public void updateUserInfoById(UserInfo userInfo) {
        userInfo.setUpdateTime(new Date());
        updateById(userInfo);
    }
}
