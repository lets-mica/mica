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

package net.dreamlu.mica.ip2region.core;

import net.dreamlu.mica.ip2region.utils.IpInfoUtil;
import org.springframework.lang.Nullable;

import java.util.function.Function;

/**
 * ip 搜索器
 *
 * @author dream.lu
 */
public interface Ip2regionSearcher {

	/**
	 * ip 位置 搜索
	 *
	 * @param ip ip
	 * @return 位置
	 */
	@Nullable
	IpInfo memorySearch(long ip);

	/**
	 * ip 位置 搜索
	 *
	 * @param ip ip
	 * @return 位置
	 */
	@Nullable
	IpInfo memorySearch(String ip);

	/**
	 * ip 位置 搜索
	 *
	 * @param ptr ptr
	 * @return 位置
	 */
	@Nullable
	IpInfo getByIndexPtr(long ptr);

	/**
	 * ip 位置 搜索
	 *
	 * @param ip ip
	 * @return 位置
	 */
	@Nullable
	IpInfo btreeSearch(long ip);

	/**
	 * ip 位置 搜索
	 *
	 * @param ip ip
	 * @return 位置
	 */
	@Nullable
	IpInfo btreeSearch(String ip);

	/**
	 * ip 位置 搜索
	 *
	 * @param ip ip
	 * @return 位置
	 */
	@Nullable
	IpInfo binarySearch(long ip);

	/**
	 * ip 位置 搜索
	 *
	 * @param ip ip
	 * @return 位置
	 */
	@Nullable
	IpInfo binarySearch(String ip);

	/**
	 * 读取 ipInfo 中的信息
	 *
	 * @param ip       ip
	 * @param function Function
	 * @return 地址
	 */
	@Nullable
	default String getInfo(long ip, Function<IpInfo, String> function) {
		return IpInfoUtil.readInfo(memorySearch(ip), function);
	}

	/**
	 * 读取 ipInfo 中的信息
	 *
	 * @param ip       ip
	 * @param function Function
	 * @return 地址
	 */
	@Nullable
	default String getInfo(String ip, Function<IpInfo, String> function) {
		return IpInfoUtil.readInfo(memorySearch(ip), function);
	}

	/**
	 * 获取地址信息
	 *
	 * @param ip ip
	 * @return 地址
	 */
	@Nullable
	default String getAddress(long ip) {
		return getInfo(ip, IpInfo::getAddress);
	}

	/**
	 * 获取地址信息
	 *
	 * @param ip ip
	 * @return 地址
	 */
	@Nullable
	default String getAddress(String ip) {
		return getInfo(ip, IpInfo::getAddress);
	}

	/**
	 * 获取地址信息包含 isp
	 *
	 * @param ip ip
	 * @return 地址
	 */
	@Nullable
	default String getAddressAndIsp(long ip) {
		return getInfo(ip, IpInfo::getAddressAndIsp);
	}

	/**
	 * 获取地址信息包含 isp
	 *
	 * @param ip ip
	 * @return 地址
	 */
	@Nullable
	default String getAddressAndIsp(String ip) {
		return getInfo(ip, IpInfo::getAddressAndIsp);
	}

}
