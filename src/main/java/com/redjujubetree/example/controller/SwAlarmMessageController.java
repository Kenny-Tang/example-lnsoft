package com.redjujubetree.example.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson2.JSON;
import com.redjujubetree.example.common.BaseResponse;
import com.redjujubetree.example.common.IdentifierGenerator;
import com.redjujubetree.example.service.SwAlarmMessageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * <p>
 * Table to store alarm messages and service response time rules. 前端控制器
 * </p>
 *
 * @author kenny
 * @since 2024-12-24
 */
@Controller
@RequestMapping("/alarm/")
public class SwAlarmMessageController {

	@Resource
	private SwAlarmMessageService swAlarmMessageService;

	@RequestMapping("/store/")
	public BaseResponse storeAlarmMessage(@RequestBody JSONArray alarms) {
		System.out.println(JSON.toJSONString(alarms));
		Long id = IdentifierGenerator.getDefaultGenerator().nextId();
		String jsonString = JSON.toJSONString(alarms);
		System.out.println(jsonString);
		return BaseResponse.ofSuccess();
	}
}
