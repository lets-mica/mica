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

package net.dreamlu.mica.jetcache.config;

import com.alicp.jetcache.anno.support.GlobalCacheConfig;
import com.alicp.jetcache.support.DefaultMetricsManager;
import io.micrometer.core.instrument.MeterRegistry;
import net.dreamlu.mica.jetcache.metrics.JetCacheMonitorManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * jetcache metrics 配置
 *
 * @author L.cm
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(MeterRegistry.class)
@ConditionalOnProperty(
	prefix = JetCacheMetricsProperties.PREFIX,
	name = "enabled",
	havingValue = "true",
	matchIfMissing = true
)
@EnableConfigurationProperties(JetCacheMetricsProperties.class)
public class JetCacheMetricsConfiguration {

	@Bean
	public JetCacheMonitorManager jetCacheMonitorManager(JetCacheMetricsProperties properties,
														 GlobalCacheConfig globalCacheConfig,
														 MeterRegistry meterRegistry) {
		DefaultMetricsManager defaultMetricsManager;
		if (properties.isEnabledStatInfoLogger()) {
			defaultMetricsManager = new DefaultMetricsManager(globalCacheConfig.getStatIntervalMinutes(), TimeUnit.MINUTES, properties.isVerboseLog());
		} else {
			defaultMetricsManager = null;
		}
		return new JetCacheMonitorManager(defaultMetricsManager, meterRegistry);
	}

}
