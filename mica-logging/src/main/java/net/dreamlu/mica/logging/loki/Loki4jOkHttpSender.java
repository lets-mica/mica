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

package net.dreamlu.mica.logging.loki;

import com.github.loki4j.client.http.HttpConfig;
import com.github.loki4j.client.http.Loki4jHttpClient;
import com.github.loki4j.logback.HttpSender;

import java.util.function.Function;

/**
 * Loki sender that is backed by OkHttp
 *
 * @author L.cm
 */
public class Loki4jOkHttpSender implements HttpSender {

	@Override
	public Function<HttpConfig, Loki4jHttpClient> getHttpClientFactory() {
		return Loki4jOkHttpClient::new;
	}

	@Override
	public HttpConfig.Builder getConfig() {
		return HttpConfig.builder();
	}
}
