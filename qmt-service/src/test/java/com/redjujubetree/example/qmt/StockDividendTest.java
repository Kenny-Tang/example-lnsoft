package com.redjujubetree.example.qmt;

import com.redjujubetree.qmt.domain.StockDividendDistribution;
import com.redjujubetree.qmt.domain.param.DividendYieldParam;
import com.redjujubetree.qmt.service.StockDividendService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@SpringBootTest
public class StockDividendTest {
	@Resource
	private StockDividendService stockDividendService;

	@Test
	public void testDividendWeekTrend() {
		DividendYieldParam param = new DividendYieldParam();
		List<StockDividendDistribution> stockDividendDistributions = stockDividendService.queryWeekTrend(param);
		System.out.println(stockDividendDistributions);
	}
}
