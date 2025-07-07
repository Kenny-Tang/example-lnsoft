package com.redjujubetree.example;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.redjujubetree.qmt.domain.entity.StockDailyData;
import com.redjujubetree.qmt.domain.entity.Top500Stock;
import com.redjujubetree.qmt.mapper.Top500StockMapper;
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

	@Resource
	private Top500StockMapper top500StockMapper;
	@Test
	public void testTrade() {

		List<String> results = new ArrayList<>();
		List<Top500Stock> top500Stocks = top500StockMapper.selectList(Wrappers.lambdaQuery(Top500Stock.class));
		for (Top500Stock value : top500Stocks) {
			Account account = new Account(value,0*10000);
			log.info(value.toString());
			String strategy = strategy(account);
			results.add(strategy);
		}
		results.forEach(t -> System.out.println(t));
	}

	private String strategy(Account account) {
		StringBuilder stringBuilder = new StringBuilder();
		// 定投开始时间
		Date transDate = DateUtil.parse("2022-01-01");
		// 定投结束时间
		Date endDate = new Date() ;
		StockDailyData stockDailyData = null;
		Date beginOfMonth = DateUtil.beginOfMonth(transDate);
		int days = 0;
		double stockPrice = 0;
		while (endDate.after(transDate)) {
			StockDailyData dailyData = stockDailyDataService.queryByCodeAndDate(account.getStockCode(), transDate);
			if (dailyData == null) { // 闭市
				transDate = DateUtil.offsetDay(transDate, 1);
				continue;
			}
			stockDailyData = dailyData;
			stockPrice = stockDailyData.getOpen();
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
			if (account.getStockAmount() > 0 && stockPrice > account.getClearLine() * 100) {
				// 卖掉利润
//				int hand = (int) (account.getStockAmount()*account.getV2()/100);
				// 保留利润
				int hand = (int) (account.getCost() / stockPrice / 100);
				account.setAppendTrans(0);
				stringBuilder.append("卖出前，当前账户状态：" + account.toString(stockPrice)).append("");
				String sell = account.sell(hand, stockDailyData);
				stringBuilder.append(sell);
			}
		}
		String x = "定投结束，当前账户状态：" + account.toString(stockPrice);
		return stringBuilder.append(x).append("\n").toString();
	}


	@Test
	public void test() {
		System.out.println("%04.2f".formatted(0.3));  // 输出 0.30
	}
}
