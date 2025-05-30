package com.redjujubetree;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.TemplateType;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.sql.Types;
import java.util.Scanner;

public class DeepAssessCodeGenerator {
	public static void main(String[] args) {
		FastAutoGenerator.create("jdbc:mysql://47.113.144.60:7500/ztbgl_batch5?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8", "root", "1234Qwer")
				.globalConfig(builder ->
					builder.author("tanjianwei") // 设置作者
							.outputDir("/Users/kenny/IdeaProjects/deep-assess/deep-admin/src/main/java") // 设置输出路径
							.dateType(DateType.ONLY_DATE)// 设置时间策略
							.disableOpenDir() // 禁止打开输出目录
				)
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
						builder.parent("com.lnsoft.deep") // 设置父包名
								.moduleName("admin") // 设置父包模块名
								.entity("domain.entity") // 设置实体类包名
				)
				.strategyConfig(builder ->
						builder.addInclude("pb_cb_plan") // 设置需要生成的表名
								.serviceBuilder()
//								.enableFileOverride() // 设置service接口覆盖文件
								.convertServiceFileName((entityName -> entityName + ConstVal.SERVICE))
								.entityBuilder()
								.enableFileOverride() // 设置实体类覆盖文件
								.controllerBuilder()
								.enableRestStyle() // 开启生成@RestController控制器
				)
				.templateConfig(builder ->
						builder.disable(TemplateType.XML)
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
