package com.redjujubetree.users.service.impl;

import com.redjujubetree.users.model.entity.UserInfo;
import com.redjujubetree.users.mapper.UserInfoMapper;
import com.redjujubetree.users.service.IUserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tanjianwei
 * @since 2025-01-24
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {

}
