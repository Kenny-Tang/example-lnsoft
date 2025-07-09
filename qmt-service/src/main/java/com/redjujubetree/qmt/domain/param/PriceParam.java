package com.redjujubetree.qmt.domain.param;

import lombok.Data;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Data
public class PriceParam {
	private Date startDate ;
	private List<String> codes;
	{
		LocalDate twoYearsAgo = LocalDate.now().minusYears(2);
		startDate = Date.from(twoYearsAgo.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

}
