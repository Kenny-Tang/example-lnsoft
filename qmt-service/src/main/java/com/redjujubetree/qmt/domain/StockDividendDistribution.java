package com.redjujubetree.qmt.domain;

import lombok.Data;

@Data
public class StockDividendDistribution {
	private String stockCode;
	private String stockName;
	private int realYear;
	private Double dividendDistribution;
}
