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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.ClassUtils;

/**
 * logging 配置
 *
 * @author L.cm
 */
@Getter
@Setter
@RefreshScope
@ConfigurationProperties(MicaLoggingProperties.PREFIX)
public class MicaLoggingProperties {
	public static final String PREFIX = "mica.logging";

	private final Console console = new Console();
	private final Files files = new Files();
	private final Logstash logstash = new Logstash();
	private final Loki loki = new Loki();

	@Getter
	@Setter
	public static class Console {
		/**
		 * 是否启动完成后关闭控制台日志，适用于，正式环境
		 */
		private boolean closeAfterStart = false;
	}

	@Getter
	@Setter
	public static class Files {
		public static final String PREFIX = MicaLoggingProperties.PREFIX + ".files";
		/**
		 * 是否开启文件日志
		 */
		private boolean enabled = true;
		/**
		 * 使用 json 格式化
		 */
		private boolean useJsonFormat = false;
	}

	@Getter
	@Setter
	public static class Logstash {
		public static final String PREFIX = MicaLoggingProperties.PREFIX + ".logstash";
		/**
		 * 是否开启 logstash 日志收集
		 */
		private boolean enabled = false;
		/**
		 * 目标地址，默认： localhost:5000，示例： host1.domain.com,host2.domain.com:5560
		 */
		private String destinations = "localhost:5000";
		/**
		 * logstash 队列大小
		 */
		private int queueSize = 512;
	}

	@Getter
	@Setter
	public static class Loki {
		public static final String PREFIX = MicaLoggingProperties.PREFIX + ".loki";
		/**
		 * 是否开启 loki 日志收集
		 */
		private boolean enabled = false;
		/**
		 * 编码方式，支持 Json、ProtoBuf，默认： Json
		 */
		private LokiEncoder encoder = LokiEncoder.Json;
		/**
		 * http sender，支持 java11、OKHttp、ApacheHttp，默认: 从项目依赖中查找，顺序 java11 -> okHttp -> ApacheHttp
		 */
		private HttpSender httpSender;
		/**
		 * 通用配置
		 */
		private int batchMaxItems = 1000;
		private int batchMaxBytes = 4 * 1024 * 1024;
		private long batchTimeoutMs = 60000;
		private long sendQueueMaxBytes = 41943040;
		/**
		 * 使用堆外内存
		 */
		private boolean useDirectBuffers = true;
		private boolean drainOnStop = true;
		/**
		 * 开启 metrics
		 */
		private boolean metricsEnabled = false;
		private boolean verbose = false;
		/**
		 * http 配置，默认: http://localhost:3100/loki/api/v1/push
		 */
		private String httpUrl = "http://localhost:3100/loki/api/v1/push";
		private long httpConnectionTimeoutMs = 30000;
		private long httpRequestTimeoutMs = 5000;
		private String httpAuthUsername;
		private String httpAuthPassword;
		private String httpTenantId;
		/**
		 * format 标签，默认： appName=${appName},profile=${profile},host=${HOSTNAME},level=%level,traceId=%X{traceId:-NAN},requestId=%X{requestId:-}
		 */
		private String formatLabelPattern = "appName=${appName},profile=${profile},host=${HOSTNAME},level=%level,traceId=%X{traceId:-NAN},requestId=%X{requestId:-NAN}";
		/**
		 * format 标签扩展
		 */
		private String formatLabelPatternExtend;
		/**
		 * format 标签分隔符，默认:，
		 */
		private String formatLabelPairSeparator = ",";
		/**
		 * format 标签 key、value 分隔符，默认: =
		 */
		private String formatLabelKeyValueSeparator = "=";
		private boolean formatLabelNoPex = true;
		/**
		 * 消息体格式，默认为: l=%level c=%logger{20} t=%thread | %msg %ex
		 */
		private String formatMessagePattern;
		private boolean formatStaticLabels = false;
		private boolean formatSortByTime = false;
	}

	/**
	 * 编码方式
	 */
	public enum LokiEncoder {
		/**
		 * Encoder
		 */
		Json,
		ProtoBuf
	}

	/**
	 * http Sender
	 */
	@Getter
	@RequiredArgsConstructor
	public enum HttpSender {
		/**
		 * http 方式
		 */
		JAVA11("java.net.http.HttpClient"),
		OKHttp("okhttp3.OkHttpClient"),
		ApacheHttp("org.apache.http.impl.client.HttpClients");

		/**
		 * sender 判定类
		 */
		private final String senderClass;

		public boolean isAvailable() {
			return ClassUtils.isPresent(senderClass, null);
		}
	}

}
