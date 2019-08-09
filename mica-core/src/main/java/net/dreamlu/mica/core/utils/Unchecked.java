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

import lombok.experimental.UtilityClass;
import net.dreamlu.mica.core.function.*;

import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Lambda 受检异常处理
 *
 * <p>
 * https://segmentfault.com/a/1190000007832130
 * https://github.com/jOOQ/jOOL
 * </p>
 *
 * @author L.cm
 */
@UtilityClass
public class Unchecked {

	public static <T, R> Function<T, R> function(CheckedFunction<T, R> mapper) {
		Objects.requireNonNull(mapper);
		return t -> {
			try {
				return mapper.apply(t);
			} catch (Throwable e) {
				throw Exceptions.unchecked(e);
			}
		};
	}

	public static <T> Consumer<T> consumer(CheckedConsumer<T> mapper) {
		Objects.requireNonNull(mapper);
		return t -> {
			try {
				mapper.accept(t);
			} catch (Throwable e) {
				throw Exceptions.unchecked(e);
			}
		};
	}

	public static <T> Supplier<T> supplier(CheckedSupplier<T> mapper) {
		Objects.requireNonNull(mapper);
		return () -> {
			try {
				return mapper.get();
			} catch (Throwable e) {
				throw Exceptions.unchecked(e);
			}
		};
	}

	public static Runnable runnable(CheckedRunnable runnable) {
		Objects.requireNonNull(runnable);
		return () -> {
			try {
				runnable.run();
			} catch (Throwable e) {
				throw Exceptions.unchecked(e);
			}
		};
	}

	public static <T> Callable<T> callable(CheckedCallable<T> callable) {
		Objects.requireNonNull(callable);
		return () -> {
			try {
				return callable.call();
			} catch (Throwable e) {
				throw Exceptions.unchecked(e);
			}
		};
	}

	public static <T> Comparator<T> comparator(CheckedComparator<T> comparator) {
		Objects.requireNonNull(comparator);
		return (T o1, T o2) -> {
			try {
				return comparator.compare(o1, o2);
			} catch (Throwable e) {
				throw Exceptions.unchecked(e);
			}
		};
	}

}
