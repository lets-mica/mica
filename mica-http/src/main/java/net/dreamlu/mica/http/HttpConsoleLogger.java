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

import javax.annotation.Nonnull;

/**
 * OkHttp console log.
 *
 * @author L.cm
 */
public enum HttpConsoleLogger implements HttpLoggingInterceptor.Logger {
	/**
	 * 实例
	 */
	INSTANCE;

	public void log(@Nonnull String message) {
		// 统一添加前缀，方便在茫茫日志中查看
		System.out.println("HttpLogger: " + message);
	}

}
