package com.redjujubetree.users.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class ArticleDTO extends ColumnDTO {
	private String id;
	private String columnId;
	private String path;
	private String title;
	private String url;
	private String icon;
	private String component;
	private String updateTime;
	private Integer displayOrder;
	private String summary;
	private List<ArticleDTO> children;
}
