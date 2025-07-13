package com.redjujubetree.qmt.domain.bo;

import lombok.Data;

@Data
public class StockDividendDistribution extends StockTrend{
	private String stockCode;
	private String stockName;
	private int realYear;
	private Double dividendDistribution;
	private Double pctDividend;

	@Override
	public String getX() {
		return this.getRealYear()+"";
	}

	@Override
	public String getLabel() {
		return getStockName();
	}

	@Override
	public String getV() {
		return getPctDividend()+"";
	}
}
