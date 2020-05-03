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

package net.dreamlu.mica.redis.ratelimiter;

import lombok.Getter;

import java.util.concurrent.TimeUnit;

/**
 * 限流异常
 *
 * @author L.cm
 */
@Getter
public class RateLimiterException extends RuntimeException {
	private final String key;
	private final long max;
	private final long ttl;
	private final TimeUnit timeUnit;

	public RateLimiterException(String key, long max, long ttl, TimeUnit timeUnit) {
		super(String.format("您的访问次数已超限：%s，速率：%d/%ds", key, max, timeUnit.toSeconds(ttl)));
		this.key = key;
		this.max = max;
		this.ttl = ttl;
		this.timeUnit = timeUnit;
	}
}
