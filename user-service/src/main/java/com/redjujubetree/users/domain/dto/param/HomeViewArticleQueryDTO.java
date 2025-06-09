package com.redjujubetree.users.domain.dto.param;

import lombok.Data;

@Data
public class HomeViewArticleQueryDTO extends PageParam{
	private String searchKey;
	private String notPath;

	public String key() {
		return "hvaqd{searchKey=" + searchKey + ",page=" + this.getPageNum() + ",size=" + this.getPageSize()+ "}";
	}
}
