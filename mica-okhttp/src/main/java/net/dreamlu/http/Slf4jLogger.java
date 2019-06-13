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

package net.dreamlu.http;

import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OkHttp Slf4j logger
 *
 * @author L.cm
 */
public class Slf4jLogger implements HttpLoggingInterceptor.Logger {
	private final Logger logger = LoggerFactory.getLogger(Slf4jLogger.class);

	public static final HttpLoggingInterceptor.Logger INSTANCE = new Slf4jLogger();

	@Override
	public void log(String message) {
		logger.info(message);
	}
}
