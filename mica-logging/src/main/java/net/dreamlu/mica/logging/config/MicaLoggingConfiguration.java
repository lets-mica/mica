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

package net.dreamlu.mica.logging.config;

import ch.qos.logback.classic.LoggerContext;
import net.dreamlu.mica.core.constant.MicaConstant;
import net.dreamlu.mica.core.utils.JsonUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

/**
 * logging 日志配置
 *
 * @author L.cm
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(MicaLoggingProperties.class)
public class MicaLoggingConfiguration {

	@Autowired
	public MicaLoggingConfiguration(Environment environment,
									MicaLoggingProperties loggingProperties) {
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		String appName = environment.getRequiredProperty(MicaConstant.SPRING_APP_NAME_KEY);
		String profile = environment.getRequiredProperty(MicaConstant.ACTIVE_PROFILES_PROPERTY);
		Map<String, Object> map = new HashMap<>();
		map.put("appName", appName);
		map.put("profile", profile);
		map.put("timestamp", "%date{\"yyyy-MM-dd'T'HH:mm:ss.SSSZ\"}");
		String customFields = JsonUtil.toJson(map);
		MicaLoggingProperties.Logstash logStashProperties = loggingProperties.getLogstash();
		if (loggingProperties.isUseJsonFormat()) {
			LoggingUtil.addJsonConsoleAppender(context, customFields);
		}
		if (logStashProperties.isEnabled()) {
			LoggingUtil.addLogstashTcpSocketAppender(context, customFields, logStashProperties);
		}
		if (loggingProperties.isUseJsonFormat() || logStashProperties.isEnabled()) {
			LoggingUtil.addContextListener(context, customFields, loggingProperties);
		}
	}

	@Bean
	public LoggingStartedEventListener loggingStartedEventListener(MicaLoggingProperties loggingProperties) {
		return new LoggingStartedEventListener(loggingProperties);
	}
}
