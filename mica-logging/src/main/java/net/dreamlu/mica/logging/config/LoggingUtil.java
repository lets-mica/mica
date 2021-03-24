/*
 * Copyright 2016-2020 the original author or authors from the JHipster project.
 *
 * This file is part of the JHipster project, see https://www.jhipster.tech/
 * for more information.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.dreamlu.mica.logging.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.filter.ThresholdFilter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.RollingPolicy;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.util.FileSize;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.utils.SystemUtil;
import net.logstash.logback.appender.LogstashTcpSocketAppender;
import net.logstash.logback.composite.ContextJsonProvider;
import net.logstash.logback.composite.GlobalCustomFieldsJsonProvider;
import net.logstash.logback.composite.loggingevent.*;
import net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder;
import net.logstash.logback.encoder.LogstashEncoder;
import net.logstash.logback.stacktrace.ShortenedThrowableConverter;
import org.slf4j.LoggerFactory;
import org.springframework.boot.logging.LoggingSystemProperties;
import org.springframework.boot.logging.logback.LogbackLoggingSystemProperties;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * 参考自 jhipster
 * <p>
 * Utility methods to add appenders to a {@link LoggerContext}.
 *
 * @author jhipster
 * @author L.cm
 */
@Slf4j
@UtilityClass
public class LoggingUtil {
	public static final String DEFAULT_LOG_DIR = "logs";
	public static final String CONSOLE_APPENDER_NAME = "CONSOLE";
	public static final String FILE_APPENDER_NAME = "FILE";
	public static final String FILE_ERROR_APPENDER_NAME = "FILE_ERROR";
	public static final String DEFAULT_FILE_LOG_PATTERN = "${FILE_LOG_PATTERN:%d{${LOG_DATEFORMAT_PATTERN:yyyy-MM-dd HH:mm:ss.SSS}} ${LOG_LEVEL_PATTERN:%5p} ${PID:} --- [%t] %-40.40logger{39} : %m%n${LOG_EXCEPTION_CONVERSION_WORD:%wEx}}";
	private static final String ASYNC_LOG_STASH_APPENDER_NAME = "ASYNC_LOG_STASH";

	/**
	 * detach appender
	 *
	 * @param name appender name
	 */
	public static void detachAppender(String name) {
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		context.getLogger(Logger.ROOT_LOGGER_NAME).detachAppender(name);
	}

	/**
	 * <p>addJsonConsoleAppender.</p>
	 *
	 * @param context      a {@link LoggerContext} object.
	 * @param customFields a {@link String} object.
	 */
	public static void addJsonConsoleAppender(LoggerContext context,
											  String customFields) {
		log.info("Initializing Console loggingProperties");
		// More documentation is available at: https://github.com/logstash/logstash-logback-encoder
		ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<>();
		consoleAppender.setContext(context);
		consoleAppender.setEncoder(compositeJsonEncoder(context, customFields));
		consoleAppender.setName(CONSOLE_APPENDER_NAME);
		consoleAppender.start();
		context.getLogger(Logger.ROOT_LOGGER_NAME).detachAppender(CONSOLE_APPENDER_NAME);
		context.getLogger(Logger.ROOT_LOGGER_NAME).addAppender(consoleAppender);
	}

