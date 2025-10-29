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

import lombok.experimental.UtilityClass;
import okhttp3.ResponseBody;
import org.jspecify.annotations.Nullable;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;

/**
 * 兼容 okhttp-jvm
 *
 * @author L.cm
 */
@UtilityClass
public class Util {
	public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
	public static final ResponseBody EMPTY_RESPONSE = ResponseBody.create(EMPTY_BYTE_ARRAY, null);

	/**
	 * closeQuietly
	 *
	 * @param closeable 自动关闭
	 */
	public static void closeQuietly(@Nullable Closeable closeable) {
		if (closeable == null) {
			return;
		}
		if (closeable instanceof Flushable flushable) {
			try {
				flushable.flush();
			} catch (IOException ignored) {
				// ignore
			}
		}
		try {
			closeable.close();
		} catch (IOException ignored) {
			// ignore
		}
	}

}
