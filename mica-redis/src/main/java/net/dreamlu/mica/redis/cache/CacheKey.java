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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.lang.Nullable;

import java.time.Duration;

/**
 * cache key 封装
 *
 * @author L.cm
 */
@Getter
@ToString
@RequiredArgsConstructor
public class CacheKey {

	/**
	 * redis key
	 */
	private final String key;

	/**
	 * 超时时间 秒
	 */
	@Nullable
	private final Duration expire;

	public CacheKey(String key) {
		this(key, null);
	}

}
