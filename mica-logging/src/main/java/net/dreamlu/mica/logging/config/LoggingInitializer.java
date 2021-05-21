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

import net.dreamlu.mica.auto.annotation.AutoEnvPostProcessor;
import net.dreamlu.mica.logging.utils.LoggingUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * logging 日志初始化
 *
 * @author L.cm
 */
@AutoEnvPostProcessor
public class LoggingInitializer implements EnvironmentPostProcessor, Ordered {
	public static final String LOGGING_FILE_PATH_KEY = "logging.file.path";
	public static final String LOGGING_FILE_NAME_KEY = "logging.file.name";
	public static final String MICA_LOGGING_PROPERTY_SOURCE_NAME = "micaLoggingPropertySource";

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		// 读取系统配置的日志目录，默认为项目下 logs
		String logBase = environment.getProperty(LOGGING_FILE_PATH_KEY, LoggingUtil.DEFAULT_LOG_DIR);
		// 用于 spring boot admin 中展示日志
		if (!environment.containsProperty(LOGGING_FILE_NAME_KEY)) {
			Map<String, Object> map = new HashMap<>(2);
			map.put(LOGGING_FILE_NAME_KEY, logBase + "/${spring.application.name}/" + LoggingUtil.LOG_FILE_ALL);
			MapPropertySource propertySource = new MapPropertySource(MICA_LOGGING_PROPERTY_SOURCE_NAME, map);
			environment.getPropertySources().addLast(propertySource);
		}
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

}
