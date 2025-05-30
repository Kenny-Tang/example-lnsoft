package com.redjujubetree.users.domain.dto.param;

import lombok.Data;

@Data
public class PageParam {
	private Integer pageSize = 10;
	private Integer pageNum = 1;

}
