package com.lnsoft.cloud.ztbgl;

import com.alibaba.fastjson.JSON;
import com.lnsoft.cloud.ztbgl.common.resp.RespResult;
import com.lnsoft.cloud.ztbgl.controller.ZtbInvoiceFetchProjectContreller;
import com.lnsoft.cloud.ztbgl.domain.dto.InvoiceFetchProjectParam;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class InvoiceFetchProjectTest {

	@Resource
	private ZtbInvoiceFetchProjectContreller ztbInvoiceFetchProjectContreller;

	@Test
	public void testCreateProject() {
		InvoiceFetchProjectParam project = new InvoiceFetchProjectParam();
		project.setProjectName("测试项目");
		project.setProjectYear("2025");
		RespResult save = ztbInvoiceFetchProjectContreller.save(project);
		System.out.println(JSON.toJSONString(save));
	}

	@Test
	public void testUpdateProject() {
		InvoiceFetchProjectParam project = new InvoiceFetchProjectParam();
		project.setProjectName("测试项目");
		project.setProjectYear("2025");
		RespResult save = ztbInvoiceFetchProjectContreller.save(project);
		System.out.println(JSON.toJSONString(save));
	}
}
