package com.redjujubetree.users.service;

import com.redjujubetree.users.domain.entity.UserVisitTrace;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tanjianwei
 * @since 2025-05-13
 */
public interface UserVisitTraceService extends IService<UserVisitTrace> {

    void saveUserVisitTrace(UserVisitTrace userVisitTrace);

    void updateUserVisitTraceById(UserVisitTrace userVisitTrace);
}
