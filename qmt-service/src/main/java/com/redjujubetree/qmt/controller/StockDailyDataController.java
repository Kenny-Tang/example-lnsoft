package com.redjujubetree.qmt.controller;

import com.redjujubetree.response.BaseResponse;
import com.redjujubetree.response.RespCodeEnum;
import lombok.extern.slf4j.Slf4j;
import lombok.Setter;
import com.redjujubetree.qmt.domain.entity.StockDailyData;
import org.springframework.web.bind.annotation.RequestMapping;
import com.redjujubetree.qmt.service.StockDailyDataService;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;

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
}
