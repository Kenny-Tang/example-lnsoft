package com.redjujubetree.example;

import cn.hutool.core.date.DateUtil;
import com.redjujubetree.qmt.domain.entity.StockDailyData;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Account{
	private StockEnum holdingStock;
	public Account(StockEnum stock,double balance) {
		this.holdingStock = stock;
		this.stockName = stock.getName();
		this.stockCode = stock.getCode();
		this.balance = balance;
	}
	public Account(String stock,double balance) {
		this.holdingStock = StockEnum.getByCode(stock);
		this.stockName = holdingStock.getName();
		this.stockCode = holdingStock.getCode();
		this.balance = balance;
	}

	private String stockName;
	private String stockCode;
	private double balance;
	private double baseLine = 0;
	private double cost = 0;
	private int stockAmount;
	double v2 = 0.4;
	int fixedAmount = 1*10000;
	private double appendTrans = 0;

	public Double getTotalValue(double stockPrice) {
		double v = balance + stockAmount * stockPrice;
		// 保留两位小数
		return BigDecimal.valueOf(v).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public double getClearLine() {
		double v = this.baseLine / this.stockAmount;
		double v1 = v + v * v2;
		return ((int)(v1*100)) / 100.0; // 保留两位小数
	}

	public double purchase(int hand, StockDailyData transInfo) {
		return purchase(hand, transInfo, 0);
	}

	public double purchase(int hand, StockDailyData transInfo, int isAppend) {
		double openPrice = transInfo.getOpen().doubleValue();
		double v = openPrice * 100 * hand;
		//this.balance -= v;
		this.baseLine += v;
		this.cost += v;
		this.stockAmount += hand * 100;
		if (balance < 0) {
			throw new RuntimeException("账户余额不足，无法购买股票");
		}
		double profit = getProfit(openPrice);

		if (isAppend>0) {
			System.out.println(DateUtil.formatDate(transInfo.getTradeDate())+"：追加 "+isAppend+" 倍，持仓成本："+ this.getBaseLine()+"，\t当前持仓："+getStockAmount()+"，\t当前市值："+getCurrentValue(openPrice).intValue()+"，\t盈亏："+  "%+.2f".formatted(profit) +"，\t交易价格："+ openPrice+"，\t减仓线："+getClearLine()+ "，\t总价值：" + getTotalValue(openPrice));
		} else {
			System.out.println(DateUtil.formatDate(transInfo.getTradeDate())+"：买入 1 次，持仓成本："+ this.getBaseLine()+"，\t当前持仓："+getStockAmount()+"，\t当前市值："+getCurrentValue(openPrice).intValue()+"，\t盈亏："+ "%+.2f".formatted(profit) +"，\t交易价格："+ openPrice+"，\t减仓线："+getClearLine()+ "，\t总价值：" + getTotalValue(openPrice));
		}
		return profit;
	}

	public double getProfit(double price) {
		double currentValue = getCurrentValue(price);
		double profit = currentValue - baseLine;
		// 保留两位小数
		return BigDecimal.valueOf(profit/baseLine).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public Double getCurrentValue(double price) {
		return BigDecimal.valueOf(stockAmount * price).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public String sell(int hand, StockDailyData stock) {
		BigDecimal decimal = stock.getOpen();
		double openValue = decimal.doubleValue();
		double v = openValue * 100 * hand;
		this.stockAmount -= hand * 100;
		this.balance += v;
		this.cost = cost - cost * v2;
		this.baseLine = stockAmount * openValue;
		String x = "\n" + DateUtil.formatDate(stock.getTradeDate()) + "-" + this.getStockName() + "：卖出，持仓成本：" + cost + "，股票数量：" + getStockAmount() + "，当前价格：" + openValue + "，清仓线：" + v2 + "，总价值：" + getTotalValue(openValue) +"\n";
		System.out.println(x);
		return x;
	}

	public void fixedPurchase(int fixedAmount, StockDailyData stockDailyData) {
		// 交易手数
		int hand = (int) (fixedAmount / stockDailyData.getOpen().doubleValue() / 100);
		purchase(hand, stockDailyData);

		double append = getAppendTransThreshold(stockDailyData);
		if (append > 0) {
			purchase((int) (hand * append), stockDailyData, (int)append);
		}
	}

	private double getAppendTransThreshold(StockDailyData stockDailyData) {
		double profit = getProfit(stockDailyData.getOpen().doubleValue());
		if (profit < -0.3) {
			return 3;
		}
		if (profit < -0.2) {
			setAppendTrans(1);
			return 2;
		}
		if (profit < -0.1) {
			return 1;
		}
		if (profit > 0.2) {
			setAppendTrans(0);
		}
		return getAppendTrans();
	}

	public String toString(double stockPrice) {
		return "Account{" +
				", stockName='" + stockName + '\'' +
				", stockCode='" + stockCode + '\'' +
				", 现金余额=" + balance +
				", baseLine=" + baseLine +
				", 持仓成本=" + cost +
				", 持仓数量=" + stockAmount +
				", 减仓阈值=" + v2 +
				", 定投金额=" + fixedAmount +
				", 账户总额=" + (this.balance + this.getStockAmount() * stockPrice) +
				'}';
	}
}