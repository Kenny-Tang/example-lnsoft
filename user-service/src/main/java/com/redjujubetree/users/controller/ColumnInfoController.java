package com.redjujubetree.users.controller;

import com.redjujubetree.response.BaseResponse;
import com.redjujubetree.response.RespCodeEnum;
import lombok.extern.slf4j.Slf4j;
import lombok.Setter;
import com.redjujubetree.users.domain.entity.ColumnInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import com.redjujubetree.users.service.ColumnInfoService;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 专栏信息 前端控制器
 * </p>
 *
 * @author tanjianwei
 * @since 2025-05-15
 */
@Slf4j
@Setter
@RestController
@RequestMapping("/users/columnInfo")
public class ColumnInfoController {

    @Resource
    private ColumnInfoService columnInfoService;

	public BaseResponse save(ColumnInfo columnInfo) {
		try {
			columnInfoService.saveColumnInfo(columnInfo);
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
