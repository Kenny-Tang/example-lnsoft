package com.redjujubetree;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

public class MybatisPlusGenerator {
	public static void main(String[] args) {
		// AutoGenerator autoGenerator = new AutoGenerator(new DataSourceConfig.Builder("jdbc:mysql://172.16.118.101:3306/test", "root", "123456").build());
		AutoGenerator autoGenerator = new AutoGenerator(new DataSourceConfig.Builder("jdbc:sqlite:/Users/kenny/IdeaProjects/userDb", "", "").build());
		GlobalConfig build = new GlobalConfig.Builder()
				.author("tanjianwei")
				.disableOpenDir()
				.fileOverride()
				.outputDir("user-service/src/main/java").build();
		autoGenerator.global(build);
		PackageConfig build1 = new PackageConfig.Builder()
				.parent("com.redjujubetree")
				.entity("model.entity")
				//.pathInfo(Collections.singletonMap("xml","user-service/src/main/resources/mapper" ))
				.moduleName("users").build();
		autoGenerator.packageInfo(build1);
		// 自定义配置
		InjectionConfig injectionConfig = new InjectionConfig.Builder().build();

		autoGenerator.injection(injectionConfig);
		TemplateConfig build2 = new TemplateConfig.Builder()
				.mapper("")
				.controller("")
				//.service("", "")
				//.entity("entity.java")
				.build();
		autoGenerator.template(build2);
		StrategyConfig.Builder builder = new StrategyConfig.Builder();
		builder.entityBuilder()
				.enableLombok()
				.naming(NamingStrategy.underline_to_camel)
				.columnNaming(NamingStrategy.underline_to_camel);
		builder.addInclude("user_info").entityBuilder().mapperBuilder().build();
		StrategyConfig build3 = builder.build();
		autoGenerator.strategy(build3);
		autoGenerator.execute(new FreemarkerTemplateEngine());
	}
}
