/*
 * Copyright (c) 2019-2029, Dreamlu (596392912@qq.com & www.dreamlu.net).
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

package net.dreamlu.mica.http;

import lombok.Getter;
import lombok.ToString;

/**
 * 重试策略
 *
 * @author dream.lu
 */
@Getter
@ToString
public class RetryPolicy {
	/**
	 * The default limit to the number of attempts for a new policy.
	 */
	public static final int DEFAULT_MAX_ATTEMPTS = 3;
	public static final RetryPolicy INSTANCE = new RetryPolicy();

	private final int maxAttempts;
	private final long sleepMillis;

	public RetryPolicy() {
		this(RetryPolicy.DEFAULT_MAX_ATTEMPTS, 0L);
	}

	public RetryPolicy(int maxAttempts, long sleepMillis) {
		this.maxAttempts = maxAttempts;
		this.sleepMillis = sleepMillis;
	}
}
