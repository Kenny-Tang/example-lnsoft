package com.redjujubetree.ztb;

import com.redjujubetree.ztb.service.DataInitService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class ZtbTest {

	@Resource
	private DataInitService dataInitService;
	@Test
	public void test() {
		System.out.println("test");
		dataInitService.initData();
	}
}
