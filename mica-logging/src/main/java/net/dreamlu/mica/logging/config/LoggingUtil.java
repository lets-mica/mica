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
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.spi.ContextAwareBase;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.appender.LogstashTcpSocketAppender;
import net.logstash.logback.composite.ContextJsonProvider;
import net.logstash.logback.composite.GlobalCustomFieldsJsonProvider;
import net.logstash.logback.composite.loggingevent.*;
import net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder;
import net.logstash.logback.encoder.LogstashEncoder;
import net.logstash.logback.stacktrace.ShortenedThrowableConverter;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * 参考自 jhipster
 *
 * Utility methods to add appenders to a {@link LoggerContext}.
 */
@Slf4j
@UtilityClass
public class LoggingUtil {
	public static final String CONSOLE_APPENDER_NAME = "CONSOLE";
	public static final String FILE_APPENDER_NAME = "FILE";
	public static final String FILE_ERROR_APPENDER_NAME = "FILE_ERROR";
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
	public static void addJsonConsoleAppender(LoggerContext context, String customFields) {
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
	 * <p>addLogstashTcpSocketAppender.</p>
	 *
	 * @param context            a {@link LoggerContext} object.
	 * @param customFields       a {@link String} object.
	 * @param logstashProperties a {@link net.dreamlu.mica.logging.config.MicaLoggingProperties.Logstash} object.
	 */
	public static void addLogstashTcpSocketAppender(LoggerContext context, String customFields,
													MicaLoggingProperties.Logstash logstashProperties) {
		log.info("Initializing Logstash loggingProperties");
		// More documentation is available at: https://github.com/logstash/logstash-logback-encoder
		LogstashTcpSocketAppender logStashAppender = new LogstashTcpSocketAppender();
		logStashAppender.addDestinations(new InetSocketAddress(logstashProperties.getHost(), logstashProperties.getPort()));
		logStashAppender.setContext(context);
		logStashAppender.setEncoder(logstashEncoder(customFields));
		logStashAppender.setName(ASYNC_LOG_STASH_APPENDER_NAME);
		logStashAppender.setQueueSize(logstashProperties.getQueueSize());
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
	public static void addContextListener(LoggerContext context, String customFields, MicaLoggingProperties properties) {
		LogbackLoggerContextListener loggerContextListener = new LogbackLoggerContextListener(properties, customFields);
		loggerContextListener.setContext(context);
		context.addListener(loggerContextListener);
	}

	private static LoggingEventCompositeJsonEncoder compositeJsonEncoder(LoggerContext context, String customFields) {
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

	private static LoggingEventJsonProviders jsonProviders(LoggerContext context, String customFields) {
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
		return timestampJsonProvider;
	}

	/**
	 * Logback configuration is achieved by configuration file and API.
	 * When configuration file change is detected, the configuration is reset.
	 * This listener ensures that the programmatic configuration is also re-applied after reset.
	 */
	private static class LogbackLoggerContextListener extends ContextAwareBase implements LoggerContextListener {
		private final MicaLoggingProperties loggingProperties;
		private final String customFields;

		private LogbackLoggerContextListener(MicaLoggingProperties loggingProperties, String customFields) {
			this.loggingProperties = loggingProperties;
			this.customFields = customFields;
		}

		@Override
		public boolean isResetResistant() {
			return true;
		}

		@Override
		public void onStart(LoggerContext context) {
			if (this.loggingProperties.isUseJsonFormat()) {
				addJsonConsoleAppender(context, customFields);
			}
			if (this.loggingProperties.getLogstash().isEnabled()) {
				addLogstashTcpSocketAppender(context, customFields, loggingProperties.getLogstash());
			}
		}

		@Override
		public void onReset(LoggerContext context) {
			if (this.loggingProperties.isUseJsonFormat()) {
				addJsonConsoleAppender(context, customFields);
			}
			if (this.loggingProperties.getLogstash().isEnabled()) {
				addLogstashTcpSocketAppender(context, customFields, loggingProperties.getLogstash());
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
