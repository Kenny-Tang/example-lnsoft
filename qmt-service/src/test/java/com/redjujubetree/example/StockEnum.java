package com.redjujubetree.example;

import lombok.Getter;

@Getter
public enum StockEnum {
	WWIDDSLI("000338", "潍柴动力"),
	NAVIETF("159941", "纳斯达克ETF"),
	UFVG100("159901", "深证100ETF"),
//	HUUF300ETF("510300", "沪深300ETF"),
//	YIYCETF("512010", "医药ETF"),
//	RIJKETF("513520", "日经ETF"),
	YILIGUFF("600887", "伊利股份"),
	XKYEYBHH("601166","兴业银行"),
	VSGOPKAN("601318", "中国平安"),
	// TDGEYIYC("300347", "泰格医药"),
	VCUHYBHH("600036", "招商银行"),
	;
	private String code;
	private String name;
	StockEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}

	@Override
	public String toString() {
		return this.code + " - " + this.name;
	}

	public static StockEnum getByCode(String code) {
		for (StockEnum stockEnum : StockEnum.values()) {
			if (stockEnum.getCode().equals(code)) {
				return stockEnum;
			}
		}
		return null;
	}

	public static void main(String[] args) {
		for (StockEnum value : StockEnum.values()) {
			System.out.print("'"+value.getCode() + "',");
		}
	}
}
