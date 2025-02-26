package com.redjujubetree.users.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSON;
import com.redjujubetree.users.model.dto.UserDTO;
import com.redjujubetree.users.model.entity.UserInfo;
import com.redjujubetree.users.service.impl.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/info")
public class TestController {

    @Autowired
    private UserInfoService userInfoService;

    @ResponseBody
    @RequestMapping("/{userId}" )
    public UserDTO hello(@PathVariable Long userId) {
        log.info("hello userId={}", userId);
        UserInfo user = userInfoService.getById(userId);
        UserDTO target = null;
        if (Objects.nonNull(user)) {
            target = new UserDTO();
            BeanUtil.copyProperties(user, target);
            target.setId(user.getId() == null ? null : user.getId()+"");
            target.setCreateTime(user.getCreateTime() == null ? null : DatePattern.NORM_DATETIME_FORMAT.format(DateUtil.date(user.getCreateTime())));
            target.setUpdateTime(user.getUpdateTime() == null ? null : DatePattern.NORM_DATETIME_FORMAT.format(DateUtil.date(user.getUpdateTime())));
        }
        log.info("result userInfo = {}", JSON.toJSONString(user));
        return target;
    }

    public UserInfoService getUserInfoService() {
        return userInfoService;
    }

    public void setUserInfoService(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }
}
