package com.redjujubetree.qmt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redjujubetree.qmt.domain.bo.StockDividendDistribution;
import com.redjujubetree.qmt.domain.entity.StockDividend;
import com.redjujubetree.qmt.domain.param.DividendYieldParam;
import com.redjujubetree.qmt.mapper.StockDividendMapper;
import com.redjujubetree.qmt.service.StockDividendService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tanjianwei
 * @since 2025-07-07
 */
@Service
public class StockDividendServiceImpl extends ServiceImpl<StockDividendMapper, StockDividend> implements StockDividendService {

    @Transactional(rollbackFor = Throwable.class)
	public void saveStockDividend(StockDividend stockDividend) {

		StockDividend entity = new StockDividend();
		BeanUtil.copyProperties(stockDividend, entity);
		Date date = new Date();
		save(entity);
	}

    public void updateStockDividendById(StockDividend stockDividend) {
        updateById(stockDividend);
    }

	@Override
	public List<StockDividendDistribution> queryDividendAmountYear(DividendYieldParam param) {
		List<StockDividendDistribution> stockDividendDistributions = baseMapper.queryWeekTrend(param);
		return stockDividendDistributions;
	}
}
