package com.redjujubetree.qmt.domain.bo;

import lombok.Data;

@Data
public class StockPriceBO extends StockTrend{
	private String stockCode;
	private String stockName;
	private String tradeDate;
	private double price;

	public String getX() {
		return tradeDate;
	}

	@Override
	public String getLabel() {
		return stockCode+"-"+stockName;
	}

	@Override
	public String getV() {
		return getPrice()+"";
	}
}
