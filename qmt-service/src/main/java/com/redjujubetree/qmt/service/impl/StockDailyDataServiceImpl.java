package com.redjujubetree.qmt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redjujubetree.common.CacheUtil;
import com.redjujubetree.qmt.domain.entity.StockDailyData;
import com.redjujubetree.qmt.mapper.StockDailyDataMapper;
import com.redjujubetree.qmt.service.StockDailyDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 每日股票交易数据 服务实现类
 * </p>
 *
 * @author tanjianwei
 * @since 2025-07-01
 */
@Slf4j
@Service
public class StockDailyDataServiceImpl extends ServiceImpl<StockDailyDataMapper, StockDailyData> implements StockDailyDataService {

	@Transactional(rollbackFor = Throwable.class)
	public void saveStockDailyData(StockDailyData stockDailyData) {

		StockDailyData entity = new StockDailyData();
		BeanUtil.copyProperties(stockDailyData, entity);
		Date date = new Date();
		save(entity);
	}

	public void updateStockDailyDataById(StockDailyData stockDailyData) {
		updateById(stockDailyData);
	}

	public StockDailyData queryByCodeAndDate(String number, Date transDate) {
		if (number == null || transDate == null) {
			return null;
		}
		String key = getKey(number, transDate);
		StockDailyData o = CacheUtil.get(key);
		if (o != null) {
			return returnCacheStock(o);
		}
		DateTime val = DateUtil.offsetMonth(transDate, 6);
		List<StockDailyData> dataList = baseMapper.selectList(
				Wrappers.lambdaQuery(StockDailyData.class).eq(StockDailyData::getTsCode, number)
						.ge(StockDailyData::getTradeDate, transDate)
						.lt(StockDailyData::getTradeDate, val));
		Date ite = transDate;
		while (ite.before(val)) {
			Date finalIte = ite;
			StockDailyData stockDailyDatum = dataList.stream()
					.filter(t -> t.getTradeDate().equals(finalIte))
					.findFirst()
					.orElse(null);
			if (stockDailyDatum == null) {
				log.debug("未找到 {} 的交易数据: {}", number, ite);
				CacheUtil.put(getKey(number, ite), new StockDailyData(), 8, TimeUnit.HOURS);
			} else {
				log.debug("查询到 {} 的交易数据: {}", number, stockDailyDatum);
				CacheUtil.put(getKey(number, ite),stockDailyDatum, 8, TimeUnit.HOURS);
			}
			ite = DateUtil.offsetDay(ite, 1);
		}

		return returnCacheStock(CacheUtil.get(key));
	}

	private static StockDailyData returnCacheStock(StockDailyData o) {
		if (Objects.isNull(o.getTsCode())) {
			return null;
		}
		return o;
	}

	public static String getKey(String number, Date transDate) {
		return number + ":" + DatePattern.PURE_DATETIME_FORMAT.format(transDate);
	}


	public String getKey(StockDailyData e) {
		return e.getTsCode()+ ":" + DatePattern.PURE_DATETIME_FORMAT.format(e.getTradeDate());
	}

}
