package com.redjujubetree.qmt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redjujubetree.qmt.domain.entity.KennyStockInfo;
import com.redjujubetree.qmt.mapper.KennyStockInfoMapper;
import com.redjujubetree.qmt.service.KennyStockInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tanjianwei
 * @since 2025-07-07
 */
@Service
public class KennyStockInfoServiceImpl extends ServiceImpl<KennyStockInfoMapper, KennyStockInfo> implements KennyStockInfoService {

    @Transactional(rollbackFor = Throwable.class)
	public void saveKennyStockInfo(KennyStockInfo kennyStockInfo) {

		KennyStockInfo entity = new KennyStockInfo();
		BeanUtil.copyProperties(kennyStockInfo, entity);
		Date date = new Date();
		save(entity);
	}

    public void updateKennyStockInfoById(KennyStockInfo kennyStockInfo) {
        updateById(kennyStockInfo);
    }
}
