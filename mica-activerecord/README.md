# mica-activerecord 模块

## 功能
- `@TableName` 注解 Model 自动 Mapping 映射。
- 基于 Druid 的可执行 Sql 打印。
- 基于 Druid 的 `DruidSqlDialect`，分页 sql 优化，支持多种数据库。
- Record 的 `jackson` 处理。
- `@Tx` 注解的 JFinal ActiveRecord 事务。
- 可自定义 `ActiveRecordPluginCustomizer` Bean，实现自定义扩展。
- `CodeGenerator` 代码生成 `markdown` 格式数据字典。
- `ModelUtil` 实现 Model、Record -> Bean 转换。

## 文档
jfinal ActiveRecord 文档：https://jfinal.com/doc/5-1

## 添加依赖
### maven
```xml
<dependency>
  <groupId>net.dreamlu</groupId>
  <artifactId>mica-activerecord</artifactId>
  <version>${version}</version>
</dependency>
```

## 配置
### ActiveRecord
| 配置项 | 默认值 | 说明 |
| ----- | ------ | ------ |
| mica.activerecord.dialect | mysql | 方言，默认：mysql，注意：设置为 Druid 时采用 Ansi + druid 分页优化，支持多种数据库 |
| mica.activerecord.auto-table-scan | true | 自定表扫描 |
| mica.activerecord.model-package |  | 模型的包路径 |
| mica.activerecord.base-template-path |  | sql 模板前缀 |
| mica.activerecord.sql-templates |  | sql 模板，支持多个 |
| mica.activerecord.transaction-level |  | 事务级别，默认：不可重复读 |

### Spring database（优先）
| 配置项 | 默认值 | 说明 |
| ----- | ------ | ------ |
| spring.datasource.url |  | 数据库地址 |
| spring.datasource.username |  | 数据库用户名 |
| spring.datasource.password |  | 数据库密码 |

### Druid
| 配置项 | 默认值 | 说明 |
| ----- | ------ | ------ |
| mica.druid.url |  | 数据库地址 |
| mica.druid.username |  | 数据库用户名 |
| mica.druid.password |  | 数据库密码 |
| mica.druid.show-sql | true | 打印可执行 sql，默认为 true |
| mica.druid.connection-init-sql |  |  |
| mica.druid.connection-properties |  |  |
| mica.druid.default-transaction-isolation |  |  |
| mica.druid.driver-class |  |  |
| mica.druid.filters |  |  |
| mica.druid.initial-size | 1 |  |
| mica.druid.keep-alive |  |  |
| mica.druid.log-abandoned | false |  |
| mica.druid.max-active | 32 |  |
| mica.druid.max-pool-prepared-statement-per-connection-size | -1 |  |
| mica.druid.max-wait | -1 |  |
| mica.druid.min-evictable-idle-time-millis | 1800000 |  |
| mica.druid.min-idle | 10 |  |
| mica.druid.public-key |  |  |
| mica.druid.remove-abandoned | false |  |
| mica.druid.remove-abandoned-timeout-millis | 300000 |  |
| mica.druid.test-on-borrow | false |  |
| mica.druid.test-on-return | false |  |
| mica.druid.test-while-idle | true |  |
| mica.druid.time-between-connect-error-millis | 30000 |  |
| mica.druid.time-between-eviction-runs-millis | 60000 |  |
| mica.druid.time-between-log-stats-millis |  |  |
| mica.druid.validation-query | select 1 |  |
| mica.druid.validation-query-timeout |  |  |

## 代码生成
```java
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
```

## 示例：自定义 jFinal ActiveRecord Plugin 配置
```java
@Configuration(proxyBeanMethods = false)
public class MicaArpCustomConfiguration {

	@Bean
	public ActiveRecordPluginCustomizer activeRecordPluginCustomizer() {
		return new ActiveRecordPluginCustomizer() {
			@Override
			public void customize(ActiveRecordPlugin arp) {
				System.out.println("----------------ActiveRecordPluginCustomizer-----------------");
				arp.setDevMode(true);
			}
		};
	}

}
```

## TODO 
- 对多数据源的支持