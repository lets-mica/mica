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

package net.dreamlu.mica.ip2region.utils;

import net.dreamlu.mica.core.utils.StringPool;
import net.dreamlu.mica.ip2region.core.IpInfo;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * ip 信息详情
 *
 * @author L.cm
 */
public class IpInfoUtil {
	private static final Pattern SPLIT_PATTERN = Pattern.compile("\\|");

	/**
	 * 将 DataBlock 转化为 IpInfo
	 *
	 * @param region region
	 * @return IpInfo
	 */
	@Nullable
	public static IpInfo toIpInfo(@Nullable String region) {
		if (region == null) {
			return null;
		}
		IpInfo ipInfo = new IpInfo();
		String[] splitInfoArr = SPLIT_PATTERN.split(region);
		// 补齐5位
		if (splitInfoArr.length < 5) {
			splitInfoArr = Arrays.copyOf(splitInfoArr, 5);
		}
		ipInfo.setCountry(filterZero(splitInfoArr[0]));
		ipInfo.setProvince(filterZero(splitInfoArr[1]));
		ipInfo.setCity(filterZero(splitInfoArr[2]));
		ipInfo.setIsp(filterZero(splitInfoArr[3]));
		ipInfo.setIsoCode(filterZero(splitInfoArr[4]));
		return ipInfo;
	}

	/**
	 * 数据过滤，因为 ip2Region 采用 0 填充的没有数据的字段
	 *
	 * @param info info
	 * @return info
	 */
	@Nullable
	private static String filterZero(@Nullable String info) {
		// null 或 0 返回 null
		if (info == null || StringPool.ZERO.equals(info)) {
			return null;
		}
		return info;
	}

	/**
	 * 读取 IpInfo
	 *
	 * @param ipInfo   IpInfo
	 * @param function Function
	 * @return info
	 */
	@Nullable
	public static String readInfo(@Nullable IpInfo ipInfo, Function<IpInfo, String> function) {
		if (ipInfo == null) {
			return null;
		}
		return function.apply(ipInfo);
	}
}
