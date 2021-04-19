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
import net.dreamlu.mica.ip2region.core.DbConfig;
import net.dreamlu.mica.ip2region.core.DbSearcher;
import net.dreamlu.mica.ip2region.core.Ip2regionSearcher;
import net.dreamlu.mica.ip2region.core.IpInfo;
import net.dreamlu.mica.ip2region.utils.IpInfoUtil;
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
public class Ip2regionSearcherImpl implements InitializingBean, Ip2regionSearcher {
	private final ResourceLoader resourceLoader;
	private final Ip2regionProperties properties;
	private DbSearcher searcher;

	@Override
	public void afterPropertiesSet() throws Exception {
		DbConfig config = new DbConfig();
		Resource resource = resourceLoader.getResource(properties.getDbFileLocation());
		try (InputStream inputStream = resource.getInputStream()) {
			this.searcher = new DbSearcher(config, new ByteArrayDBReader(StreamUtils.copyToByteArray(inputStream)));
		}
	}

	@Override
	public IpInfo memorySearch(long ip) {
		try {
			return IpInfoUtil.toIpInfo(searcher.memorySearch(ip));
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	@Override
	public IpInfo memorySearch(String ip) {
		try {
			return IpInfoUtil.toIpInfo(searcher.memorySearch(ip));
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	@Override
	public IpInfo getByIndexPtr(long ptr) {
		try {
			return IpInfoUtil.toIpInfo(searcher.getByIndexPtr(ptr));
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	@Override
	public IpInfo btreeSearch(long ip) {
		try {
			return IpInfoUtil.toIpInfo(searcher.btreeSearch(ip));
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	@Override
	public IpInfo btreeSearch(String ip) {
		try {
			return IpInfoUtil.toIpInfo(searcher.btreeSearch(ip));
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	@Override
	public IpInfo binarySearch(long ip) {
		try {
			return IpInfoUtil.toIpInfo(searcher.binarySearch(ip));
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	@Override
	public IpInfo binarySearch(String ip) {
		try {
			return IpInfoUtil.toIpInfo(searcher.binarySearch(ip));
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

}
