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

package net.dreamlu.mica.redis.cache;

import net.dreamlu.mica.core.utils.ObjectUtil;
import net.dreamlu.mica.core.utils.StringPool;
import net.dreamlu.mica.core.utils.StringUtil;

import javax.annotation.Nullable;
import java.time.Duration;

/**
 * cache key
 *
 * @author L.cm
 */
public interface ICacheKey {

	/**
	 * 获取前缀
	 *
	 * @return key 前缀
	 */
	String getPrefix();

	/**
	 * 超时时间
	 *
	 * @return 超时时间
	 */
	@Nullable
	default Duration getExpire() {
		return null;
	}

	/**
	 * 组装 cache key
	 *
	 * @param suffix 参数
	 * @return cache key
	 */
	default String getKeyStr(Object... suffix) {
		String prefix = this.getPrefix();
		// 拼接参数
		if (ObjectUtil.isEmpty(suffix)) {
			return prefix;
		}
		return prefix.concat(StringUtil.join(suffix, StringPool.COLON));
	}

	/**
	 * 组装 cache key
	 *
	 * @param suffix 参数
	 * @return cache key
	 */
	default CacheKey getKey(Object... suffix) {
		String key = this.getKeyStr(suffix);
		Duration expire = this.getExpire();
		return expire == null ? new CacheKey(key) : new CacheKey(key, expire);
	}

}
