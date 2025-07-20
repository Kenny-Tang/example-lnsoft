package com.redjujubetree.qmt.domain.bo;

import lombok.Data;

@Data
public class StockPriceBO extends StockTrend{
	private String stockCode;
	private String stockName;
	private String tradeDate;
	private double price;
	private double dividendEstimated;
	private Double pctDividendEstimated;

	public String getX() {
		return tradeDate;
	}

	@Override
	public String getLabel() {
		return stockName;
	}

	@Override
	public String getV() {
		return getPctDividendEstimated()+"";
	}
}
