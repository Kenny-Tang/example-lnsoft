package com.redjujubetree.qmt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.redjujubetree.qmt.domain.bo.StockPriceBO;
import com.redjujubetree.qmt.domain.entity.StockDailyData;
import com.redjujubetree.qmt.domain.param.PriceParam;

import java.util.List;

/**
 * <p>
 * 每日股票交易数据 Mapper 接口
 * </p>
 *
 * @author tanjianwei
 * @since 2025-07-01
 */
public interface StockDailyDataMapper extends BaseMapper<StockDailyData> {

	List<StockPriceBO> queryWeekData(PriceParam param);
}
