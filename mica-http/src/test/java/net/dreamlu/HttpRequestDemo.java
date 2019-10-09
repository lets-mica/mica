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

import com.fasterxml.jackson.databind.JsonNode;
import net.dreamlu.mica.core.utils.Base64Util;
import net.dreamlu.mica.http.HttpRequest;
import net.dreamlu.mica.http.LogLevel;
import net.dreamlu.mica.http.ResponseSpec;
import okhttp3.Cookie;

import java.net.URI;
import java.time.Duration;
import java.util.Optional;

/**
 * This example of mica http
 *
 * @author L.cm
 */
public class HttpRequestDemo {

	public void doc() {
		// 设定全局日志级别 NONE，BASIC，HEADERS，BODY， 默认：NONE
		HttpRequest.setGlobalLog(LogLevel.BODY);

		// 同步请求 url，方法支持 get、post、patch、put、delete
		HttpRequest.get("https://www.baidu.com")
			.log(LogLevel.BASIC)             //设定本次的日志级别，优先于全局
			.addHeader("x-account-id", "mica001") // 添加 header
			.addCookie(new Cookie.Builder()  // 添加 cookie
				.name("sid")
				.value("mica_user_001")
				.build()
			)
			.query("q", "mica") //设置 url 参数，默认进行 url encode
			.queryEncoded("name", "encodedValue")
			.formBuilder()    // 表单构造器，同类 multipartFormBuilder 文件上传表单
			.add("id", 123123) // 表单参数
			.execute()// 发起请求
			.asJsonNode();
		// 结果集转换，注：如果网络异常等会直接抛出异常。
		// 同类的方法有 asString、asBytes
		// json 类响应：asJsonNode、asObject、asList、asMap，采用 jackson 处理
		// xml、html响应：asDocument，采用的 jsoup 处理
		// file 文件：toFile

		// 同步
		String html = HttpRequest.post("https://www.baidu.com")
			.execute()
			.onSuccess(ResponseSpec::asString);// 处理响应，有网络异常等直接返回 null

		// 同步
		String text = HttpRequest.patch("https://www.baidu.com")
			.execute()
			.onSuccess(ResponseSpec::asString);
		// onSuccess http code in [200..300) 处理响应，有网络异常等直接返回 null

		// 发送异步请求
		HttpRequest.delete("https://www.baidu.com")
			.async() // 开启异步
			.onFailed((request, e) -> {    // 异常时的处理
				e.printStackTrace();
			})
			.onSuccessful(responseSpec -> { // 消费响应成功 http code in [200..300)
				// 注意：响应结果流只能读一次
				JsonNode jsonNode = responseSpec.asJsonNode();
			});
	}

	public static void main(String[] args) {
		// 设定全局日志级别
		HttpRequest.setGlobalLog(LogLevel.BODY);

		// 同步，异常时 返回 null
		String html = HttpRequest.get("https://www.baidu.com")
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

		// 同步调用，返回 Optional，异常时返回 Optional.empty()
		Optional<String> opt = HttpRequest.post(URI.create("https://www.baidu.com"))
			.bodyString("Important stuff")
			.formBuilder()
			.add("a", "b")
			.execute()
			.onSuccessOpt(ResponseSpec::asString);

		// 同步，成功时消费（处理） response
		HttpRequest.post("https://www.baidu.com/some-form")
			.addHeader("X-Custom-header", "stuff")
			.execute()
			.onFailed((request, e) -> {
				e.printStackTrace();
			})
			.onSuccessful(ResponseSpec::asString);

		// async，异步执行结果，失败时打印堆栈
		HttpRequest.get("https://www.baidu.com/some-form")
			.async()
			.onFailed((request, e) -> {
				e.printStackTrace();
			})
			.onSuccessful(System.out::println);
	}

}
