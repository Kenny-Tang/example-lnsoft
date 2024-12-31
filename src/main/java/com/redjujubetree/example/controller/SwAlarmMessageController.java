package com.redjujubetree.example.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson2.JSON;
import com.redjujubetree.example.common.BaseResponse;
import com.redjujubetree.example.common.IdentifierGenerator;
import com.redjujubetree.example.model.dto.AlarmMessageDTO;
import com.redjujubetree.example.service.SwAlarmMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * Table to store alarm messages and service response time rules. 前端控制器
 * </p>
 *
 * @author kenny
 * @since 2024-12-24
 */
@Slf4j
@Controller
@RequestMapping("/alarm")
public class SwAlarmMessageController {

	@Resource
	private SwAlarmMessageService swAlarmMessageService;

	@RequestMapping("/store")
	public BaseResponse storeAlarmMessage(@RequestBody List<AlarmMessageDTO> alarms) {
		log.info("AlarmMessageDTO-{}",JSON.toJSONString(alarms));

		Long id = IdentifierGenerator.getDefaultGenerator().nextId();
		String jsonString = JSON.toJSONString(alarms);
		System.out.println(jsonString);
		return BaseResponse.ofSuccess();
	}

	public static void main(String[] args) {
		AlarmMessageDTO x = new AlarmMessageDTO();
		x.setAlarmMessage("http://localhost:8081/users/{{userId}}");
		x.setName("test");
		System.out.println(JSON.toJSONString(x));
	}
}
