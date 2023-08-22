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

import io.nats.client.Connection;
import io.nats.client.ConnectionListener;
import io.nats.client.Nats;
import io.nats.client.Options;
import net.dreamlu.mica.core.utils.ResourceUtil;
import net.dreamlu.mica.core.utils.StringUtil;
import net.dreamlu.mica.nats.core.DefaultNatsTemplate;
import net.dreamlu.mica.nats.core.NatsListenerDetector;
import net.dreamlu.mica.nats.core.NatsTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import javax.net.ssl.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;

/**
 * nats 配置
 *
 * @author L.cm
 */
@AutoConfiguration
@EnableConfigurationProperties({
	NatsProperties.class,
	NatsStreamProperties.class
})
@ConditionalOnClass(Options.class)
public class NatsConfiguration {

	@Bean
	public Options natsOptions(NatsProperties properties,
							   ObjectProvider<ConnectionListener> connectionListenerObjectProvider,
							   ObjectProvider<NatsCustomizer> natsCustomizerObjectProvider) {
		final Options.Builder builder = new Options.Builder()
			.server(properties.getServer())
			.connectionName(properties.getConnectionName())
			.maxReconnects(properties.getMaxReconnect())
			.reconnectWait(properties.getReconnectWait())
			.connectionTimeout(properties.getConnectionTimeout())
			.pingInterval(properties.getPingInterval())
			.reconnectBufferSize(properties.getReconnectBufferSize())
			.inboxPrefix(properties.getInboxPrefix());
		// 服务端是否将发送的消息回发，默认 false
		if (properties.isNoEcho()) {
			builder.noEcho();
		}
		// 是否将 subjects 主题视为 UTF-8 编码，默认值为：ASCII
		if (properties.isUtf8Support()) {
			builder.supportUTF8Subjects();
		}
		// 认证相关参数
		String nKey = properties.getNkey();
		String credentials = properties.getCredentials();
		String token = properties.getToken();
		String username = properties.getUsername();
		String password = properties.getPassword();
		if (StringUtils.hasText(nKey)) {
			builder.authHandler(Nats.staticCredentials(null, nKey.toCharArray()));
		} else if (StringUtils.hasText(credentials)) {
			builder.authHandler(Nats.credentials(credentials));
		} else if (StringUtils.hasText(token)) {
			builder.token(token.toCharArray());
		} else if (StringUtils.hasText(username) && StringUtils.hasText(password)) {
			builder.userInfo(username, password);
		}
		// ssl 证书信息
		String keyStorePath = properties.getKeyStorePath();
		String trustStorePath = properties.getTrustStorePath();
		if (StringUtils.hasText(keyStorePath) && StringUtils.hasText(trustStorePath)) {
			builder.sslContext(createSSLContext(properties));
		}
		// 设置 nats 连接监听器
		connectionListenerObjectProvider.ifAvailable(builder::connectionListener);
		// 用户自定义配置
		natsCustomizerObjectProvider.orderedStream().forEach(natsOptionsCustomizer -> natsOptionsCustomizer.customize(builder));
		return builder.build();
	}

	@Bean
	public Connection natsConnection(Options natsOptions) throws IOException, InterruptedException {
		return Nats.connect(natsOptions);
	}

	@Bean
	public NatsListenerDetector natsListenerDetector(Connection natsConnection) {
		return new NatsListenerDetector(natsConnection);
	}

	@Bean
	public NatsTemplate natsTemplate(Connection natsConnection) {
		return new DefaultNatsTemplate(natsConnection);
	}

	private static SSLContext createSSLContext(NatsProperties properties) {
		try {
			return initSSLContext(properties);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	private static KeyStore loadKeystore(String path, char[] password, String keyStoreType)
		throws IOException, GeneralSecurityException {
		KeyStore store = KeyStore.getInstance(keyStoreType);
		Resource resource = ResourceUtil.getResource(path);
		try (BufferedInputStream in = new BufferedInputStream(resource.getInputStream())) {
			store.load(in, password);
		}
		return store;
	}

	private static KeyManager[] createKeyManagers(String path, String passwordStr,
												  String keyStoreProvider, String keyStoreType)
		throws IOException, GeneralSecurityException {
		if (StringUtil.isBlank(keyStoreProvider)) {
			keyStoreProvider = "SunX509";
		}
		if (StringUtil.isBlank(keyStoreProvider)) {
			keyStoreType = "PKCS12";
		}
		char[] password;
		if (StringUtils.hasText(passwordStr)) {
			password = passwordStr.toCharArray();
		} else {
			password = new char[0];
		}
		KeyStore store = loadKeystore(path, password, keyStoreType);
		KeyManagerFactory factory = KeyManagerFactory.getInstance(keyStoreProvider);
		factory.init(store, password);
		return factory.getKeyManagers();
	}

	private static TrustManager[] createTrustManagers(String path, String passwordStr,
													  String trustStoreProvider, String trustStoreType)
		throws IOException, GeneralSecurityException {
		if (StringUtil.isBlank(trustStoreProvider)) {
			trustStoreProvider = "SunX509";
		}
		if (StringUtil.isBlank(trustStoreType)) {
			trustStoreType = "PKCS12";
		}
		char[] password;
		if (StringUtils.hasText(passwordStr)) {
			password = passwordStr.toCharArray();
		} else {
			password = new char[0];
		}
		KeyStore store = loadKeystore(path, password, trustStoreType);
		TrustManagerFactory factory = TrustManagerFactory.getInstance(trustStoreProvider);
		/*
		 * Trust Store doesn't need to load any keys from the store (just certs) so
		 * passing a password is not necessary
		 */
		factory.init(store);
		return factory.getTrustManagers();
	}

	/**
	 * @return SSLContext with the specified TLS protocol, Key and Trust stores.
	 * @throws IOException              if there is a problem reading a file or
	 *                                  setting up the SSL context
	 * @throws GeneralSecurityException if there is a problem setting up the SSL
	 *                                  context
	 */
	private static SSLContext initSSLContext(NatsProperties properties) throws IOException, GeneralSecurityException {
		String tlsProtocol = properties.getTlsProtocol();
		if (!StringUtils.hasText(tlsProtocol)) {
			tlsProtocol = Options.DEFAULT_SSL_PROTOCOL;
		}
		// key 证书管理器
		String keyStorePath = properties.getKeyStorePath();
		String keyStorePassword = properties.getKeyStorePassword();
		String keyStoreProvider = properties.getKeyStoreProvider();
		String keyStoreType = properties.getKeyStoreType();
		KeyManager[] keyManagers = createKeyManagers(
			keyStorePath,
			keyStorePassword,
			keyStoreProvider,
			keyStoreType
		);
		// 信任证书管理器
		String trustStorePath = properties.getTrustStorePath();
		String trustStorePassword = properties.getTrustStorePassword();
		String trustStoreProvider = properties.getTrustStoreProvider();
		String trustStoreType = properties.getTrustStoreType();
		TrustManager[] trustManagers = createTrustManagers(
			trustStorePath,
			trustStorePassword,
			trustStoreProvider,
			trustStoreType
		);
		SSLContext sslContext = SSLContext.getInstance(tlsProtocol);
		sslContext.init(keyManagers, trustManagers, new SecureRandom());
		return sslContext;
	}

}
