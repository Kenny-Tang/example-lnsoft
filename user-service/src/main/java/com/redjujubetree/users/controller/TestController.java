package com.redjujubetree.users.controller;

import com.alibaba.fastjson2.JSON;
import com.redjujubetree.users.model.entity.UserInfo;
import com.redjujubetree.users.service.impl.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {

    @Autowired
    private UserInfoService userInfoService;

    @ResponseBody
    @RequestMapping("/hello/{userId}" )
    public UserInfo hello(@PathVariable Long userId) {
        log.info("hello userId={}", userId);
        UserInfo one = userInfoService.getById(userId);
        log.info("result userInfo = {}", JSON.toJSONString(one));
        System.out.println("enter the hello method");
        return one;
    }

    public UserInfoService getUserInfoService() {
        return userInfoService;
    }

    public void setUserInfoService(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }
}
