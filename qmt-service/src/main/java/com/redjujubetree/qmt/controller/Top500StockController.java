package com.redjujubetree.qmt.controller;

import com.redjujubetree.response.BaseResponse;
import com.redjujubetree.response.RespCodeEnum;
import lombok.extern.slf4j.Slf4j;
import lombok.Setter;
import com.redjujubetree.qmt.domain.entity.Top500Stock;
import org.springframework.web.bind.annotation.RequestMapping;
import com.redjujubetree.qmt.service.Top500StockService;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 沪深A股实时行情（含top500） 前端控制器
 * </p>
 *
 * @author tanjianwei
 * @since 2025-07-07
 */
@Slf4j
@Setter
@RestController
@RequestMapping("/qmt/top500Stock")
public class Top500StockController {

    @Resource
    private Top500StockService top500StockService;

	public BaseResponse save(Top500Stock top500Stock) {
		try {
			top500StockService.saveTop500Stock(top500Stock);
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
