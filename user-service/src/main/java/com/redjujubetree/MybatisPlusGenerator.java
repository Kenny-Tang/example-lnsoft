package com.redjujubetree;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.TemplateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.sql.Types;
import java.util.Scanner;

public class MybatisPlusGenerator {
	public static void main(String[] args) {
		FastAutoGenerator.create("jdbc:mysql://47.113.144.60:7500/xiaozaoshu?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8", "root", "1234Qwer")
				.globalConfig(builder -> {
					builder.author("tanjianwei") // 设置作者
							.outputDir(System.getProperty("user.dir") + "/user-service/src/main/java") // 设置输出路径
							.fileOverride(); // 设置文件覆盖
				})
				.dataSourceConfig(builder ->
						builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
							int typeCode = metaInfo.getJdbcType().TYPE_CODE;
							if (typeCode == Types.SMALLINT) {
								// 自定义类型转换
								return DbColumnType.INTEGER;
							}
							return typeRegistry.getColumnType(metaInfo);
						})
				)
				.packageConfig(builder ->
						builder.parent("com.redjujubetree") // 设置父包名
								.moduleName("users") // 设置父包模块名
								.entity("model.entity") // 设置实体类包名
				)
				.strategyConfig(builder ->
						builder.addInclude(scanner("表名")) // 设置需要生成的表名
								.serviceBuilder() // 设置不生成service接口
				)
				.templateConfig(builder ->
						builder.disable(TemplateType.CONTROLLER)
								.disable(TemplateType.SERVICE)
								.disable(TemplateType.SERVICE_IMPL)
								.disable(TemplateType.XML)
								.disable(TemplateType.MAPPER)
				)
				.templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
				.execute();
	}
	public static String scanner(String tip) {
		Scanner scanner = new Scanner(System.in);
		StringBuilder help = new StringBuilder();
		help.append("请输入" + tip + "：");
		System.out.println(help.toString());
		if (scanner.hasNext()) {
			String ipt = scanner.next();
			if (StringUtils.isNotEmpty(ipt)) {
				return ipt;
			}
		}
		throw new MybatisPlusException("请输入正确的" + tip + "！");
	}
}
