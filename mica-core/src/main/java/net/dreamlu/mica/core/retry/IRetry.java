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

import net.dreamlu.mica.core.function.CheckedCallable;

/**
 * 重试接口
 *
 * @author L.cm
 */
public interface IRetry {

	/**
	 * Execute the supplied {@link CheckedCallable} with the configured retry semantics. See
	 * implementations for configuration details.
	 *
	 * @param <T>           the return value
	 * @param retryCallback the {@link CheckedCallable}
	 * @param <E>           the exception thrown
	 * @return T the return value
	 * @throws E the exception thrown
	 */
	<T, E extends Throwable> T execute(RetryCallback<T, E> retryCallback) throws E;

}
