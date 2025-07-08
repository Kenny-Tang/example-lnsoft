package com.redjujubetree.qmt.domain.param;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class DividendYieldParam {
	private Date startDate ;
	private List<String> codes;
}
