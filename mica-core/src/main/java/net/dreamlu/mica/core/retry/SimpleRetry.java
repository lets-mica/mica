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

package net.dreamlu.mica.core.retry;

import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.utils.Exceptions;
import net.dreamlu.mica.core.utils.ThreadUtil;

import java.io.IOException;

/**
 * 简单的 retry 重试
 *
 * @author L.cm
 */
@Slf4j
public final class SimpleRetry implements IRetry {
	/**
	 * The default limit to the number of attempts for a new policy.
	 */
	public final static int DEFAULT_MAX_ATTEMPTS = 3;
	/**
	 * Default back off period - 1ms.
	 */
	private static final long DEFAULT_BACK_OFF_PERIOD = 1L;

	/**
	 * 重试次数
	 */
	private final int maxAttempts;
	/**
	 * 重试时间间隔
	 */
	private final long sleepMillis;

	public SimpleRetry() {
		this(DEFAULT_MAX_ATTEMPTS, DEFAULT_BACK_OFF_PERIOD);
	}

	public SimpleRetry(int maxAttempts) {
		this(maxAttempts, DEFAULT_BACK_OFF_PERIOD);
	}

	public SimpleRetry(int maxAttempts, long sleepMillis) {
		this.maxAttempts = maxAttempts;
		this.sleepMillis = (sleepMillis > 0 ? sleepMillis : 1);
	}

	public int getMaxAttempts() {
		return maxAttempts;
	}

	public long getSleepMillis() {
		return sleepMillis;
	}

	@Override
	public <T, E extends Throwable> T execute(RetryCallback<T, E> retryCallback) throws E {
		int retryCount;
		Throwable lastThrowable = null;
		for (int i = 0; i < maxAttempts; i++) {
			try {
				return retryCallback.call();
			} catch (Throwable e) {
				retryCount = i + 1;
				log.warn("retry on {} times error{}.", retryCount, e.getMessage());
				lastThrowable = e;
				if (sleepMillis > 0 && retryCount < maxAttempts) {
					ThreadUtil.sleep(sleepMillis);
				}
			}
		}
		if (lastThrowable == null) {
			lastThrowable = new IOException("retry on " + maxAttempts + " times,still fail.");
		}
		throw Exceptions.unchecked(lastThrowable);
	}

}
