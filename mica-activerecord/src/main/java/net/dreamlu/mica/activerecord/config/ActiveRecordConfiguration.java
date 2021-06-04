/*
 * Copyright (c) 2019-2029, Dreamlu 卢春梦 (596392912@qq.com & www.dreamlu.net).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.dreamlu.mica.activerecord.config;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.IDataSourceProvider;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;
import com.jfinal.template.source.ClassPathSourceFactory;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.activerecord.annotation.TableName;
import net.dreamlu.mica.activerecord.datasource.SpringDataSourceProvider;
import net.dreamlu.mica.activerecord.logger.SqlLogFilter;
import net.dreamlu.mica.activerecord.tx.ActiveRecordTxAspect;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * jfinal activerecord 配置
 *
 * @author L.cm
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({
	MicaDruidProperties.class,
	MicaActiveRecordProperties.class
})
public class ActiveRecordConfiguration {
	@Value("${spring.datasource.url:}")
	private String url;
	@Value("${spring.datasource.username:}")
	private String username;
	@Value("${spring.datasource.password:}")
	private String password;

	@Bean
	public ActiveRecordTxAspect activeRecordTxAspect() {
		return new ActiveRecordTxAspect();
	}

	@Bean
	@ConditionalOnBean(DataSource.class)
	public IDataSourceProvider springDataSourceProvider(DataSource dataSource) {
		return new SpringDataSourceProvider(dataSource);
	}

	@Bean
	@ConditionalOnMissingBean(IDataSourceProvider.class)
	public IDataSourceProvider druidDataSourceProvider(MicaDruidProperties properties) {
		// 数据库信息
		String dbUrl = StrKit.isBlank(url) ? properties.getUrl() : url;
		String dbUserName = StrKit.isBlank(username) ? properties.getUsername() : username;
		String dbPassWord = StrKit.isBlank(password) ? properties.getPassword() : password;
		Assert.hasText(dbUrl, "spring.datasource.url or mica.druid.url is blank.");
		Assert.hasText(dbUserName, "spring.datasource.username or mica.druid.username is blank.");
		Assert.hasText(dbUserName, "spring.datasource.password or mica.druid.password is blank.");
		// Druid 连接池配置
		DruidPlugin druidPlugin = new DruidPlugin(dbUrl, dbUserName, dbPassWord)
			.setDriverClass(properties.getDriverClass())
			.setInitialSize(properties.getInitialSize())
			.setMinIdle(properties.getMinIdle())
			.setMaxActive(properties.getMaxActive())
			.setMaxWait(properties.getMaxWait())
			.setTimeBetweenEvictionRunsMillis(properties.getTimeBetweenEvictionRunsMillis())
			.setTimeBetweenConnectErrorMillis(properties.getTimeBetweenConnectErrorMillis())
			.setMinEvictableIdleTimeMillis(properties.getMinEvictableIdleTimeMillis())
			.setValidationQuery(properties.getValidationQuery())
			.setConnectionProperties(properties.getConnectionProperties())
			.setTestOnBorrow(properties.isTestOnBorrow())
			.setTestOnReturn(properties.isTestOnReturn())
			.setTestWhileIdle(properties.isTestWhileIdle())
			.setRemoveAbandoned(properties.isRemoveAbandoned())
			.setRemoveAbandonedTimeoutMillis(properties.getRemoveAbandonedTimeoutMillis())
			.setLogAbandoned(properties.isLogAbandoned())
			.setMaxPoolPreparedStatementPerConnectionSize(properties.getMaxPoolPreparedStatementPerConnectionSize())
			.setFilters(properties.getFilters());
		druidPlugin.setConnectionInitSql(properties.getConnectionInitSql());
		Integer defaultTransactionIsolation = properties.getDefaultTransactionIsolation();
		if (defaultTransactionIsolation != null) {
			druidPlugin.setDefaultTransactionIsolation(defaultTransactionIsolation);
		}
		Integer validationQueryTimeout = properties.getValidationQueryTimeout();
		if (validationQueryTimeout != null) {
			druidPlugin.setValidationQueryTimeout(validationQueryTimeout);
		}
		Integer timeBetweenLogStatsMillis = properties.getTimeBetweenLogStatsMillis();
		if (timeBetweenLogStatsMillis != null) {
			druidPlugin.setTimeBetweenLogStatsMillis(timeBetweenLogStatsMillis);
		}
		Boolean keepAlive = properties.getKeepAlive();
		if (keepAlive != null) {
			druidPlugin.setKeepAlive(properties.getKeepAlive());
		}
		// 打印可执行sql
		if (properties.isShowSql()) {
			druidPlugin.addFilter(new SqlLogFilter(properties));
		}
		druidPlugin.start();
		return druidPlugin;
	}

	@Bean
	public ActiveRecordPlugin activeRecordPlugin(IDataSourceProvider dataSourceProvider,
												 Environment environment,
												 ResourceLoader resourceLoader,
												 MicaActiveRecordProperties properties,
												 ObjectProvider<List<ActiveRecordPluginCustomizer>> arpCustomizerProvider) {
		String modelPackage = properties.getModelPackage();
		Assert.hasText(modelPackage, "mica.activerecord.model-package is blank.");
		ActiveRecordPlugin arp = new ActiveRecordPlugin(dataSourceProvider);
		arp.setDialect(properties.getDialect().getDialect());
		arp.setTransactionLevel(properties.getTransactionLevel().getLevel());
		arp.setShowSql(false);
		// 加载 sql 模板
		String[] sqlTemplates = properties.getSqlTemplates();
		if (StrKit.notBlank(sqlTemplates)) {
			Engine engine = arp.getEngine();
			engine.setBaseTemplatePath(properties.getBaseTemplatePath());
			engine.setSourceFactory(new ClassPathSourceFactory());
			Arrays.stream(sqlTemplates).forEach(arp::addSqlTemplate);
		}
		// 扫描和添加表映射
		if (properties.isAutoTableScan()) {
			scanTable(arp, environment, resourceLoader, modelPackage);
		}
		// arp 自定义配置 bean
		arpCustomizerProvider.ifAvailable(customizers -> customizers.forEach(customizer -> customizer.customize(arp)));
		arp.start();
		return arp;
	}

	@SuppressWarnings("unchecked")
	private static void scanTable(ActiveRecordPlugin arp,
								  Environment environment,
								  ResourceLoader resourceLoader,
								  String modelPackage) {
		ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
		provider.addIncludeFilter(new AnnotationTypeFilter(TableName.class));
		provider.setEnvironment(environment);
		provider.setResourceLoader(resourceLoader);
		Set<BeanDefinition> definitionSet = provider.findCandidateComponents(modelPackage);
		definitionSet.forEach(beanDefinition -> {
			String className = beanDefinition.getBeanClassName();
			Class<?> modelClass = ClassUtils.resolveClassName(Objects.requireNonNull(className), ClassUtils.getDefaultClassLoader());
			TableName table = AnnotationUtils.findAnnotation(modelClass, TableName.class);
			String tableName = Objects.requireNonNull(table).value();
			String primaryKey = table.primaryKey();
			String modelClassName = ClassUtils.getShortName(modelClass);
			log.debug("ActiveRecordPlugin mapping table:{} primaryKey:{} modelClass:{}", tableName, primaryKey, modelClassName);
			arp.addMapping(tableName, primaryKey, (Class<? extends Model<?>>) modelClass);
		});
	}

}
