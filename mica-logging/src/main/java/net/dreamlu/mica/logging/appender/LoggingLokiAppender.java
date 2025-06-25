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

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.CoreConstants;
import com.github.loki4j.logback.*;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.constant.MicaConstant;
import net.dreamlu.mica.core.utils.CharPool;
import net.dreamlu.mica.core.utils.StringUtil;
import net.dreamlu.mica.logging.config.MicaLoggingProperties;
import net.dreamlu.mica.logging.loki.Loki4jOkHttpSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;

/**
 * loki 日志接收
 *
 * @author L.cm
 */
@Slf4j
public class LoggingLokiAppender implements ILoggingAppender {
	private static final String APPENDER_NAME = "LOKI";
	private final MicaLoggingProperties properties;
	private final String appName;
	private final String profile;

	public LoggingLokiAppender(Environment environment,
							   MicaLoggingProperties properties) {
		this.properties = properties;
		// 1. 服务名和环境
		this.appName = environment.getRequiredProperty(MicaConstant.SPRING_APP_NAME_KEY);
		this.profile = environment.getRequiredProperty(MicaConstant.ACTIVE_PROFILES_PROPERTY);
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		this.start(context);
	}

	@Override
	public void start(LoggerContext context) {
		log.info("Loki logging start.");
		reload(context);
	}

	@Override
	public void reset(LoggerContext context) {
		log.info("Loki logging reset.");
		reload(context);
	}

	private void reload(LoggerContext context) {
		MicaLoggingProperties.Loki loki = properties.getLoki();
		if (loki.isEnabled()) {
			addLokiAppender(context, loki);
		}
	}

	private void addLokiAppender(LoggerContext context, MicaLoggingProperties.Loki properties) {
		// appender 配置
		Loki4jAppender lokiAppender = new Loki4jAppender();
		lokiAppender.setName(APPENDER_NAME);
		lokiAppender.setContext(context);
		lokiAppender.setHttp(getHttpCfg(properties));
		lokiAppender.setBatch(getBatchCfg(properties));
		lokiAppender.setMetricsEnabled(properties.isMetricsEnabled());
		lokiAppender.setVerbose(properties.isVerbose());
		// format
		lokiAppender.setLabels(formatLabelPatternHandle(context, properties));
		lokiAppender.setStructuredMetadata(properties.getStructuredMetadataPattern());
		lokiAppender.start();
		// 先删除，再添加
		context.getLogger(Logger.ROOT_LOGGER_NAME).detachAppender(APPENDER_NAME);
		context.getLogger(Logger.ROOT_LOGGER_NAME).addAppender(lokiAppender);
	}

	private static PipelineConfigAppenderBase.HttpCfg getHttpCfg(MicaLoggingProperties.Loki properties) {
		// http 配置
		PipelineConfigAppenderBase.HttpCfg httpCfg = new PipelineConfigAppenderBase.HttpCfg();
		httpCfg.setUrl(properties.getHttpUrl());
		httpCfg.setTenantId(properties.getHttpTenantId());
		httpCfg.setConnectionTimeoutMs(properties.getHttpConnectionTimeoutMs());
		httpCfg.setRequestTimeoutMs(properties.getHttpRequestTimeoutMs());
		httpCfg.setUseProtobufApi(true);
		// 认证配置
		String authUsername = properties.getHttpAuthUsername();
		String authPassword = properties.getHttpAuthPassword();
		if (StringUtil.isNotBlank(authUsername) && StringUtil.isNotBlank(authPassword)) {
			PipelineConfigAppenderBase.BasicAuth basicAuth = new PipelineConfigAppenderBase.BasicAuth();
			basicAuth.setUsername(authUsername);
			basicAuth.setPassword(authPassword);
			httpCfg.setAuth(basicAuth);
		}
		// json or Protobuf
		httpCfg.setUseProtobufApi(MicaLoggingProperties.LokiEncoder.ProtoBuf == properties.getEncoder());
		// http sender
		HttpSender httpSender;
		MicaLoggingProperties.HttpSender httpSenderType = getHttpSender(properties);
		if (MicaLoggingProperties.HttpSender.OKHttp == httpSenderType) {
			httpSender = new Loki4jOkHttpSender();
		} else if (MicaLoggingProperties.HttpSender.ApacheHttp == httpSenderType) {
			httpSender = new ApacheHttpSender();
		} else {
			httpSender = new JavaHttpSender();
		}
		httpCfg.setSender(httpSender);
		return httpCfg;
	}

	private static PipelineConfigAppenderBase.BatchCfg getBatchCfg(MicaLoggingProperties.Loki properties) {
		PipelineConfigAppenderBase.BatchCfg batchCfg = new PipelineConfigAppenderBase.BatchCfg();
		batchCfg.setMaxItems(properties.getBatchMaxItems());
		batchCfg.setMaxBytes(properties.getBatchMaxBytes());
		batchCfg.setTimeoutMs(properties.getBatchTimeoutMs());
		batchCfg.setSendQueueMaxBytes(properties.getSendQueueMaxBytes());
		batchCfg.setUseDirectBuffers(properties.isUseDirectBuffers());
		batchCfg.setDrainOnStop(properties.isDrainOnStop());
		batchCfg.setStaticLabels(properties.isFormatStaticLabels());
		return batchCfg;
	}

	private String formatLabelPatternHandle(LoggerContext context, MicaLoggingProperties.Loki properties) {
		String labelPattern = properties.getFormatLabelPattern();
		Assert.hasText(labelPattern, "MicaLoggingProperties mica.logging.loki.format-label-pattern is blank.");
		String labelPatternExtend = properties.getFormatLabelPatternExtend();
		if (StringUtil.isNotBlank(labelPatternExtend)) {
			labelPattern = labelPattern + CharPool.COMMA + labelPatternExtend;
		}
		return labelPattern
			.replace("${appName}", appName)
			.replace("${profile}", profile)
			.replace("${HOSTNAME}", context.getProperty(CoreConstants.HOSTNAME_KEY));
	}

	private static MicaLoggingProperties.HttpSender getHttpSender(MicaLoggingProperties.Loki properties) {
		MicaLoggingProperties.HttpSender httpSenderProp = properties.getHttpSender();
		if (httpSenderProp != null && httpSenderProp.isAvailable()) {
			log.debug("mica logging use {} HttpSender", httpSenderProp);
			return httpSenderProp;
		}
		if (httpSenderProp == null) {
			MicaLoggingProperties.HttpSender[] httpSenders = MicaLoggingProperties.HttpSender.values();
			for (MicaLoggingProperties.HttpSender httpSender : httpSenders) {
				if (httpSender.isAvailable()) {
					log.debug("mica logging use {} HttpSender", httpSender);
					return httpSender;
				}
			}
			throw new IllegalArgumentException("Not java11 and no okHttp or apache http dependency.");
		}
		throw new NoClassDefFoundError(httpSenderProp.getSenderClass());
	}
}
