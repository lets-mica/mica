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

package net.dreamlu.mica.core.compiler;

import net.dreamlu.mica.core.function.CheckedFunction;
import net.dreamlu.mica.core.utils.CollectionUtil;
import net.dreamlu.mica.core.utils.Unchecked;

import java.security.SecureClassLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 内存的代码 class loader，参考自 oracle jdk
 *
 * @author L.cm
 */
public class ByteCodeLoader extends SecureClassLoader {
	/**
	 * Map which represents class name and its compiled java object
	 */
	private static final ConcurrentMap<String, Class<?>> JAVA_FILE_OBJECT_MAP = new ConcurrentHashMap<>();
	private final String className;
	private final byte[] byteCode;

	/**
	 * Creates a new {@code ByteCodeLoader} ready to load a class with the
	 * given name and the given byte code.
	 *
	 * @param className The name of the class
	 * @param byteCode  The byte code of the class
	 */
	public ByteCodeLoader(String className, byte[] byteCode) {
		this.className = className;
		this.byteCode = byteCode;
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		if (!name.equals(className)) {
			throw new ClassNotFoundException(name);
		}

		return defineClass(name, byteCode, 0, byteCode.length);
	}

	/**
	 * Utility method for creating a new {@code ByteCodeLoader} and then
	 * directly load the given byte code.
	 *
	 * @param className The name of the class
	 * @param byteCode  The byte code for the class
	 * @return A {@see Class} object representing the class
	 */
	public static Class<?> load(String className, byte[] byteCode) {
		CheckedFunction<String, Class<?>> classLoadFunc = (key) -> new ByteCodeLoader(key, byteCode).loadClass(className);
		return CollectionUtil.computeIfAbsent(JAVA_FILE_OBJECT_MAP, className, Unchecked.function(classLoadFunc));
	}

	/**
	 * Utility method for creating a new {@code ByteCodeLoader} and then
	 * directly load the given byte code.
	 *
	 * @param className  The name of the class
	 * @param sourceCode The source code for the class with name {@code className}
	 * @return A {@see Class} object representing the class
	 */
	public static Class<?> load(String className, CharSequence sourceCode) {
		return load(className, InMemoryJavaCompiler.compile(className, sourceCode));
	}

}
