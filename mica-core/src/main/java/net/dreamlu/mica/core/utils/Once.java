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

package net.dreamlu.mica.core.utils;

import org.springframework.lang.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 加载一次
 *
 * @author L.cm
 */
public class Once {
	private final AtomicBoolean value;

	public Once() {
		this.value = new AtomicBoolean(false);
	}

	/**
	 * 是否可以执行
	 *
	 * @return 是否可以执行
	 */
	public boolean canRun() {
		return value.compareAndSet(false, true);
	}

	/**
	 * 执行函数
	 *
	 * @param consumer Consumer
	 * @param argument 参数
	 * @param <T>      泛型
	 */
	public <T> void run(Consumer<T> consumer, T argument) {
		if (canRun()) {
			consumer.accept(argument);
		}
	}

	/**
	 * 执行函数
	 *
	 * @param function Function
	 * @param argument 参数
	 * @param <T>      泛型
	 * @param <R>      泛型
	 * @return 返回值，不可执行返回 null
	 */
	@Nullable
	public <T, R> R run(Function<T, R> function, T argument) {
		if (canRun()) {
			return function.apply(argument);
		}
		return null;
	}

}
