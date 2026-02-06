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

package net.dreamlu.mica.ip2region.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.ip2region.config.Config;
import net.dreamlu.mica.ip2region.config.Ip2regionProperties;
import net.dreamlu.mica.ip2region.core.Ip2Region;
import net.dreamlu.mica.ip2region.core.Ip2regionSearcher;
import net.dreamlu.mica.ip2region.core.IpInfo;
import net.dreamlu.mica.ip2region.utils.IpInfoUtil;
import net.dreamlu.mica.ip2region.xdb.InetAddressException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;

/**
 * ip2region 初始化
 *
 * @author dream.lu
 */
@Slf4j
@RequiredArgsConstructor
public class Ip2regionSearcherImpl implements InitializingBean, DisposableBean, Ip2regionSearcher {
	private final ResourceLoader resourceLoader;
	private final Ip2regionProperties properties;
	/**
	 * ip2region 实例
	 */
	private Ip2Region ip2Region;

	@Override
	public IpInfo memorySearch(String ip) {
		try {
			String region = ip2Region.search(ip);
			return IpInfoUtil.toIpInfo(region);
		} catch (InetAddressException | IOException | InterruptedException e) {
			log.error("ip2region memorySearch error, ip: {}", ip, e);
			return null;
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Resource ipv4Resource = resourceLoader.getResource(properties.getIpv4xdbFileLocation());
		Resource ipV6Resource = resourceLoader.getResource(properties.getIpv6xdbFileLocation());
		try (
			InputStream v4InputStream = ipv4Resource.getInputStream();
			InputStream v6InputStream = ipV6Resource.getInputStream()
		) {
			Config v4Config = Config.custom()
				.setXdbInputStream(v4InputStream)
				.setCachePolicy(Config.BufferCache)
				.setSearchers(10)
				.asV4();
			Config v6Config = Config.custom()
				.setXdbInputStream(v6InputStream)
				.setCachePolicy(Config.BufferCache)
				.setSearchers(10)
				.asV6();
			ip2Region = Ip2Region.create(v4Config, v6Config);
		}
	}

	@Override
	public void destroy() throws Exception {
		ip2Region.close();
	}

}
