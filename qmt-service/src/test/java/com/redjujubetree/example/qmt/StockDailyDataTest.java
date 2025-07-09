package com.redjujubetree.example.qmt;

import com.alibaba.fastjson.JSON;
import com.redjujubetree.qmt.domain.bo.StockPriceBO;
import com.redjujubetree.qmt.domain.param.PriceParam;
import com.redjujubetree.qmt.service.StockDailyDataService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@SpringBootTest
public class StockDailyDataTest {

	@Resource
	private StockDailyDataService stockDailyDataService;

	@Test
	public void queryWeekData() {
		PriceParam param = new PriceParam();
		List<StockPriceBO> stockPriceBOS = stockDailyDataService.queryWeekData(param);
		System.out.println(JSON.toJSONString(stockPriceBOS));
	}
}
