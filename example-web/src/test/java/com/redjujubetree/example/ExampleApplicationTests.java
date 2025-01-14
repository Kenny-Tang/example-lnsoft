package com.redjujubetree.example;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.redjujubetree.example.service.UsersService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.FileInputStream;

@SpringBootTest
public class ExampleApplicationTests {

	@Resource
	private UsersService usersService;

	@Test
	public void list() {
		usersService.list().forEach(user -> {
			System.out.println(JSON.toJSONString(user));
		});
	}

	public void test() {
	}

}
