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

package net.dreamlu.mica.redis;

import lombok.Getter;
import lombok.ToString;

import javax.annotation.Nullable;

/**
 * cache key 封装
 *
 * @author L.cm
 */
@Getter
@ToString
public class CacheKey {
	/**
	 * redis key
	 */
	@Nullable
	private String key;
	/**
	 * 超时时间 秒
	 */
	@Nullable
	private Long seconds;

	public CacheKey(String key) {
		this.key = key;
	}

	public CacheKey(String key, Long seconds) {
		this.key = key;
		this.seconds = seconds;
	}
}
