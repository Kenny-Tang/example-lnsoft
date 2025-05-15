package com.lnsoft.cloud.ztbgl.controller;


import cn.hutool.core.lang.Assert;
import com.lnsoft.cloud.ztbgl.common.resp.RespCodeEnum;
import com.lnsoft.cloud.ztbgl.common.resp.RespResult;
import com.lnsoft.cloud.ztbgl.domain.dto.InvoiceFetchProjectParam;
import com.lnsoft.cloud.ztbgl.service.ZtbInvoiceFetchProjectService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@Setter
@RestController
public class ZtbInvoiceFetchProjectContreller {

	@Resource
	private ZtbInvoiceFetchProjectService ztbInvoiceFetchProjectService;

	@PostMapping("/invoice/fetch/project/")
	public RespResult save(InvoiceFetchProjectParam project) {
		try {
			Assert.notBlank(project.getProjectName(), "项目名称不能为空");
			Assert.notBlank(project.getProjectYear(), "项目年度不能为空");
			ztbInvoiceFetchProjectService.saveProject(project);
			return new RespResult(0, "请求成功");
		} catch(IllegalArgumentException e){
			log.warn(e.getMessage(), e);
			return new RespResult(RespCodeEnum.FAIL.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error("系统异常请联系管理员处理", e);
			return new RespResult(RespCodeEnum.FAIL.getCode(), "系统异常请联系管理员处理");
		}
	}

}
