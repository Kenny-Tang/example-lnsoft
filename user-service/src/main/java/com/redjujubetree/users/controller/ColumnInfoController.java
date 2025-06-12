package com.redjujubetree.users.controller;

import com.redjujubetree.response.BaseResponse;
import com.redjujubetree.response.RespCodeEnum;
import com.redjujubetree.users.domain.dto.ColumnDTO;
import com.redjujubetree.users.domain.entity.ColumnInfo;
import com.redjujubetree.users.service.ColumnInfoService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

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

	@GetMapping("/list")
	public BaseResponse list() {
		List<ColumnInfo> list = columnInfoService.list();
		List<ColumnDTO> collect = list.stream().map(en -> {
			ColumnDTO target = new ColumnDTO();
			BeanUtils.copyProperties(en, target);
			target.setId(en.getId().toString());
			return target;
		}).collect(Collectors.toList());
		return BaseResponse.ofSuccess(collect);
	}
}
