import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.sql.Types;
import java.util.Collections;

public class MybatisPlusGenerator {
	public static void main(String[] args) {
		FastAutoGenerator.create("jdbc:mysql://172.16.118.101:3306/test", "root", "123456")
				.globalConfig(builder -> {
					builder.author("kenny") // 设置作者
							.disableServiceInterface()
							.dateType(DateType.ONLY_DATE)
							.outputDir("src/main/java/"); // 指定输出目录
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
						builder.parent("com.redjujubetree.example") // 设置父包名
//								.moduleName("system") // 设置父包模块名
								.serviceImpl("service")
								.entity("model.entity")
								.pathInfo(Collections.singletonMap(OutputFile.xml, "src/main/resources/mapper")) // 设置mapperXml生成路径
				)
				.strategyConfig(builder ->
						builder.addInclude("sw_alarm_message") // 设置需要生成的表名
								.serviceBuilder()
//								.formatServiceFileName("%sService")
								.formatServiceImplFileName("%sService")
								.entityBuilder()
								.enableFileOverride()
								.logicDeleteColumnName("deleted")
								.versionColumnName("version")
								.disableSerialVersionUID()
								.enableLombok()
								//.addTablePrefix("t_", "c_") // 设置过滤表前缀
				)

				.templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
				.execute();
	}
}
