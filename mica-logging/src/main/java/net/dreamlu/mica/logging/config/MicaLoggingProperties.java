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
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * logging 配置
 *
 * @author L.cm
 */
@Getter
@Setter
@RefreshScope
@ConfigurationProperties("mica.logging")
public class MicaLoggingProperties {

	/**
	 * 使用 json 格式化
	 */
	private boolean useJsonFormat = false;
	private final Console console = new Console();
	private final Logstash logstash = new Logstash();

	@Getter
	@Setter
	public static class Console {
		/**
		 * 是否开启控制台日志
		 */
		private boolean enabled = true;
	}

	@Getter
	@Setter
	public static class Logstash {
		/**
		 * 是否开启 logstash 日志收集
		 */
		private boolean enabled = false;
		/**
		 * logstash host
		 */
		private String host = "localhost";
		/**
		 * logstash port
		 */
		private int port = 5000;
		/**
		 * logstash 队列大小
		 */
		private int queueSize = 512;
	}
}
