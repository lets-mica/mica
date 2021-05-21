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

package net.dreamlu.mica.metrics.druid;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.jdbc.DataSourceUnwrapper;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadataProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * DruidDataSourceMetadata Provide
 *
 * @author L.cm
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(DruidDataSource.class)
public class DruidMetricsConfiguration {
	private static final String DATASOURCE_SUFFIX = "dataSource";

	@Bean
	public DataSourcePoolMetadataProvider druidDataSourceMetadataProvider() {
		return (dataSource) -> {
			DruidDataSource druidDataSource = DataSourceUnwrapper.unwrap(dataSource, DruidDataSource.class);
			if (druidDataSource != null) {
				return new DruidDataSourcePoolMetadata(druidDataSource);
			}
			return null;
		};
	}

	@Bean
	@ConditionalOnMissingBean
	public StatFilter statFilter() {
		return new StatFilter();
	}

	@Bean
	public DruidMetrics druidMetrics(ObjectProvider<Map<String, DataSource>> dataSourcesProvider) {
		Map<String, DataSource> dataSourceMap = dataSourcesProvider.getIfAvailable(HashMap::new);
		Map<String, DruidDataSource> druidDataSourceMap = new HashMap<>(2);
		dataSourceMap.forEach((name, dataSource) -> {
			// 保证连接池数据和 DataSourcePoolMetadataProvider 的一致
			druidDataSourceMap.put(getDataSourceName(name), DataSourceUnwrapper.unwrap(dataSource, DruidDataSource.class));
		});
		return new DruidMetrics(druidDataSourceMap);
	}

	/**
	 * Get the name of a DataSource based on its {@code beanName}.
	 *
	 * @param beanName the name of the data source bean
	 * @return a name for the given data source
	 */
	private static String getDataSourceName(String beanName) {
		if (beanName.length() > DATASOURCE_SUFFIX.length()
			&& StringUtils.endsWithIgnoreCase(beanName, DATASOURCE_SUFFIX)) {
			return beanName.substring(0, beanName.length() - DATASOURCE_SUFFIX.length());
		}
		return beanName;
	}

}
