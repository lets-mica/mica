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

import okhttp3.logging.HttpLoggingInterceptor;
import net.dreamlu.http.XRequest;
import net.dreamlu.http.XResponse;

import java.time.Duration;

/**
 * This example of mica http
 */
public class XRequests {

	public static void main(String[] args) {
		// Execute a GET with timeout settings and return response content as String.
		XRequest.get("https://www.baidu.com/")
			.connectTimeout(Duration.ofSeconds(1000))
			.query("test", "a", "b", "c")
			.query("name", "張三")
			.query("x", "1", "2")
			.log()
			.execute().asString();

		// Execute a POST with the 'expect-continue' handshake, using HTTP/1.1,
		// containing a request body as String and return response content as byte array.
		XRequest.post("https://www.baidu.com/do-stuff")
			.log(HttpLoggingInterceptor.Level.BASIC)
			.bodyString("Important stuff")
			.execute().asBytes();

		// Execute a POST with a custom header through the proxy containing a request body
		// as an HTML form and save the result to the file
		XResponse xResponse = XRequest.post("https://www.baidu.com/some-form")
			.log(HttpLoggingInterceptor.Level.HEADERS)
			.addHeader("X-Custom-header", "stuff")
			.execute();
		System.out.println(xResponse);
	}

}
