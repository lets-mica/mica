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

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.jdbc.DataSourceUnwrapper;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadataProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * DruidDataSourceMetadata Provide
 *
 * @author L.cm
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(DruidDataSource.class)
public class DruidDataSourceMetadataProviderConfiguration {

	@Bean
	public DataSourcePoolMetadataProvider hikariPoolDataSourceMetadataProvider() {
		return (dataSource) -> {
			DruidDataSource druidDataSource = DataSourceUnwrapper.unwrap(dataSource, DruidDataSource.class);
			if (druidDataSource != null) {
				return new DruidDataSourcePoolMetadata(druidDataSource);
			}
			return null;
		};
	}

}
