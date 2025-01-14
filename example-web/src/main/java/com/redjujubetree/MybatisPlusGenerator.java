package com.redjujubetree;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

public class MybatisPlusGenerator {
	public static void main(String[] args) {
		AutoGenerator autoGenerator = new AutoGenerator(new DataSourceConfig.Builder("jdbc:mysql://172.16.118.101:3306/test", "root", "123456").build());
		GlobalConfig build = new GlobalConfig.Builder()
				.author("tanjianwei")
				.openDir(true)
				.outputDir("example-web/src/main/java").build();
		autoGenerator.global(build);
		PackageConfig build1 = new PackageConfig.Builder()
				.parent("com.redjujubetree")
				.entity("model.entity")
				.pathInfo(Collections.singletonMap("xml","example-web/src/main/resources/mapper" ))
				.moduleName("exampleg").build();
		autoGenerator.packageInfo(build1);
		// 自定义配置
		InjectionConfig injectionConfig = new InjectionConfig.Builder().build();

		autoGenerator.injection(injectionConfig);
		TemplateConfig build2 = new TemplateConfig.Builder()
				//.mapperXml("mapper.xml")
				.build();
		autoGenerator.template(build2);
		StrategyConfig.Builder builder = new StrategyConfig.Builder();
		builder.entityBuilder()
				.enableLombok()
				.naming(NamingStrategy.underline_to_camel)
				.columnNaming(NamingStrategy.underline_to_camel);
		StrategyConfig build3 = builder.addInclude("sw_alarm_message").mapperBuilder().build();
		autoGenerator.strategy(build3);
		autoGenerator.execute(new FreemarkerTemplateEngine());
	}
}
