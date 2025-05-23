package com.lnsoft.cloud.ztbgl;

import com.redjujubetree.ztb.service.DataInitService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication

@MapperScan("com.lnsoft.cloud.ztbgl.mapper")
public class ZtbApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(ZtbApplication.class, args);
		ctx.getBean(DataInitService.class).initData();
	}


}