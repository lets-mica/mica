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

package net.dreamlu.mica.lock.config;

import net.dreamlu.mica.core.utils.StringUtil;
import net.dreamlu.mica.lock.client.RedisLockClient;
import net.dreamlu.mica.lock.client.RedisLockClientImpl;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 分布式锁自动化配置
 *
 * @author L.cm
 */
@Configuration
@ConditionalOnClass(RedissonClient.class)
@EnableConfigurationProperties(MicaLockProperties.class)
public class MicaLockAutoConfiguration {

	private static Config singleConfig(MicaLockProperties properties) {
		Config config = new Config();
		SingleServerConfig serversConfig = config.useSingleServer();
		serversConfig.setAddress(properties.getAddress());
		String password = properties.getPassword();
		if (StringUtil.isNotBlank(password)) {
			serversConfig.setPassword(password);
		}
		serversConfig.setDatabase(properties.getDatabase());
		serversConfig.setConnectionPoolSize(properties.getPoolSize());
		serversConfig.setConnectionMinimumIdleSize(properties.getIdleSize());
		serversConfig.setIdleConnectionTimeout(properties.getConnectionTimeout());
		serversConfig.setConnectTimeout(properties.getConnectionTimeout());
		serversConfig.setTimeout(properties.getTimeout());
		return config;
	}

	private static Config masterSlaveConfig(MicaLockProperties properties) {
		Config config = new Config();
		MasterSlaveServersConfig serversConfig = config.useMasterSlaveServers();
		serversConfig.setMasterAddress(properties.getMasterAddress());
		serversConfig.addSlaveAddress(properties.getSlaveAddress());
		String password = properties.getPassword();
		if (StringUtil.isNotBlank(password)) {
			serversConfig.setPassword(password);
		}
		serversConfig.setDatabase(properties.getDatabase());
		serversConfig.setMasterConnectionPoolSize(properties.getPoolSize());
		serversConfig.setMasterConnectionMinimumIdleSize(properties.getIdleSize());
		serversConfig.setSlaveConnectionPoolSize(properties.getPoolSize());
		serversConfig.setSlaveConnectionMinimumIdleSize(properties.getIdleSize());
		serversConfig.setIdleConnectionTimeout(properties.getConnectionTimeout());
		serversConfig.setConnectTimeout(properties.getConnectionTimeout());
		serversConfig.setTimeout(properties.getTimeout());
		return config;
	}

	private static Config sentinelConfig(MicaLockProperties properties) {
		Config config = new Config();
		SentinelServersConfig serversConfig = config.useSentinelServers();
		serversConfig.setMasterName(properties.getMasterName());
		serversConfig.addSentinelAddress(properties.getSentinelAddress());
		String password = properties.getPassword();
		if (StringUtil.isNotBlank(password)) {
			serversConfig.setPassword(password);
		}
		serversConfig.setDatabase(properties.getDatabase());
		serversConfig.setMasterConnectionPoolSize(properties.getPoolSize());
		serversConfig.setMasterConnectionMinimumIdleSize(properties.getIdleSize());
		serversConfig.setSlaveConnectionPoolSize(properties.getPoolSize());
		serversConfig.setSlaveConnectionMinimumIdleSize(properties.getIdleSize());
		serversConfig.setIdleConnectionTimeout(properties.getConnectionTimeout());
		serversConfig.setConnectTimeout(properties.getConnectionTimeout());
		serversConfig.setTimeout(properties.getTimeout());
		return config;
	}

	private static Config clusterConfig(MicaLockProperties properties) {
		Config config = new Config();
		ClusterServersConfig serversConfig = config.useClusterServers();
		serversConfig.addNodeAddress(properties.getNodeAddress());
		String password = properties.getPassword();
		if (StringUtil.isNotBlank(password)) {
			serversConfig.setPassword(password);
		}
		serversConfig.setMasterConnectionPoolSize(properties.getPoolSize());
		serversConfig.setMasterConnectionMinimumIdleSize(properties.getIdleSize());
		serversConfig.setSlaveConnectionPoolSize(properties.getPoolSize());
		serversConfig.setSlaveConnectionMinimumIdleSize(properties.getIdleSize());
		serversConfig.setIdleConnectionTimeout(properties.getConnectionTimeout());
		serversConfig.setConnectTimeout(properties.getConnectionTimeout());
		serversConfig.setTimeout(properties.getTimeout());
		return config;
	}

	@Bean
	@ConditionalOnMissingBean
	public RedisLockClient redisLockClient(MicaLockProperties properties) {
		return new RedisLockClientImpl(redissonClient(properties));
	}

	@Bean
	@ConditionalOnMissingBean
	public RedisLockAspect redisLockAspect(RedisLockClient redisLockClient) {
		return new RedisLockAspect(redisLockClient);
	}

	private static RedissonClient redissonClient(MicaLockProperties properties) {
		MicaLockProperties.Mode mode = properties.getMode();
		Config config;
		switch (mode) {
			case sentinel:
				config = sentinelConfig(properties);
				break;
			case cluster:
				config = clusterConfig(properties);
				break;
			case master:
				config = masterSlaveConfig(properties);
				break;
			case single:
				config = singleConfig(properties);
				break;
			default:
				config = new Config();
				break;
		}
		return Redisson.create(config);
	}

}
