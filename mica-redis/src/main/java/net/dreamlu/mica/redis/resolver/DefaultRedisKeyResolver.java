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

package net.dreamlu.mica.redis.resolver;

import lombok.RequiredArgsConstructor;
import net.dreamlu.mica.core.utils.CharPool;
import net.dreamlu.mica.core.utils.StringUtil;
import net.dreamlu.mica.redis.config.MicaRedisProperties;

/**
 * 默认的 key 处理，添加配置的 key 前缀
 *
 * @author L.cm
 */
@RequiredArgsConstructor
public class DefaultRedisKeyResolver implements RedisKeyResolver {
	private final MicaRedisProperties properties;

	@Override
	public String resolve(String key) {
		String keyPrefix = properties.getKeyPrefix();
		return StringUtil.isBlank(keyPrefix) ? key : keyPrefix + CharPool.COLON + key;
	}

}
