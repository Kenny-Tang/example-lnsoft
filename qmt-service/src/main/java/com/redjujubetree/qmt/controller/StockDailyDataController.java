package com.redjujubetree.qmt.controller;

import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSON;
import com.redjujubetree.common.CacheUtil;
import com.redjujubetree.qmt.domain.bo.StockPriceBO;
import com.redjujubetree.qmt.domain.entity.StockDailyData;
import com.redjujubetree.qmt.domain.param.PriceParam;
import com.redjujubetree.qmt.service.StockDailyDataService;
import com.redjujubetree.response.BaseResponse;
import com.redjujubetree.response.RespCodeEnum;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 每日股票交易数据 前端控制器
 * </p>
 *
 * @author tanjianwei
 * @since 2025-07-01
 */
@Slf4j
@Setter
@RestController
@RequestMapping("/qmt/stockDailyData")
public class StockDailyDataController {

    @Resource
    private StockDailyDataService stockDailyDataService;

	public BaseResponse save(StockDailyData stockDailyData) {
		try {
			stockDailyDataService.saveStockDailyData(stockDailyData);
			return new BaseResponse(RespCodeEnum.SUCCESS.getCode(), RespCodeEnum.SUCCESS.getMessage());
		} catch(IllegalArgumentException e){
			log.warn(e.getMessage(), e);
			return new BaseResponse(RespCodeEnum.PARAM_ERROR.getCode(), RespCodeEnum.PARAM_ERROR.getMessage());
		} catch (Exception e) {
			log.error("系统异常请联系管理员处理", e);
			return new BaseResponse(RespCodeEnum.FAIL.getCode(), "系统异常请联系管理员处理");
		}
	}

	@PostMapping("list-trends")
	public BaseResponse list(@RequestBody PriceParam param) {
		log.info("PriceParam {}", JSON.toJSONString(param));
		Assert.notNull(param.getStartDate(), "查询开始时间为必传字段");
		List<StockPriceBO> stockPriceBOS = stockDailyDataService.queryWeekData(param);
		CacheUtil.get("");
		return BaseResponse.ofSuccess(stockPriceBOS);
	}
}
