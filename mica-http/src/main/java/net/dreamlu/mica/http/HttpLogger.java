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

import lombok.extern.slf4j.Slf4j;
import okhttp3.internal.annotations.EverythingIsNonNull;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * OkHttp logger, Slf4j and console log.
 *
 * @author L.cm
 */
public enum HttpLogger {

	/**
	 * http 日志：Slf4j
	 */
	Slf4j(new Slf4jLogger()),
	/**
	 * http 日志：Console
	 */
	Console(new ConsoleLogger());

	private final HttpLoggingInterceptor.Logger logger;

	HttpLogger(HttpLoggingInterceptor.Logger logger) {
		this.logger = logger;
	}

	public HttpLoggingInterceptor.Logger getLogger() {
		return logger;
	}

	/**
	 * Slf4j日志
	 */
	@Slf4j
	@EverythingIsNonNull
	public static class Slf4jLogger implements HttpLoggingInterceptor.Logger {
		@Override
		public void log(String message) {
			log.info(message);
		}
	}

	/**
	 * 控制台日志
	 */
	@EverythingIsNonNull
	public static class ConsoleLogger implements HttpLoggingInterceptor.Logger {
		@Override
		public void log(String message) {
			// 统一添加前缀，方便在茫茫日志中查看
			System.out.print("ConsoleLogger: ");
			System.out.println(message);
		}
	}

}
