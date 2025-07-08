package com.redjujubetree.qmt.service;

import com.redjujubetree.qmt.domain.StockDividendDistribution;
import com.redjujubetree.qmt.domain.entity.StockDividend;
import com.baomidou.mybatisplus.extension.service.IService;
import com.redjujubetree.qmt.domain.param.DividendYieldParam;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tanjianwei
 * @since 2025-07-07
 */
public interface StockDividendService extends IService<StockDividend> {

    void saveStockDividend(StockDividend stockDividend);

    void updateStockDividendById(StockDividend stockDividend);

	List<StockDividendDistribution> queryWeekTrend(DividendYieldParam param);
}
