package com.redjujubetree.users.domain.dto;

import lombok.Data;

@Data
public class ColumnDTO {
	private String id;

	/**
	 * 路由路径
	 */
	private String path;

	/**
	 * 专栏显示图标
	 */
	private String icon;

	/**
	 * 专栏名称
	 */
	private String title;

}
