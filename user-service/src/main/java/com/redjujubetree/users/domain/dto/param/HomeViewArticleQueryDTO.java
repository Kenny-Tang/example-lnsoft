package com.redjujubetree.users.domain.dto.param;

import lombok.Data;

@Data
public class HomeViewArticleQueryDTO extends PageParam{
	private String searchKey;

	public String toString() {
		return "HomeViewArticleQueryDTO{" +
				"searchKey='" + searchKey + '\'' +
				", page=" + this.getPageNum() +
				", size=" + this.getPageSize()+
				'}';
	}
}
