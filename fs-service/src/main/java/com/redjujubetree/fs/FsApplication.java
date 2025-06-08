package com.redjujubetree.fs;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.redjujubetree.common.mybatis.typehandler.SqliteBlobTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@MapperScan("com.redjujubetree.fs.mapper")
public class FsApplication {

	public static void main(String[] args) {
		SpringApplication.run(FsApplication.class, args);
	}

	@Bean
	public ConfigurationCustomizer configurationCustomizer() {
		return configuration -> {
			configuration.getTypeHandlerRegistry().register(byte[].class, JdbcType.BLOB, SqliteBlobTypeHandler.class);
		};
	}
}
