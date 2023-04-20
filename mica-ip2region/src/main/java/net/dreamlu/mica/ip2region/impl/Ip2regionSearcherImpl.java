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
import net.dreamlu.mica.core.utils.Exceptions;
import net.dreamlu.mica.ip2region.config.Ip2regionProperties;
import net.dreamlu.mica.ip2region.core.Ip2regionSearcher;
import net.dreamlu.mica.ip2region.core.IpInfo;
import net.dreamlu.mica.ip2region.core.IpV6Searcher;
import net.dreamlu.mica.ip2region.core.Searcher;
import net.dreamlu.mica.ip2region.utils.IpInfoUtil;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * ip2region 初始化
 *
 * @author dream.lu
 */
@RequiredArgsConstructor
public class Ip2regionSearcherImpl implements InitializingBean, DisposableBean, Ip2regionSearcher {
	private final ResourceLoader resourceLoader;
	private final Ip2regionProperties properties;
	private Searcher searcher;
	private IpV6Searcher ipV6Searcher;

	@Override
	public IpInfo memorySearch(long ip) {
		try {
			return IpInfoUtil.toIpInfo(searcher.search(ip));
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	@Override
	public IpInfo memorySearch(String ip) {
		// 1. ipv4
		String[] ipV4Part = IpInfoUtil.getIpV4Part(ip);
		if (ipV4Part.length == 4) {
			return memorySearch(Searcher.getIpAdder(ipV4Part));
		}
		// 2. 非 ipv6
		if (!ip.contains(":")) {
			throw new IllegalArgumentException("invalid ip address `" + ip + "`");
		}
		// 3. ipv6
		return ipV6Searcher.query(ip);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Resource resource = resourceLoader.getResource(properties.getDbFileLocation());
		try (InputStream inputStream = resource.getInputStream()) {
			this.searcher = Searcher.newWithBuffer(StreamUtils.copyToByteArray(inputStream));
		}
		Resource ipV6Resource = resourceLoader.getResource(properties.getIpv6dbFileLocation());
		try (InputStream inputStream = ipV6Resource.getInputStream()) {
			this.ipV6Searcher = IpV6Searcher.newWithBuffer(StreamUtils.copyToByteArray(inputStream));
		}
	}

	@Override
	public void destroy() throws Exception {
		if (this.searcher != null) {
			this.searcher.close();
		}
	}

}
