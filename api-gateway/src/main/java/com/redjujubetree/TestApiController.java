package com.redjujubetree;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestApiController {

	@GetMapping("/gateway")
	public Object getway() {
		log.info("getway");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("api", "getway");
		return jsonObject;
	}
}
