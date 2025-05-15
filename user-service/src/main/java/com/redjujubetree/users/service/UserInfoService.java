package com.redjujubetree.users.service;

import com.redjujubetree.users.domain.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tanjianwei
 * @since 2025-05-07
 */
public interface UserInfoService extends IService<UserInfo> {

    void saveUserInfo(UserInfo userInfo);

    void updateUserInfoById(UserInfo userInfo);
}
