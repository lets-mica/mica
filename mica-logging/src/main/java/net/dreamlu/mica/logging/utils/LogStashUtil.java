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

package net.dreamlu.mica.logging.utils;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import net.logstash.logback.composite.ContextJsonProvider;
import net.logstash.logback.composite.GlobalCustomFieldsJsonProvider;
import net.logstash.logback.composite.loggingevent.*;
import net.logstash.logback.stacktrace.ShortenedThrowableConverter;

/**
 * LogStash 工具
 * <p>
 * Utility methods to add appenders to a {@link LoggerContext}.
 *
 * @author jhipster
 * @author L.cm
 */
public class LogStashUtil {

	public static LoggingEventJsonProviders jsonProviders(LoggerContext context,
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
		jsonProviders.addThreadName(new LoggingEventThreadNameJsonProvider());
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
		final StackTraceJsonProvider stackTraceJsonProvider = new StackTraceJsonProvider();
		stackTraceJsonProvider.setThrowableConverter(throwableConverter());
		return stackTraceJsonProvider;
	}

	public static ShortenedThrowableConverter throwableConverter() {
		final ShortenedThrowableConverter throwableConverter = new ShortenedThrowableConverter();
		throwableConverter.setRootCauseFirst(true);
		return throwableConverter;
	}

	private static LoggingEventFormattedTimestampJsonProvider timestampJsonProvider() {
		final LoggingEventFormattedTimestampJsonProvider timestampJsonProvider = new LoggingEventFormattedTimestampJsonProvider();
		timestampJsonProvider.setTimeZone("UTC");
		return timestampJsonProvider;
	}

}
