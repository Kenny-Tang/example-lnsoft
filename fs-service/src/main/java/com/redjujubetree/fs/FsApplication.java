package com.redjujubetree.fs;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.redjujubetree.common.mybatis.typehandler.SqliteBlobTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication(scanBasePackages = "com.redjujubetree.fs,com.example")
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

//	@Bean
//	public WebMvcConfigurer webMvcConfigurer(AsyncTaskExecutor taskExecutor) {
//		return new WebMvcConfigurer() {
//			@Override
//			public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
//				configurer.setTaskExecutor(taskExecutor);
//				configurer.setDefaultTimeout(-1); // 不超时
//			}
//		};
//	}

	@Bean
	public AsyncTaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10);
		executor.setMaxPoolSize(50);
		executor.setQueueCapacity(100);
		executor.setThreadNamePrefix("async-download-");
		executor.initialize();
		return executor;
	}
}
