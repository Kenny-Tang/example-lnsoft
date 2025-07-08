package com.redjujubetree.qmt.service;

import com.redjujubetree.qmt.domain.entity.KennyStockInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tanjianwei
 * @since 2025-07-07
 */
public interface KennyStockInfoService extends IService<KennyStockInfo> {

    void saveKennyStockInfo(KennyStockInfo kennyStockInfo);

    void updateKennyStockInfoById(KennyStockInfo kennyStockInfo);
}
