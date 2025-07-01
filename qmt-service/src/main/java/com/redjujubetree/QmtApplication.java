package com.redjujubetree;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.redjujubetree.qmt.mapper")
public class QmtApplication {

	public static void main(String[] args) {
		SpringApplication.run(QmtApplication.class, args);
	}

}
