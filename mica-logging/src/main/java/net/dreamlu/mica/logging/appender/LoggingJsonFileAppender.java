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

package net.dreamlu.mica.logging.appender;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.constant.MicaConstant;
import net.dreamlu.mica.core.utils.CharPool;
import net.dreamlu.mica.core.utils.JsonUtil;
import net.dreamlu.mica.logging.config.MicaLoggingProperties;
import net.dreamlu.mica.logging.utils.LogStashUtil;
import net.dreamlu.mica.logging.utils.LoggingUtil;
import net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder;
import org.slf4j.LoggerFactory;
import org.springframework.boot.logging.LoggingSystemProperties;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

/**
 * json 日志输出，json 日志只输出 all.log
 *
 * @author L.cm
 */
@Slf4j
public class LoggingJsonFileAppender implements ILoggingAppender {
	private final MicaLoggingProperties properties;
	private final String logAllFile;
	private final String customFieldsJson;

	public LoggingJsonFileAppender(Environment environment,
								   MicaLoggingProperties properties) {
		this.properties = properties;
		// 1. 服务名和环境和日志目录
		String appName = environment.getRequiredProperty(MicaConstant.SPRING_APP_NAME_KEY);
		String profile = environment.getRequiredProperty(MicaConstant.ACTIVE_PROFILES_PROPERTY);
		// 2. 文件日志格式
		String fileLogPattern = environment.resolvePlaceholders(LoggingUtil.DEFAULT_FILE_LOG_PATTERN);
		System.setProperty(LoggingSystemProperties.FILE_LOG_PATTERN, fileLogPattern);
		// 3. 生成日志文件的文件
		String logDir = environment.getProperty("logging.file.path", LoggingUtil.DEFAULT_LOG_DIR);
		this.logAllFile = logDir + CharPool.SLASH + appName + CharPool.SLASH + LoggingUtil.LOG_FILE_ALL;
		// 4. json 自定义字段
		Map<String, Object> customFields = new HashMap<>(4);
		customFields.put("appName", appName);
		customFields.put("profile", profile);
		this.customFieldsJson = JsonUtil.toJson(customFields);
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		this.start(context);
	}

	@Override
	public void start(LoggerContext context) {
		log.info("JsonFile logging start.");
		reload(context);
	}

	@Override
	public void reset(LoggerContext context) {
		log.info("JsonFile logging start.");
		reload(context);
	}

	private void reload(LoggerContext context) {
		MicaLoggingProperties.Files files = properties.getFiles();
		if (files.isEnabled() && files.isUseJsonFormat()) {
			addAllFileAppender(context, logAllFile, customFieldsJson);
		}
	}

	/**
	 * <p>addJsonConsoleAppender.</p>
	 *
	 * @param context      a {@link LoggerContext} object.
	 * @param customFields a {@link String} object.
	 */
	private static void addAllFileAppender(LoggerContext context,
										   String logFile,
										   String customFields) {
		// More documentation is available at: https://github.com/logstash/logstash-logback-encoder
		final RollingFileAppender<ILoggingEvent> allFileAppender = new RollingFileAppender<>();
		allFileAppender.setContext(context);
		allFileAppender.setEncoder(compositeJsonEncoder(context, customFields));
		allFileAppender.setName(LoggingUtil.FILE_APPENDER_NAME);
		allFileAppender.setFile(logFile);
		allFileAppender.setRollingPolicy(LoggingUtil.rollingPolicy(context, allFileAppender, logFile));
		allFileAppender.start();
		// 先删除，再添加
		context.getLogger(Logger.ROOT_LOGGER_NAME).detachAppender(LoggingUtil.FILE_APPENDER_NAME);
		context.getLogger(Logger.ROOT_LOGGER_NAME).addAppender(allFileAppender);
	}

	private static LoggingEventCompositeJsonEncoder compositeJsonEncoder(LoggerContext context,
																		 String customFields) {
		final LoggingEventCompositeJsonEncoder compositeJsonEncoder = new LoggingEventCompositeJsonEncoder();
		compositeJsonEncoder.setContext(context);
		compositeJsonEncoder.setProviders(LogStashUtil.jsonProviders(context, customFields));
		compositeJsonEncoder.start();
		return compositeJsonEncoder;
	}

}
