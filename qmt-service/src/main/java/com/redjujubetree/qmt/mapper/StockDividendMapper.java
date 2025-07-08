package com.redjujubetree.qmt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.redjujubetree.qmt.domain.StockDividendDistribution;
import com.redjujubetree.qmt.domain.entity.StockDividend;
import com.redjujubetree.qmt.domain.param.DividendYieldParam;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author tanjianwei
 * @since 2025-07-07
 */
public interface StockDividendMapper extends BaseMapper<StockDividend> {

	List<StockDividendDistribution> queryWeekTrend(DividendYieldParam param);
}
