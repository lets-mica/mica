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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;

/**
 * 异常类型
 *
 * @author L.cm
 */
@Getter
@RequiredArgsConstructor
public enum ErrorType {
	/**
	 * rest 请求异常
	 */
	REQUEST("request"),
	/**
	 * 异步异常
	 */
	ASYNC("async"),
	/**
	 * 定时任务异常
	 */
	SCHEDULER("scheduler"),
	/**
	 * websocket 异常
	 */
	WEB_SOCKET("websocket"),
	/**
	 * 其他异常
	 */
	OTHER("other");

	@JsonValue
	private final String type;

	@Nullable
	@JsonCreator
	public static ErrorType of(String type) {
		ErrorType[] values = ErrorType.values();
		for (ErrorType errorType : values) {
			if (errorType.type.equals(type)) {
				return errorType;
			}
		}
		return null;
	}

}
