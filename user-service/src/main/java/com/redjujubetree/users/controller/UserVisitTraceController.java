package com.redjujubetree.users.controller;

import com.alibaba.fastjson.JSON;
import com.redjujubetree.response.BaseResponse;
import com.redjujubetree.response.RespCodeEnum;
import com.redjujubetree.users.domain.entity.UserVisitTrace;
import com.redjujubetree.users.service.UserVisitTraceService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tanjianwei
 * @since 2025-05-13
 */
@Slf4j
@Setter
@RestController
@RequestMapping("/users/userVisitTrace")
public class UserVisitTraceController {

    @Resource
    private UserVisitTraceService userVisitTraceService;

	@RequestMapping
	public BaseResponse save(HttpServletRequest request, @RequestBody UserVisitTrace userVisitTrace) {
		String clientIp = (String) request.getAttribute("clientIp");

		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				String token = cookie.getValue();
				String name = cookie.getName();
				log.info("Token from cookie: {} : {}", name, token);
				if ("vId".equals(cookie.getName())) {
					userVisitTrace.setClientId(cookie.getValue());
				}
			}
		}
		if (excludeClientIdSet.contains(userVisitTrace.getClientId())
				|| "127.0.0.1".equals(userVisitTrace.getRemoteAddr()) // 本地测试时不记录访问日志
		){
			return new BaseResponse(RespCodeEnum.SUCCESS.getCode(), RespCodeEnum.SUCCESS.getMessage());
		}
		userVisitTrace.setRemoteAddr(clientIp);
		userVisitTrace.setRemotePort(request.getRemotePort());
		userVisitTraceService.saveUserVisitTrace(userVisitTrace);
		log.info("保存用户访问记录: {}", JSON.toJSONString(userVisitTrace));
		return new BaseResponse(RespCodeEnum.SUCCESS.getCode(), RespCodeEnum.SUCCESS.getMessage());
	}
	public static Set<String> excludeClientIdSet;

	static {
		excludeClientIdSet = Set.of(
				"cfc0b048baf82c459d4a2a9d407174fb",
				"2d362c1f5196fabc5de16164b1136e44", // 我的电脑
				"348b1a2bbc78407802b2f5242c6bd631",  // 我的手机
				"f8e9452a91c071574c3ad8eedc89f119", // WeChat
				"561509212ffb879b430aa7aa9a843e0b"
		);
	}
}
