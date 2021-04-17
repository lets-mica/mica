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

package net.dreamlu.mica.lite.error;

import lombok.experimental.UtilityClass;
import net.dreamlu.mica.core.utils.Exceptions;
import net.dreamlu.mica.core.utils.ObjectUtil;

import java.time.LocalDateTime;

/**
 * 异常工具类
 *
 * @author L.cm
 */
@UtilityClass
public class ErrorUtil {

	/**
	 * 初始化异常信息
	 *
	 * @param error 异常
	 * @param event 异常事件封装
	 */
	public static void initErrorInfo(Throwable error, MicaErrorEvent event) {
		// 堆栈信息
		event.setStackTrace(Exceptions.getStackTraceAsString(error));
		event.setExceptionName(error.getClass().getName());
		event.setMessage(error.getMessage());
		event.setCreatedAt(LocalDateTime.now());
		StackTraceElement[] elements = error.getStackTrace();
		if (ObjectUtil.isNotEmpty(elements)) {
			// 报错的类信息
			StackTraceElement element = elements[0];
			event.setClassName(element.getClassName());
			event.setFileName(element.getFileName());
			event.setMethodName(element.getMethodName());
			event.setLineNumber(element.getLineNumber());
		}
	}
}
