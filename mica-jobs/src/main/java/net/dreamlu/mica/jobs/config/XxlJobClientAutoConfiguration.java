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

package net.dreamlu.mica.jobs.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.jobs.properties.XxlJobClientProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.dreamlu.mica.jobs.properties.XxlJobClientProperties.XxlJobAdminProps;
import static net.dreamlu.mica.jobs.properties.XxlJobClientProperties.XxlJobExecutorProps;

/**
 * xxl-job client config
 *
 * @author L.cm
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(XxlJobClientProperties.class)
@ConditionalOnProperty(
	prefix = XxlJobClientProperties.PREFIX,
	name = "enabled",
	havingValue = "true",
	matchIfMissing = true
)
public class XxlJobClientAutoConfiguration {
	private static final String LB_PREFIX = "lb://";

	@Bean
	public XxlJobSpringExecutor xxlJobExecutor(XxlJobClientProperties properties,
											   DiscoveryClient discoveryClient,
											   Environment environment,
											   InetUtils inetUtils) {
		log.info(">>>>>>>>>>> xxl-job client config init.");
		XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
		XxlJobAdminProps admin = properties.getAdmin();
		xxlJobSpringExecutor.setAccessToken(admin.getAccessToken());
		// 获取 admin 管理端的地址
		xxlJobSpringExecutor.setAdminAddresses(getAdminServiceUrl(discoveryClient, admin));

		XxlJobExecutorProps executor = properties.getExecutor();
		// 微服务 服务名
		String serviceId = getAppName(environment);
		xxlJobSpringExecutor.setAppname(getExecutorName(executor, serviceId));
		String ipAddress = executor.getIp();
		if (!StringUtils.hasText(ipAddress)) {
			ipAddress = inetUtils.findFirstNonLoopbackHostInfo().getIpAddress();
		}
		xxlJobSpringExecutor.setIp(ipAddress);
		xxlJobSpringExecutor.setPort(executor.getPort());
		xxlJobSpringExecutor.setLogPath(getLogPath(executor, environment, serviceId));
		xxlJobSpringExecutor.setLogRetentionDays(executor.getLogRetentionDays());
		return xxlJobSpringExecutor;
	}

	private static String getAdminServiceUrl(DiscoveryClient discoveryClient, XxlJobAdminProps admin) {
		String addresses = admin.getAddress();
		Assert.hasText(addresses, "xxl-job admin addresses is empty.");
		// 处理 context-path，trim，补充前缀
		String contextPath = Optional.ofNullable(admin.getContextPath())
			.map(String::trim)
			.map(path -> path.startsWith("/") ? path : "/" + path)
			.orElse("");
		return StringUtils.commaDelimitedListToSet(addresses).stream()
			.flatMap(address -> {
				// lb://xxx
				if (address.startsWith(LB_PREFIX)) {
					return discoveryClient.getInstances(address.substring(LB_PREFIX.length()))
						.stream()
						.map(ServiceInstance::getUri)
						.map(URI::toString);
				}
				// ip:port，补充 http:// 前缀
				if (!address.startsWith("http")) {
					address = "http://" + address;
				}
				// 其他 protocol://ip:port
				return Stream.of(address);
			}).map(url -> url + contextPath)
			.collect(Collectors.joining(","));
	}

	private static String getExecutorName(XxlJobExecutorProps executor, String serviceId) {
		String appName = executor.getAppName();
		if (StringUtils.hasText(appName)) {
			return appName;
		}
		return serviceId;
	}

	private static String getLogPath(XxlJobExecutorProps executor, Environment environment, String serviceId) {
		String logPath = executor.getLogPath();
		if (!StringUtils.hasText(logPath)) {
			logPath = environment.getProperty("LOGGING_PATH", "logs")
				.concat("/").concat(serviceId).concat("/jobs");
		}
		return logPath;
	}

	private static String getAppName(Environment environment) {
		return environment.getProperty("spring.application.name", "");
	}

}
