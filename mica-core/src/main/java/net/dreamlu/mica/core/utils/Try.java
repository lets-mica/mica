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
import org.springframework.lang.Nullable;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Lambda 受检异常处理
 * https://segmentfault.com/a/1190000007832130
 *
 * @author L.cm
 */
@UtilityClass
public class Try {

	public static <T, R> Function<T, R> of(UncheckedFunction<T, R> mapper) {
		Objects.requireNonNull(mapper);
		return t -> {
			try {
				return mapper.apply(t);
			} catch (Exception e) {
				throw Exceptions.unchecked(e);
			}
		};
	}

	public static <T> Consumer<T> of(UncheckedConsumer<T> mapper) {
		Objects.requireNonNull(mapper);
		return t -> {
			try {
				mapper.accept(t);
			} catch (Exception e) {
				throw Exceptions.unchecked(e);
			}
		};
	}

	public static <T> Supplier<T> of(UncheckedSupplier<T> mapper) {
		Objects.requireNonNull(mapper);
		return () -> {
			try {
				return mapper.get();
			} catch (Exception e) {
				throw Exceptions.unchecked(e);
			}
		};
	}

	@FunctionalInterface
	public interface UncheckedFunction<T, R> {
		@Nullable
		R apply(@Nullable T t) throws Exception;
	}

	@FunctionalInterface
	public interface UncheckedConsumer<T> {
		@Nullable
		void accept(@Nullable T t) throws Exception;
	}

	@FunctionalInterface
	public interface UncheckedSupplier<T> {
		@Nullable
		T get() throws Exception;
	}
}
