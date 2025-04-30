package com.redjujubetree.example;

import com.alibaba.fastjson2.JSON;
import com.redjujubetree.users.model.entity.UserInfo;
import com.redjujubetree.users.service.impl.UserInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExampleApplicationTests {
	@Autowired
	private UserInfoService userInfoService;

	public void InitDbInfo() {
		UserInfo userInfo = new UserInfo();

	}

	public void createTestUsers() {
		UserInfo userInfo = new UserInfo();
	}

	@Test
	public void test() {
		UserInfo byId = userInfoService.getById(1001L);
		System.out.println(JSON.toJSONString(byId));
	}

	public UserInfoService getUserInfoService() {
		return userInfoService;
	}

	public void setUserInfoService(UserInfoService userInfoService) {
		this.userInfoService = userInfoService;
	}
}
