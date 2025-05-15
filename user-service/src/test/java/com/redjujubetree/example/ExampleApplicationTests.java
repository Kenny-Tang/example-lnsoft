package com.redjujubetree.example;

import com.alibaba.fastjson2.JSON;
import com.redjujubetree.users.domain.entity.UserInfo;
import com.redjujubetree.users.service.UserInfoService;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Setter
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
	public void save() {
		UserInfo userInfo = new UserInfo();
		userInfoService.saveUserInfo(userInfo);
	}
	@Test
	public void getByIdUserInfo() {
		System.out.println("this is a test message");
		UserInfo byId = userInfoService.getById(1001L);
		System.out.println(JSON.toJSONString(byId));
	}

	@Test
	public void updateUserInfo() {
		System.out.println("this is a test message");
		UserInfo byId = userInfoService.getById(1001L);
		userInfoService.updateById(byId);
		System.out.println(JSON.toJSONString(byId));
	}

}
