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
import ch.qos.logback.core.CoreConstants;
import com.github.loki4j.logback.*;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.constant.MicaConstant;
import net.dreamlu.mica.core.utils.CharPool;
import net.dreamlu.mica.core.utils.StringUtil;
import net.dreamlu.mica.logging.config.MicaLoggingProperties;
import net.dreamlu.mica.logging.loki.OkHttpSender;
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

	private void addLokiAppender(LoggerContext context,
								 MicaLoggingProperties.Loki properties) {
		Loki4jAppender lokiAppender = new Loki4jAppender();
		lokiAppender.setName(APPENDER_NAME);
		lokiAppender.setContext(context);
		// 通用配置
		lokiAppender.setBatchMaxItems(properties.getBatchMaxItems());
		lokiAppender.setBatchMaxBytes(properties.getBatchMaxBytes());
		lokiAppender.setBatchTimeoutMs(properties.getBatchTimeoutMs());
		lokiAppender.setSendQueueMaxBytes(properties.getSendQueueMaxBytes());
		lokiAppender.setUseDirectBuffers(properties.isUseDirectBuffers());
		lokiAppender.setDrainOnStop(properties.isDrainOnStop());
		lokiAppender.setMetricsEnabled(properties.isMetricsEnabled());
		lokiAppender.setVerbose(properties.isVerbose());
		// format
		Loki4jEncoder loki4jEncoder = getFormat(context, properties);
		lokiAppender.setFormat(loki4jEncoder);
		// http
		lokiAppender.setHttp(getSender(context, loki4jEncoder, properties));
		lokiAppender.start();
		// 先删除，再添加
		context.getLogger(Logger.ROOT_LOGGER_NAME).detachAppender(APPENDER_NAME);
		context.getLogger(Logger.ROOT_LOGGER_NAME).addAppender(lokiAppender);
	}

	private Loki4jEncoder getFormat(LoggerContext context,
									MicaLoggingProperties.Loki properties) {
		MicaLoggingProperties.LokiEncoder encoder = properties.getEncoder();
		AbstractLoki4jEncoder loki4jEncoder = MicaLoggingProperties.LokiEncoder.ProtoBuf == encoder ?
			new ProtobufEncoder() : new JsonEncoder();
		// label config
		AbstractLoki4jEncoder.LabelCfg labelCfg = new AbstractLoki4jEncoder.LabelCfg();
		labelCfg.setPattern(formatLabelPatternHandle(context, properties));
		labelCfg.setPairSeparator(properties.getFormatLabelPairSeparator());
		labelCfg.setKeyValueSeparator(properties.getFormatLabelKeyValueSeparator());
		labelCfg.setNopex(properties.isFormatLabelNoPex());
		loki4jEncoder.setLabel(labelCfg);
		// message config
		AbstractLoki4jEncoder.MessageCfg messageCfg = new AbstractLoki4jEncoder.MessageCfg();
		String formatMessagePattern = properties.getFormatMessagePattern();
		if (StringUtil.isNotBlank(formatMessagePattern)) {
			messageCfg.setPattern(formatMessagePattern);
		}
		loki4jEncoder.setMessage(messageCfg);
		// 其他配置
		loki4jEncoder.setStaticLabels(properties.isFormatStaticLabels());
		loki4jEncoder.setSortByTime(properties.isFormatSortByTime());
		loki4jEncoder.setContext(context);
		loki4jEncoder.start();
		return loki4jEncoder;
	}

	private static HttpSender getSender(LoggerContext context,
										Loki4jEncoder loki4jEncoder,
										MicaLoggingProperties.Loki properties) {
		MicaLoggingProperties.HttpSender httpSenderType = getHttpSender(properties);
		AbstractHttpSender httpSender;
		if (MicaLoggingProperties.HttpSender.OKHttp == httpSenderType) {
			httpSender = new OkHttpSender();
		} else if (MicaLoggingProperties.HttpSender.ApacheHttp == httpSenderType) {
			httpSender = new ApacheHttpSender();
		} else {
			httpSender = new JavaHttpSender();
		}
		httpSender.setUrl(properties.getHttpUrl());
		httpSender.setConnectionTimeoutMs(properties.getHttpConnectionTimeoutMs());
		httpSender.setRequestTimeoutMs(properties.getHttpRequestTimeoutMs());
		String authUsername = properties.getHttpAuthUsername();
		String authPassword = properties.getHttpAuthPassword();
		if (StringUtil.isNotBlank(authUsername) && StringUtil.isNotBlank(authPassword)) {
			AbstractHttpSender.BasicAuth basicAuth = new AbstractHttpSender.BasicAuth();
			basicAuth.setUsername(authUsername);
			basicAuth.setPassword(authPassword);
			httpSender.setAuth(basicAuth);
		}
		httpSender.setTenantId(properties.getHttpTenantId());
		httpSender.setContentType(loki4jEncoder.getContentType());
		httpSender.setContext(context);
		httpSender.start();
		return httpSender;
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
