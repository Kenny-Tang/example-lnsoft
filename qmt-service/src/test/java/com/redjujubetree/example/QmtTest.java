package com.redjujubetree.example;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.redjujubetree.qmt.domain.entity.StockDailyData;
import com.redjujubetree.qmt.service.StockDailyDataService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@SpringBootTest
public class QmtTest {
	//		String number = "600887";
//		String number = "000338";

	@Resource
	private StockDailyDataService stockDailyDataService;

	@Test
	public void testTrade() {

		List<String> results = new ArrayList<>();
		for (StockEnum value : StockEnum.values()) {
			Account account = new Account(value,0*10000);
			if (value.equals(StockEnum.NAVIETF)) {
				account.setV2(0.4);
			}
			log.info("开仓："+value);
			String strategy = strategy(account);
			results.add(strategy);
			strategy = strategy(account);
			results.add(strategy);
			break;
		}
		// results.forEach(t -> System.out.println(t));
	}

	private String strategy(Account account) {
		StringBuilder stringBuilder = new StringBuilder();
		// 定投开始时间
		Date transDate = DateUtil.parse("2021-01-01");
		// 定投结束时间
		Date endDate = new Date() ;
		StockDailyData stockDailyData = null;
		Date beginOfMonth = DateUtil.beginOfMonth(transDate);
		int days = 0;
		while (endDate.after(transDate)) {
			StockDailyData dailyData = stockDailyDataService.queryByCodeAndDate(account.getStockCode(), transDate);
			if (dailyData == null) { // 闭市
				transDate = DateUtil.offsetDay(transDate, 1);
				continue;
			}
			stockDailyData = dailyData;
			days++;
			if (days == 10) { // 第十个交易日，执行定投
				account.fixedPurchase(account.fixedAmount, stockDailyData);
			}
			transDate = DateUtil.offsetDay(transDate, 1);
			DateTime currentMonth = DateUtil.beginOfMonth(transDate);
			if (currentMonth.isAfter(beginOfMonth)) {
				// 如果当前月份大于开始月份，重置天数
				days = 0;
				beginOfMonth = currentMonth;
			}
			// 交易日
			if (account.getStockAmount() > 0 && stockDailyData.getOpen().doubleValue() > account.getClearLine()) {
				// 卖掉利润
//				int hand = (int) (account.getStockAmount()*account.getV2()/100);
				// 保留利润
				int hand = (int) (account.getCost() / stockDailyData.getOpen().doubleValue() / 100);
				account.setAppendTrans(0);
				stringBuilder.append("卖出前，当前账户状态：" + account.toString(stockDailyData.getOpen().doubleValue())).append("");
				String sell = account.sell(hand, stockDailyData);
				stringBuilder.append(sell);
			}
		}
		String x = "定投结束，当前账户状态：" + account.toString(stockDailyData.getOpen().doubleValue());
		return stringBuilder.append(x).append("\n").toString();
	}


	@Test
	public void test() {
		System.out.println("%04.2f".formatted(0.3));  // 输出 0.30
	}
}
