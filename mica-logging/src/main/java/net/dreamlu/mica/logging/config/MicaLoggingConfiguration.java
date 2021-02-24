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
import org.springframework.boot.logging.LoggingSystemProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

/**
 * logging 日志配置
 *
 * @author L.cm
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(MicaLoggingProperties.class)
public class MicaLoggingConfiguration {

	@Autowired
	public MicaLoggingConfiguration(Environment environment,
									MicaLoggingProperties loggingProperties) {
		// 1. 服务名和环境和日志目录
		String appName = environment.getRequiredProperty(MicaConstant.SPRING_APP_NAME_KEY);
		String profile = environment.getRequiredProperty(MicaConstant.ACTIVE_PROFILES_PROPERTY);
		// 2. 文件日志格式
		String fileLogPattern = environment.resolvePlaceholders(LoggingUtil.DEFAULT_FILE_LOG_PATTERN);
		System.setProperty(LoggingSystemProperties.FILE_LOG_PATTERN, fileLogPattern);
		// 3. 生成日志文件的文件
		String logDir = environment.getProperty("logging.file.path", LoggingUtil.DEFAULT_LOG_DIR);
		String logFile = logDir + '/' + appName + "/all.log";
		String logErrorFile = logDir + '/' + appName + "/error.log";
		// 4. logStash 配置
		MicaLoggingProperties.Logstash logStashProperties = loggingProperties.getLogstash();
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		// 4. json 自定义字段
		Map<String, Object> customFields = new HashMap<>();
		customFields.put("appName", appName);
		customFields.put("profile", profile);
		String customFieldsJson = JsonUtil.toJson(customFields);
		// 是否采用 json 格式化
		boolean useJsonFormat = loggingProperties.isUseJsonFormat();
		if (logStashProperties.isEnabled()) {
			LoggingUtil.addLogStashTcpSocketAppender(context, customFieldsJson, logStashProperties);
		} else {
			LoggingUtil.addFileAppender(context, logFile, logErrorFile, useJsonFormat, customFieldsJson);
		}
		if (useJsonFormat || logStashProperties.isEnabled()) {
			LoggingUtil.addContextListener(context, logFile, logErrorFile, customFieldsJson, loggingProperties);
		}
	}

	@Bean
	public LoggingStartedEventListener loggingStartedEventListener(MicaLoggingProperties loggingProperties) {
		return new LoggingStartedEventListener(loggingProperties);
	}
}