	/**
	 * 添加 FileAppender
	 *
	 * @param context       LoggerContext
	 * @param logFile       logFile
	 * @param logErrorFile  logErrorFile
	 * @param useJsonFormat useJsonFormat
	 * @param customFields  customFields
	 */
	public static void addFileAppender(LoggerContext context,
									   String logFile,
									   String logErrorFile,
									   boolean useJsonFormat,
									   String customFields) {
		addAllFileAppender(context, logFile, useJsonFormat, customFields);
		if (!useJsonFormat) {
			addErrorFileAppender(context, logErrorFile);
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
										   boolean useJsonFormat,
										   String customFields) {
		log.info("Initializing {} file loggingProperties", logFile);
		// More documentation is available at: https://github.com/logstash/logstash-logback-encoder
		RollingFileAppender<ILoggingEvent> allFileAppender = new RollingFileAppender<>();
		allFileAppender.setContext(context);
		if (useJsonFormat) {
			allFileAppender.setEncoder(compositeJsonEncoder(context, customFields));
		} else {
			allFileAppender.setEncoder(patternLayoutEncoder(context));
		}
		allFileAppender.setName(FILE_APPENDER_NAME);
		allFileAppender.setFile(logFile);
		allFileAppender.setRollingPolicy(rollingPolicy(context, allFileAppender, logFile));
		allFileAppender.start();
		context.getLogger(Logger.ROOT_LOGGER_NAME).detachAppender(FILE_APPENDER_NAME);
		context.getLogger(Logger.ROOT_LOGGER_NAME).addAppender(allFileAppender);
	}

	/**
	 * <p>addJsonConsoleAppender.</p>
	 *
	 * @param context      a {@link LoggerContext} object.
	 * @param logErrorFile a {@link String} object.
	 */
	private static void addErrorFileAppender(LoggerContext context, String logErrorFile) {
		log.info("Initializing {} file loggingProperties", logErrorFile);
		// More documentation is available at: https://github.com/logstash/logstash-logback-encoder
		RollingFileAppender<ILoggingEvent> errorFileAppender = new RollingFileAppender<>();
		errorFileAppender.setContext(context);
		errorFileAppender.addFilter(errorLevelFilter(context));
		errorFileAppender.setEncoder(patternLayoutEncoder(context));
		errorFileAppender.setName(FILE_ERROR_APPENDER_NAME);
		errorFileAppender.setFile(logErrorFile);
		errorFileAppender.setRollingPolicy(rollingPolicy(context, errorFileAppender, logErrorFile));
		errorFileAppender.start();
		context.getLogger(Logger.ROOT_LOGGER_NAME).detachAppender(FILE_ERROR_APPENDER_NAME);
		context.getLogger(Logger.ROOT_LOGGER_NAME).addAppender(errorFileAppender);
	}

	/**
	 * <p>addLogstashTcpSocketAppender.</p>
	 *
	 * @param context            a {@link LoggerContext} object.
	 * @param customFields       a {@link String} object.
	 * @param logStashProperties a {@link net.dreamlu.mica.logging.config.MicaLoggingProperties.Logstash} object.
	 */
	public static void addLogStashTcpSocketAppender(LoggerContext context,
													String customFields,
													MicaLoggingProperties.Logstash logStashProperties) {
		log.info("Initializing LogStash loggingProperties");
		// More documentation is available at: https://github.com/logstash/logstash-logback-encoder
		LogstashTcpSocketAppender logStashAppender = new LogstashTcpSocketAppender();
		logStashAppender.addDestinations(new InetSocketAddress(logStashProperties.getHost(), logStashProperties.getPort()));
		logStashAppender.setContext(context);
		logStashAppender.setEncoder(logstashEncoder(customFields));
		logStashAppender.setName(ASYNC_LOG_STASH_APPENDER_NAME);
		logStashAppender.setQueueSize(logStashProperties.getQueueSize());
		logStashAppender.start();
		context.getLogger(Logger.ROOT_LOGGER_NAME).addAppender(logStashAppender);
	}

	/**
	 * <p>addContextListener.</p>
	 *
	 * @param context      a {@link LoggerContext} object.
	 * @param customFields a {@link String} object.
	 * @param properties   a {@link net.dreamlu.mica.logging.config.MicaLoggingProperties} object.
	 */
	public static void addContextListener(LoggerContext context,
										  String logFile,
										  String logErrorFile,
										  String customFields,
										  MicaLoggingProperties properties) {
		LogbackLoggerContextListener loggerContextListener = new LogbackLoggerContextListener(logFile, logErrorFile, customFields, properties);
		loggerContextListener.setContext(context);
		context.addListener(loggerContextListener);
	}

	private static RollingPolicy rollingPolicy(LoggerContext context,
											   FileAppender<?> appender,
											   String logErrorFile) {
		SizeAndTimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = new SizeAndTimeBasedRollingPolicy<>();
		rollingPolicy.setContext(context);
		rollingPolicy.setCleanHistoryOnStart(SystemUtil.getPropToBool(LogbackLoggingSystemProperties.ROLLINGPOLICY_CLEAN_HISTORY_ON_START, false));
		rollingPolicy.setFileNamePattern(logErrorFile + ".%d{yyyy-MM-dd}.%i.gz");
		rollingPolicy.setMaxFileSize(FileSize.valueOf(SystemUtil.getProp(LogbackLoggingSystemProperties.ROLLINGPOLICY_MAX_FILE_SIZE, "10MB")));
		rollingPolicy.setMaxHistory(SystemUtil.getPropToInt(LogbackLoggingSystemProperties.ROLLINGPOLICY_MAX_HISTORY, 7));
		rollingPolicy.setTotalSizeCap(FileSize.valueOf(SystemUtil.getProp(LogbackLoggingSystemProperties.ROLLINGPOLICY_TOTAL_SIZE_CAP, "0")));
		rollingPolicy.setParent(appender);
		rollingPolicy.start();
		return rollingPolicy;
	}

	private static ThresholdFilter errorLevelFilter(LoggerContext context) {
		final ThresholdFilter filter = new ThresholdFilter();
		filter.setContext(context);
		filter.setLevel(Level.ERROR.levelStr);
		filter.start();
		return filter;
	}

	private static Encoder<ILoggingEvent> patternLayoutEncoder(LoggerContext context) {
		PatternLayoutEncoder encoder = new PatternLayoutEncoder();
		encoder.setContext(context);
		encoder.setPattern(SystemUtil.getProp(LoggingSystemProperties.FILE_LOG_PATTERN));
		String charsetName = SystemUtil.getProp(LogbackLoggingSystemProperties.FILE_LOG_CHARSET, "default");
		encoder.setCharset(Charset.forName(charsetName));
		encoder.start();
		return encoder;
	}

	private static LoggingEventCompositeJsonEncoder compositeJsonEncoder(LoggerContext context,
																		 String customFields) {
		final LoggingEventCompositeJsonEncoder compositeJsonEncoder = new LoggingEventCompositeJsonEncoder();
		compositeJsonEncoder.setContext(context);
		compositeJsonEncoder.setProviders(jsonProviders(context, customFields));
		compositeJsonEncoder.start();
		return compositeJsonEncoder;
	}

	private static LogstashEncoder logstashEncoder(String customFields) {
		final LogstashEncoder logstashEncoder = new LogstashEncoder();
		logstashEncoder.setThrowableConverter(throwableConverter());
		logstashEncoder.setCustomFields(customFields);
		return logstashEncoder;
	}

	private static LoggingEventJsonProviders jsonProviders(LoggerContext context,
														   String customFields) {
		final LoggingEventJsonProviders jsonProviders = new LoggingEventJsonProviders();
		jsonProviders.addArguments(new ArgumentsJsonProvider());
		jsonProviders.addContext(new ContextJsonProvider<>());
		jsonProviders.addGlobalCustomFields(customFieldsJsonProvider(customFields));
		jsonProviders.addLogLevel(new LogLevelJsonProvider());
		jsonProviders.addLoggerName(loggerNameJsonProvider());
		jsonProviders.addMdc(new MdcJsonProvider());
		jsonProviders.addMessage(new MessageJsonProvider());
		jsonProviders.addPattern(new LoggingEventPatternJsonProvider());
		jsonProviders.addStackTrace(stackTraceJsonProvider());
		jsonProviders.addThreadName(new ThreadNameJsonProvider());
		jsonProviders.addTimestamp(timestampJsonProvider());
		jsonProviders.setContext(context);
		return jsonProviders;
	}

	private static GlobalCustomFieldsJsonProvider<ILoggingEvent> customFieldsJsonProvider(String customFields) {
		final GlobalCustomFieldsJsonProvider<ILoggingEvent> customFieldsJsonProvider = new GlobalCustomFieldsJsonProvider<>();
		customFieldsJsonProvider.setCustomFields(customFields);
		return customFieldsJsonProvider;
	}

	private static LoggerNameJsonProvider loggerNameJsonProvider() {
		final LoggerNameJsonProvider loggerNameJsonProvider = new LoggerNameJsonProvider();
		loggerNameJsonProvider.setShortenedLoggerNameLength(20);
		return loggerNameJsonProvider;
	}

	private static StackTraceJsonProvider stackTraceJsonProvider() {
		StackTraceJsonProvider stackTraceJsonProvider = new StackTraceJsonProvider();
		stackTraceJsonProvider.setThrowableConverter(throwableConverter());
		return stackTraceJsonProvider;
	}

	private static ShortenedThrowableConverter throwableConverter() {
		final ShortenedThrowableConverter throwableConverter = new ShortenedThrowableConverter();
		throwableConverter.setRootCauseFirst(true);
		return throwableConverter;
	}

	private static LoggingEventFormattedTimestampJsonProvider timestampJsonProvider() {
		final LoggingEventFormattedTimestampJsonProvider timestampJsonProvider = new LoggingEventFormattedTimestampJsonProvider();
		timestampJsonProvider.setTimeZone("UTC");
		timestampJsonProvider.setFieldName("timestamp");
		return timestampJsonProvider;
	}

	/**
	 * Logback configuration is achieved by configuration file and API.
	 * When configuration file change is detected, the configuration is reset.
	 * This listener ensures that the programmatic configuration is also re-applied after reset.
	 */
	private static class LogbackLoggerContextListener extends ContextAwareBase implements LoggerContextListener {
		private final String logFile;
		private final String logErrorFile;
		private final String customFields;
		private final MicaLoggingProperties loggingProperties;

		private LogbackLoggerContextListener(String logFile,
											 String logErrorFile,
											 String customFields,
											 MicaLoggingProperties loggingProperties) {
			this.loggingProperties = loggingProperties;
			this.customFields = customFields;
			this.logFile = logFile;
			this.logErrorFile = logErrorFile;
		}

		@Override
		public boolean isResetResistant() {
			return true;
		}

		@Override
		public void onStart(LoggerContext context) {
			if (this.loggingProperties.getLogstash().isEnabled()) {
				addLogStashTcpSocketAppender(context, customFields, loggingProperties.getLogstash());
			} else {
				boolean useJsonFormat = loggingProperties.isUseJsonFormat();
				addFileAppender(context, logFile, logErrorFile, useJsonFormat, customFields);
			}
		}

		@Override
		public void onReset(LoggerContext context) {
			if (this.loggingProperties.getLogstash().isEnabled()) {
				addLogStashTcpSocketAppender(context, customFields, loggingProperties.getLogstash());
			} else {
				boolean useJsonFormat = loggingProperties.isUseJsonFormat();
				addFileAppender(context, logFile, logErrorFile, useJsonFormat, customFields);
			}
		}

		@Override
		public void onStop(LoggerContext context) {
			// Nothing to do.
		}

		@Override
		public void onLevelChange(Logger logger, Level level) {
			// Nothing to do.
		}
	}
}
