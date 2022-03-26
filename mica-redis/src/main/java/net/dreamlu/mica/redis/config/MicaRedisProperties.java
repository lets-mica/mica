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

package net.dreamlu.mica.redis.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.time.Duration;

/**
 * redis 配置
 *
 * @author L.cm
 */
@Getter
@Setter
@RefreshScope
@ConfigurationProperties(MicaRedisProperties.PREFIX)
public class MicaRedisProperties {
	public static final String PREFIX = "mica.redis";

	/**
	 * 序列化方式
	 */
	private SerializerType serializerType = SerializerType.JSON;
	/**
	 * stream
	 */
	private Stream stream = new Stream();

	/**
	 * 序列化方式
	 */
	public enum SerializerType {
		/**
		 * json 序列化
		 */
		JSON,
		/**
		 * jdk 序列化
		 */
		JDK
	}

	@Getter
	@Setter
	public static class Stream {
		public static final String PREFIX = MicaRedisProperties.PREFIX + "stream";
		/**
		 * 是否开启 stream
		 */
		boolean enable = false;
		/**
		 * consumer group，默认：服务名 + 环境
		 */
		String consumerGroup;
		/**
		 * 消费者名称，默认：ip + 端口
		 */
		String consumerName;
		/**
		 * poll 批量大小
		 */
		Integer pollBatchSize;
		/**
		 * poll 超时时间
		 */
		Duration pollTimeout;
	}

}
