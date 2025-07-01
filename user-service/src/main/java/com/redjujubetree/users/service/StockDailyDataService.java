package com.redjujubetree.users.service;

import com.redjujubetree.users.domain.entity.StockDailyData;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;

/**
 * <p>
 * 每日股票交易数据 服务类
 * </p>
 *
 * @author tanjianwei
 * @since 2025-06-28
 */
public interface StockDailyDataService extends IService<StockDailyData> {

    void saveStockDailyData(StockDailyData stockDailyData);

    void updateStockDailyDataById(StockDailyData stockDailyData);

    StockDailyData queryByCodeAndDate(String number, Date transDate);
}
