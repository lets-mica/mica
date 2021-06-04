package net.dreamlu.mica.activerecord.test;

import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import net.dreamlu.mica.activerecord.generator.CodeGenerator;

/**
 * 在数据库表有任何变动时，运行一下 main 方法，极速响应变化进行代码重构
 */
public class CodeGeneratorTest {

	public static void main(String[] args) {
		CodeGenerator generator = CodeGenerator.create()
			.url("jdbc:mysql://127.0.0.1:3306/blog")
			.username("root")
			.password("12345678")
			.basePackageName("net.dreamlu.demo")
			.outputDir(PathKit.getWebRootPath())
			.openDir() // 完成后打开目录窗口
			.build();
		// 为生成器添加类型映射，将数据库反射得到的类型映射到指定类型
//		generator.addTypeMapping(Date.class, LocalDateTime.class);
		// 设置数据库方言
		generator.setDialect(new MysqlDialect());
		// 设置是否生成链式 setter 方法
		generator.setGenerateChainSetter(false);
		// 添加不需要生成的表名
		generator.addExcludedTable("adv");
		// 设置是否在 Model 中生成 dao 对象
		generator.setGenerateDaoInModel(true);
		// 设置需要被移除的表名前缀用于生成modelName。例如表名 "osc_user"，移除前缀 "osc_"后生成的model名为 "User"而非 OscUser
		generator.setRemovedTableNamePrefixes("t_");
		// 生成
		generator.generate();
	}

}
