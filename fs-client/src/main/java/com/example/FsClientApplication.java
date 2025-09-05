package com.example;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.redjujubetree.common.mybatis.typehandler.SqliteBlobTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import top.redjujubetree.grpc.tunnel.generator.ClientIdGenerator;
import top.redjujubetree.grpc.tunnel.generator.SingleIpClientIdGenerator;

@SpringBootApplication(scanBasePackages = "com.redjujubetree.fs,com.example")
@MapperScan("com.redjujubetree.fs.mapper")
public class FsClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(FsClientApplication.class, args);
	}

	@Bean
	public ConfigurationCustomizer configurationCustomizer() {
		return configuration -> {
			configuration.getTypeHandlerRegistry().register(byte[].class, JdbcType.BLOB, SqliteBlobTypeHandler.class);
		};
	}

	@Bean
	public ClientIdGenerator clientIdGenerator() {
		return new SingleIpClientIdGenerator();
	}
}
