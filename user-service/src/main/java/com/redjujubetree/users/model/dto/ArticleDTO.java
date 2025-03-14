package com.redjujubetree.users.model.dto;

import lombok.Data;

@Data
public class ArticleDTO {
	private String id;
	private String path;
	private String name;
	private String url;
	private String icon;
	private String component;
	private String createTime;
}
