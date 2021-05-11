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
import com.github.loki4j.logback.*;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.utils.StringUtil;
import net.dreamlu.mica.logging.config.MicaLoggingProperties;
import net.dreamlu.mica.logging.loki.OkHttpSender;
import org.slf4j.LoggerFactory;

/**
 * loki 日志接收
 *
 * @author L.cm
 */
@Slf4j
public class LoggingLokiAppender implements ILoggingAppender {
	private static final String APPENDER_NAME = "LOKI";
	private final MicaLoggingProperties properties;

	public LoggingLokiAppender(MicaLoggingProperties properties) {
		this.properties = properties;
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
		MicaLoggingProperties.Loki loki = properties.getLoki();
		if (loki.isEnabled()) {
			addLokiAppender(context, loki);
		}
	}

	private static void addLokiAppender(LoggerContext context,
										MicaLoggingProperties.Loki properties) {
		Loki4jAppender lokiAppender = new Loki4jAppender();
		lokiAppender.setName(APPENDER_NAME);
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
		lokiAppender.setFormat(getFormat(properties));
		// http
		lokiAppender.setHttp(getSender(properties));
		// 先删除，再添加
		context.getLogger(Logger.ROOT_LOGGER_NAME).detachAppender(APPENDER_NAME);
		context.getLogger(Logger.ROOT_LOGGER_NAME).addAppender(lokiAppender);
	}

	private static Loki4jEncoder getFormat(MicaLoggingProperties.Loki properties) {
		MicaLoggingProperties.LokiEncoder encoder = properties.getEncoder();
		AbstractLoki4jEncoder loki4jEncoder = MicaLoggingProperties.LokiEncoder.ProtoBuf == encoder ?
			new ProtobufEncoder() : new JsonEncoder();
		// label config
		AbstractLoki4jEncoder.LabelCfg labelCfg = new AbstractLoki4jEncoder.LabelCfg();
		labelCfg.setPattern(properties.getFormatLabelPattern());
		labelCfg.setPairSeparator(properties.getFormatLabelPairSeparator());
		labelCfg.setKeyValueSeparator(properties.getFormatLabelKeyValueSeparator());
		labelCfg.setNopex(properties.isFormatLabelNoPex());
		loki4jEncoder.setLabel(labelCfg);
		// message config
		AbstractLoki4jEncoder.MessageCfg messageCfg = new AbstractLoki4jEncoder.MessageCfg();
		messageCfg.setPattern(properties.getFormatMessagePattern());
		loki4jEncoder.setMessage(messageCfg);
		// 其他配置
		loki4jEncoder.setStaticLabels(properties.isFormatStaticLabels());
		loki4jEncoder.setSortByTime(properties.isFormatSortByTime());
		return loki4jEncoder;
	}

	private static HttpSender getSender(MicaLoggingProperties.Loki properties) {
		MicaLoggingProperties.HttpSender httpSender = properties.getHttpSender();
		AbstractHttpSender abstractHttpSender;
		if (MicaLoggingProperties.HttpSender.OK_HTTP == httpSender) {
			abstractHttpSender = new OkHttpSender();
		} else if (MicaLoggingProperties.HttpSender.APACHE_HTTP == httpSender) {
			abstractHttpSender = new ApacheHttpSender();
		} else {
			abstractHttpSender = new JavaHttpSender();
		}
		abstractHttpSender.setUrl(properties.getHttpUrl());
		abstractHttpSender.setConnectionTimeoutMs(properties.getHttpConnectionTimeoutMs());
		abstractHttpSender.setRequestTimeoutMs(properties.getHttpRequestTimeoutMs());
		String authUsername = properties.getHttpAuthUsername();
		String authPassword = properties.getHttpAuthPassword();
		if (StringUtil.isNotBlank(authUsername) && StringUtil.isNotBlank(authPassword)) {
			AbstractHttpSender.BasicAuth basicAuth = new AbstractHttpSender.BasicAuth();
			basicAuth.setUsername(authUsername);
			basicAuth.setPassword(authPassword);
			abstractHttpSender.setAuth(basicAuth);
		}
		abstractHttpSender.setTenantId(properties.getHttpAuthTenantId());
		return abstractHttpSender;
	}

}
