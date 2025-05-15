package com.lnsoft.cloud.ztbgl.domain.dto;

import lombok.Data;

@Data
public class InvoiceFetchProjectParam {

	private String id;

	private String projectName;

	/**
	 * 项目年份
	 */
	private String projectYear;
}
