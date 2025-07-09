package com.redjujubetree.qmt.service;

import com.redjujubetree.qmt.domain.bo.StockPriceBO;
import com.redjujubetree.qmt.domain.entity.StockDailyData;
import com.baomidou.mybatisplus.extension.service.IService;
import com.redjujubetree.qmt.domain.param.PriceParam;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 每日股票交易数据 服务类
 * </p>
 *
 * @author tanjianwei
 * @since 2025-07-01
 */
public interface StockDailyDataService extends IService<StockDailyData> {

    void saveStockDailyData(StockDailyData stockDailyData);

    void updateStockDailyDataById(StockDailyData stockDailyData);

    StockDailyData queryByCodeAndDate(String number, Date transDate);

	List<StockPriceBO> queryWeekData(PriceParam param);
}