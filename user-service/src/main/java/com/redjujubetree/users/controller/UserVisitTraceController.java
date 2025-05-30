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
		try {
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					String token = cookie.getValue();
					String name = cookie.getName();
					System.out.println("Token from cookie: " + name + " : " + token);
					if ("vId".equals(cookie.getName())) {
						userVisitTrace.setClientId(cookie.getValue());
					}
				}
			}
			userVisitTrace.setRemoteAddr(request.getRemoteAddr());
			userVisitTrace.setRemotePort(request.getRemotePort());
			userVisitTraceService.saveUserVisitTrace(userVisitTrace);
			System.out.println(JSON.toJSONString(userVisitTrace));
			return new BaseResponse(RespCodeEnum.SUCCESS.getCode(), RespCodeEnum.SUCCESS.getMessage());
		} catch(IllegalArgumentException e){
			log.warn(e.getMessage(), e);
			return new BaseResponse(RespCodeEnum.PARAM_ERROR.getCode(), RespCodeEnum.PARAM_ERROR.getMessage());
		} catch (Exception e) {
			log.error("系统异常请联系管理员处理", e);
			return new BaseResponse(RespCodeEnum.FAIL.getCode(), "系统异常请联系管理员处理");
		}
	}
}
