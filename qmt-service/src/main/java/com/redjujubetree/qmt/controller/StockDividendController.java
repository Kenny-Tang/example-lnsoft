package com.redjujubetree.qmt.controller;

import com.alibaba.fastjson.JSON;
import com.redjujubetree.qmt.domain.bo.StockDividendDistribution;
import com.redjujubetree.qmt.domain.entity.StockDividend;
import com.redjujubetree.qmt.domain.param.DividendYieldParam;
import com.redjujubetree.qmt.service.StockDividendService;
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
 *  前端控制器
 * </p>
 *
 * @author tanjianwei
 * @since 2025-07-07
 */
@Slf4j
@Setter
@RestController
@RequestMapping("/qmt/stockDividend")
public class StockDividendController {

    @Resource
    private StockDividendService stockDividendService;

	public BaseResponse save(StockDividend stockDividend) {
		stockDividendService.saveStockDividend(stockDividend);
		return new BaseResponse(RespCodeEnum.SUCCESS.getCode(), RespCodeEnum.SUCCESS.getMessage());
	}

	@PostMapping("list-trends")
	public BaseResponse list(@RequestBody DividendYieldParam param) {
		log.info("DividendYieldParam : {}", JSON.toJSONString(param));
		List<StockDividendDistribution> stockDividendDistributions = stockDividendService.queryDividendAmountYear(param);
		return BaseResponse.ofSuccess(stockDividendDistributions);
	}
}
