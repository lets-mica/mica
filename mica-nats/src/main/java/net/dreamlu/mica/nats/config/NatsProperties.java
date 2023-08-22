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

package net.dreamlu.mica.nats.config;

import io.nats.client.Options;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.time.Duration;

/**
 * nats 配置
 *
 * <p>
 *    参考： https://github.com/nats-io/spring-nats
 * </p>
 *
 * @author L.cm
 */
@Getter
@Setter
@RefreshScope
@ConfigurationProperties(NatsProperties.PREFIX)
public class NatsProperties {
	public static final String PREFIX = "nats";

	/**
	 * nats服务器的URL，可以是 , 逗号分隔的列表。默认：nats://localhost:4222
	 */
	private String server = Options.DEFAULT_URL;

	/**
	 * 连接名称，显示在线程名称中。
	 */
	private String connectionName;

	/**
	 * 在初始连接之后，如果连接丢失，则最大重新连接尝试次数。
	 */
	private int maxReconnect = Options.DEFAULT_MAX_RECONNECT;

	/**
	 * 重连等待时间
	 */
	private Duration reconnectWait = Options.DEFAULT_RECONNECT_WAIT;

	/**
	 * 链接超时时间
	 */
	private Duration connectionTimeout = Options.DEFAULT_CONNECTION_TIMEOUT;

	/**
	 * 心跳发送周期
	 */
	private Duration pingInterval = Options.DEFAULT_PING_INTERVAL;

	/**
	 * 重连包大小
	 */
	private long reconnectBufferSize = Options.DEFAULT_RECONNECT_BUF_SIZE;

	/**
	 * 用于收件箱的前缀，通常使用默认前缀，但使用自定义前缀可以进行安全控制。
	 */
	private String inboxPrefix = Options.DEFAULT_INBOX_PREFIX;

	/**
	 * 服务端是否将发送的消息回发，默认 false
	 */
	private boolean noEcho = false;

	/**
	 * 是否将 subjects 主题视为 UTF-8 编码，默认值为：ASCII
	 */
	private boolean utf8Support = false;

	/**
	 * 使用用户名、密码认证
	 */
	private String username;

	/**
	 * 使用用户名、密码认证
	 */
	private String password;

	/**
	 * 使用 token 令牌认证
	 */
	private String token;

	/**
	 * 使用证书认证
	 */
	private String credentials;

	/**
	 * 用于NKey身份验证的私钥
	 */
	private String nkey;

	/**
	 * SSL keystore 路径
	 */
	private String keyStorePath;

	/**
	 * SSL keystore 密码
	 */
	private String keyStorePassword;

	/**
	 * SSL keystore 类型.
	 */
	private String keyStoreType;

	/**
	 * SSL trust store 路径，用于验证服务端
	 */
	private String trustStorePath;

	/**
	 * the SSL 信任证书密码，用于校验服务端
	 */
	private String trustStorePassword;

	/**
	 * SSL密钥存储的提供程序算法，用于验证服务器。
	 */
	private String keyStoreProvider;

	/**
	 * 用于验证服务器的SSL信任存储的提供程序算法。
	 */
	private String trustStoreProvider;

	/**
	 * TLS 协议版本. 使用: TLSv1.2, TLSv1.3
	 */
	private String tlsProtocol;

	/**
	 * SSL trust store 证书类型
	 */
	private String trustStoreType;

}
