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

import com.alicp.jetcache.anno.support.DefaultSpringKeyConvertorParser;
import com.alicp.jetcache.anno.support.GlobalCacheConfig;
import com.alicp.jetcache.anno.support.SpringConfigProvider;
import com.alicp.jetcache.support.StatInfo;
import com.alicp.jetcache.support.StatInfoLogger;
import net.dreamlu.mica.jetcache.jackson.JacksonKeyConvertor;
import net.dreamlu.mica.jetcache.metrics.JetCacheMonitorManager;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

/**
 * jetcache 配置
 *
 * @author L.cm
 */
@Configuration(proxyBeanMethods = false)
public class JetCacheConfiguration {

	@Bean("jacksonKeyConvertor")
	public JacksonKeyConvertor JacksonKeyConvertor() {
		return new JacksonKeyConvertor();
	}

	@Bean
	public SpringConfigProvider springConfigProvider(ApplicationContext applicationContext) {
		DefaultSpringKeyConvertorParser convertorParser = new DefaultSpringKeyConvertorParser();
		convertorParser.setApplicationContext(applicationContext);
		SpringConfigProvider springConfigProvider = new SpringConfigProvider();
		springConfigProvider.setKeyConvertorParser(convertorParser);
		return springConfigProvider;
	}

	@Bean
	public JetCacheMonitorManager jetCacheMonitorManager(GlobalCacheConfig globalCacheConfig,
														 ObjectProvider<Consumer<StatInfo>> metricsProvide) {
		Consumer<StatInfo> metricsCallback = metricsProvide.getIfAvailable(() -> new StatInfoLogger(false));
		return new JetCacheMonitorManager(globalCacheConfig, metricsCallback);
	}

}
