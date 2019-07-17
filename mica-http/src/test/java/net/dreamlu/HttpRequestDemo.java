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

package net.dreamlu;

import net.dreamlu.http.HttpRequest;
import net.dreamlu.http.HttpResponse;
import net.dreamlu.http.LogLevel;
import net.dreamlu.http.ResponseSpec;
import net.dreamlu.mica.core.utils.Base64Util;

import java.net.URI;
import java.time.Duration;
import java.util.Optional;

/**
 * This example of mica http
 */
public class HttpRequestDemo {

	public static void main(String[] args) {
		HttpRequest.setGlobalLog(LogLevel.BODY);

		// Execute a GET with timeout settings and return response content as String.
		String html = HttpRequest.get("www.baidu.com")
			.connectTimeout(Duration.ofSeconds(1000))
			.query("test", "a")
			.query("name", "張三")
			.query("x", 1)
			.query("abd", Base64Util.encode("123&$#%"))
			.queryEncoded("abc", Base64Util.encode("123&$#%"))
			.execute()
			.onFailed(((request, e) -> {
				e.printStackTrace();
			}))
			.onSuccess(ResponseSpec::asString);
		System.out.println(html);

		// Execute a POST with the 'expect-continue' handshake, using HTTP/1.1,
		// containing a request body as String and return response content as byte array.
		Optional<String> opt = HttpRequest.post(URI.create("https://www.baidu.com"))
			.bodyString("Important stuff")
			.formBuilder()
			.add("a", "b")
			.execute()
			.onSuccessOpt(ResponseSpec::asString);

		// Execute a POST with a custom header through the proxy containing a request body
		// as an HTML form and save the result to the file
		HttpResponse httpResponse = HttpRequest.post("https://www.baidu.com/some-form")
			.addHeader("X-Custom-header", "stuff")
			.execute();
		System.out.println(httpResponse);

		// async，异步执行结果
		HttpRequest.get("https://www.baidu.com/some-form")
			.async()
			.onSuccessful(System.out::println)
			.onFailed((request, e) -> {
				e.printStackTrace();
			});
	}

}
