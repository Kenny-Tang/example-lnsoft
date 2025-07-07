package com.redjujubetree.qmt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redjujubetree.qmt.domain.entity.Top500Stock;
import com.redjujubetree.qmt.mapper.Top500StockMapper;
import com.redjujubetree.qmt.service.Top500StockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 * 沪深A股实时行情（含top500） 服务实现类
 * </p>
 *
 * @author tanjianwei
 * @since 2025-07-07
 */
@Service
public class Top500StockServiceImpl extends ServiceImpl<Top500StockMapper, Top500Stock> implements Top500StockService {

    @Transactional(rollbackFor = Throwable.class)
	public void saveTop500Stock(Top500Stock top500Stock) {

		Top500Stock entity = new Top500Stock();
		BeanUtil.copyProperties(top500Stock, entity);
		Date date = new Date();
		save(entity);
	}

    public void updateTop500StockById(Top500Stock top500Stock) {
        updateById(top500Stock);
    }
}
