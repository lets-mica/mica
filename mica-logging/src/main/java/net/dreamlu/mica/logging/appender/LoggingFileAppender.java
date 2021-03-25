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

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.filter.ThresholdFilter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.rolling.RollingFileAppender;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.constant.MicaConstant;
import net.dreamlu.mica.core.utils.CharPool;
import net.dreamlu.mica.core.utils.SystemUtil;
import net.dreamlu.mica.logging.config.MicaLoggingProperties;
import net.dreamlu.mica.logging.utils.LoggingUtil;
import org.slf4j.LoggerFactory;
import org.springframework.boot.logging.LoggingSystemProperties;
import org.springframework.boot.logging.logback.LogbackLoggingSystemProperties;
import org.springframework.core.env.Environment;

import java.nio.charset.Charset;

/**
 * 纯文件日志输出，all.log 和 error.log
 *
 * @author L.cm
 */
@Slf4j
public class LoggingFileAppender implements ILoggingAppender {
	private final MicaLoggingProperties properties;
	private final String logAllFile;
	private final String logErrorFile;

	public LoggingFileAppender(Environment environment,
							   MicaLoggingProperties properties) {
		this.properties = properties;
		// 1. 服务名和日志目录
		String appName = environment.getRequiredProperty(MicaConstant.SPRING_APP_NAME_KEY);
		// 2. 文件日志格式
		String fileLogPattern = environment.resolvePlaceholders(LoggingUtil.DEFAULT_FILE_LOG_PATTERN);
		System.setProperty(LoggingSystemProperties.FILE_LOG_PATTERN, fileLogPattern);
		// 3. 生成日志文件的文件
		String logDir = environment.getProperty("logging.file.path", LoggingUtil.DEFAULT_LOG_DIR);
		this.logAllFile = logDir + CharPool.SLASH + appName + CharPool.SLASH + LoggingUtil.LOG_FILE_ALL;
		this.logErrorFile = logDir + CharPool.SLASH + appName + CharPool.SLASH + LoggingUtil.LOG_FILE_ERROR;
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		this.start(context);
	}

	@Override
	public void start(LoggerContext context) {
		log.info("File logging start.");
		reload(context);
	}

	@Override
	public void reset(LoggerContext context) {
		log.info("File logging reset.");
		reload(context);
	}

	private void reload(LoggerContext context) {
		MicaLoggingProperties.Files files = properties.getFiles();
		if (files.isEnabled() && !files.isUseJsonFormat()) {
			addAllFileAppender(context, logAllFile);
			addErrorFileAppender(context, logErrorFile);
		}
	}

	/**
	 * addAllFileAppender
	 *
	 * @param context a {@link LoggerContext} object.
	 * @param logFile a {@link String} object.
	 */
	private static void addAllFileAppender(LoggerContext context, String logFile) {
		RollingFileAppender<ILoggingEvent> allFileAppender = new RollingFileAppender<>();
		allFileAppender.setContext(context);
		allFileAppender.setEncoder(patternLayoutEncoder(context));
		allFileAppender.setName(LoggingUtil.FILE_APPENDER_NAME);
		allFileAppender.setFile(logFile);
		allFileAppender.setRollingPolicy(LoggingUtil.rollingPolicy(context, allFileAppender, logFile));
		allFileAppender.start();
		context.getLogger(Logger.ROOT_LOGGER_NAME).detachAppender(LoggingUtil.FILE_APPENDER_NAME);
		context.getLogger(Logger.ROOT_LOGGER_NAME).addAppender(allFileAppender);
	}

	/**
	 * addErrorFileAppender
	 *
	 * @param context      a {@link LoggerContext} object.
	 * @param logErrorFile a {@link String} object.
	 */
	private static void addErrorFileAppender(LoggerContext context, String logErrorFile) {
		// More documentation is available at: https://github.com/logstash/logstash-logback-encoder
		final RollingFileAppender<ILoggingEvent> errorFileAppender = new RollingFileAppender<>();
		errorFileAppender.setContext(context);
		errorFileAppender.addFilter(errorLevelFilter(context));
		errorFileAppender.setEncoder(patternLayoutEncoder(context));
		errorFileAppender.setName(LoggingUtil.FILE_ERROR_APPENDER_NAME);
		errorFileAppender.setFile(logErrorFile);
		errorFileAppender.setRollingPolicy(LoggingUtil.rollingPolicy(context, errorFileAppender, logErrorFile));
		errorFileAppender.start();
		context.getLogger(Logger.ROOT_LOGGER_NAME).detachAppender(LoggingUtil.FILE_ERROR_APPENDER_NAME);
		context.getLogger(Logger.ROOT_LOGGER_NAME).addAppender(errorFileAppender);
	}

	private static Encoder<ILoggingEvent> patternLayoutEncoder(LoggerContext context) {
		final PatternLayoutEncoder encoder = new PatternLayoutEncoder();
		encoder.setContext(context);
		encoder.setPattern(SystemUtil.getProp(LoggingSystemProperties.FILE_LOG_PATTERN));
		String charsetName = SystemUtil.getProp(LogbackLoggingSystemProperties.FILE_LOG_CHARSET, "default");
		encoder.setCharset(Charset.forName(charsetName));
		encoder.start();
		return encoder;
	}

	private static ThresholdFilter errorLevelFilter(LoggerContext context) {
		final ThresholdFilter filter = new ThresholdFilter();
		filter.setContext(context);
		filter.setLevel(Level.ERROR.levelStr);
		filter.start();
		return filter;
	}

}
