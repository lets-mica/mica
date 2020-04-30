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

import net.dreamlu.mica.core.utils.CharPool;
import org.springframework.util.FastByteArrayOutputStream;

import javax.tools.*;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject.Kind;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Collections;

/**
 * 内存的代码编译器，参考自 oracle jdk
 *
 * @author L.cm
 */
public class InMemoryJavaCompiler {
	/**
	 * JavaCompiler
	 */
	private static final JavaCompiler COMPILER = ToolProvider.getSystemJavaCompiler();

	/**
	 * Compiles the class with the given name and source code.
	 *
	 * @param className  The name of the class
	 * @param sourceCode The source code for the class with name {@code className}
	 * @return The resulting byte code from the compilation
	 * @throws IllegalArgumentException if the compilation did not succeed
	 */
	public static byte[] compile(String className, CharSequence sourceCode) {
		MemoryJavaFileObject file = new MemoryJavaFileObject(className, sourceCode);
		CompilationTask task = getCompilationTask(file);

		if (!task.call()) {
			throw new IllegalArgumentException("Could not compile " + className + " with source code :\t" + sourceCode);
		}

		return file.getByteCode();
	}

	private static CompilationTask getCompilationTask(MemoryJavaFileObject file) {
		return COMPILER.getTask(null, new FileManagerWrapper(file), null, null, null, Collections.singletonList(file));
	}

	private static class MemoryJavaFileObject extends SimpleJavaFileObject {
		private final String className;
		private final CharSequence sourceCode;
		private final FastByteArrayOutputStream byteCode;

		public MemoryJavaFileObject(String className, CharSequence sourceCode) {
			super(URI.create("string:///" + className.replace(CharPool.DOT, CharPool.SLASH) + Kind.SOURCE.extension), Kind.SOURCE);
			this.className = className;
			this.sourceCode = sourceCode;
			this.byteCode = new FastByteArrayOutputStream();
		}

		@Override
		public CharSequence getCharContent(boolean ignoreEncodingErrors) {
			return sourceCode;
		}

		@Override
		public OutputStream openOutputStream() throws IOException {
			return byteCode;
		}

		public byte[] getByteCode() {
			return byteCode.toByteArray();
		}

		public String getClassName() {
			return className;
		}
	}

	private static class FileManagerWrapper extends ForwardingJavaFileManager<StandardJavaFileManager> {
		private final MemoryJavaFileObject file;

		public FileManagerWrapper(MemoryJavaFileObject file) {
			super(COMPILER.getStandardFileManager(null, null, null));
			this.file = file;
		}

		@Override
		public JavaFileObject getJavaFileForOutput(Location location, String className, Kind kind, FileObject sibling) throws IOException {
			if (!file.getClassName().equals(className)) {
				throw new IOException("Expected class with name " + file.getClassName() + ", but got " + className);
			}
			return file;
		}
	}

}
