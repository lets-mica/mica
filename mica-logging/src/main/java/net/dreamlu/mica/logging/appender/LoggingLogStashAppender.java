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
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.constant.MicaConstant;
import net.dreamlu.mica.core.utils.JsonUtil;
import net.dreamlu.mica.logging.config.MicaLoggingProperties;
import net.dreamlu.mica.logging.utils.LogStashUtil;
import net.logstash.logback.appender.LogstashTcpSocketAppender;
import net.logstash.logback.encoder.LogstashEncoder;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

/**
 * LogStash 输出 json
 *
 * @author L.cm
 */
@Slf4j
public class LoggingLogStashAppender implements ILoggingAppender {
	private static final String ASYNC_LOG_STASH_APPENDER_NAME = "ASYNC_LOG_STASH";
	private final MicaLoggingProperties properties;
	private final String customFieldsJson;

	public LoggingLogStashAppender(Environment environment,
								   MicaLoggingProperties properties) {
		this.properties = properties;
		// 1. 服务名和环境
		String appName = environment.getRequiredProperty(MicaConstant.SPRING_APP_NAME_KEY);
		String profile = environment.getRequiredProperty(MicaConstant.ACTIVE_PROFILES_PROPERTY);
		// 2. json 自定义字段
		Map<String, Object> customFields = new HashMap<>(4);
		customFields.put("appName", appName);
		customFields.put("profile", profile);
		this.customFieldsJson = JsonUtil.toJson(customFields);
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		this.start(context);
	}

	@Override
	public void start(LoggerContext context) {
		log.info("LogStash logging start.");
		reload(context);
	}

	@Override
	public void reset(LoggerContext context) {
		log.info("LogStash logging reset.");
		reload(context);
	}

	private void reload(LoggerContext context) {
		MicaLoggingProperties.Logstash logStash = properties.getLogstash();
		if (logStash.isEnabled()) {
			addLogStashTcpSocketAppender(context, customFieldsJson, logStash);
		}
	}

	/**
	 * addLogstashTcpSocketAppender.
	 *
	 * @param context            a {@link LoggerContext} object.
	 * @param customFields       a {@link String} object.
	 * @param logStashProperties a {@link net.dreamlu.mica.logging.config.MicaLoggingProperties.Logstash} object.
	 */
	private static void addLogStashTcpSocketAppender(LoggerContext context,
													String customFields,
													MicaLoggingProperties.Logstash logStashProperties) {
		// More documentation is available at: https://github.com/logstash/logstash-logback-encoder
		final LogstashTcpSocketAppender logStashAppender = new LogstashTcpSocketAppender();
		logStashAppender.addDestination(logStashProperties.getDestinations());
		logStashAppender.setContext(context);
		logStashAppender.setEncoder(logstashEncoder(customFields));
		logStashAppender.setName(ASYNC_LOG_STASH_APPENDER_NAME);
		logStashAppender.setQueueSize(logStashProperties.getQueueSize());
		logStashAppender.start();
		// 先删除，再添加
		context.getLogger(Logger.ROOT_LOGGER_NAME).detachAppender(ASYNC_LOG_STASH_APPENDER_NAME);
		context.getLogger(Logger.ROOT_LOGGER_NAME).addAppender(logStashAppender);
	}

	private static LogstashEncoder logstashEncoder(String customFields) {
		final LogstashEncoder logstashEncoder = new LogstashEncoder();
		logstashEncoder.setThrowableConverter(LogStashUtil.throwableConverter());
		logstashEncoder.setCustomFields(customFields);
		return logstashEncoder;
	}

}
