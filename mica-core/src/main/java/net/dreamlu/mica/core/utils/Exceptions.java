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
import net.dreamlu.mica.core.io.FastStringWriter;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * 异常处理工具类
 *
 * @author L.cm
 */
@UtilityClass
public class Exceptions {

	/**
	 * 将CheckedException转换为UncheckedException.
	 *
	 * @param e Throwable
	 * @return {RuntimeException}
	 */
	public static RuntimeException unchecked(Throwable e) {
		if (e instanceof Error) {
			throw (Error) e;
		} else if (e instanceof IllegalAccessException ||
			e instanceof IllegalArgumentException ||
			e instanceof NoSuchMethodException) {
			return new IllegalArgumentException(e);
		} else if (e instanceof InvocationTargetException) {
			return Exceptions.runtime(((InvocationTargetException) e).getTargetException());
		} else if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		} else if (e instanceof InterruptedException) {
			Thread.currentThread().interrupt();
		}
		return Exceptions.runtime(e);
	}

	/**
	 * 不采用 RuntimeException 包装，直接抛出，使异常更加精准
	 *
	 * @param throwable Throwable
	 * @param <T>       泛型标记
	 * @return Throwable
	 * @throws T 泛型
	 */
	@SuppressWarnings("unchecked")
	private static <T extends Throwable> T runtime(Throwable throwable) throws T {
		throw (T) throwable;
	}

	/**
	 * 代理异常解包
	 *
	 * @param wrapped 包装过得异常
	 * @return 解包后的异常
	 */
	public static Throwable unwrap(Throwable wrapped) {
		Throwable unwrapped = wrapped;
		while (true) {
			if (unwrapped instanceof InvocationTargetException) {
				unwrapped = ((InvocationTargetException) unwrapped).getTargetException();
			} else if (unwrapped instanceof UndeclaredThrowableException) {
				unwrapped = ((UndeclaredThrowableException) unwrapped).getUndeclaredThrowable();
			} else {
				return unwrapped;
			}
		}
	}

	/**
	 * 将ErrorStack转化为String.
	 *
	 * @param ex Throwable
	 * @return {String}
	 */
	public static String getStackTraceAsString(Throwable ex) {
		FastStringWriter stringWriter = new FastStringWriter(512);
		ex.printStackTrace(new PrintWriter(stringWriter));
		return stringWriter.toString();
	}
}
