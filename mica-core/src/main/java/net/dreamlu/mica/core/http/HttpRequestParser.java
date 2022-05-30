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

package net.dreamlu.mica.core.http;

import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.StringTokenizer;

/**
 * http 请求解析
 *
 * @author L.cm
 */
@UtilityClass
public class HttpRequestParser {

	/**
	 * 解析 http 纯文本
	 *
	 * @param httpText httpText
	 * @return HttpRequest
	 */
	public static HttpRequestInfo parser(String httpText) {
		try (StringReader stringReader = new StringReader(httpText);
			 BufferedReader reader = new BufferedReader(stringReader)) {
			return httpParser(reader);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}

	private static HttpRequestInfo httpParser(BufferedReader reader) throws IOException {
		// RequestLine
		String line = reader.readLine();
		if (line == null) {
			return null;
		}
		StringTokenizer tokenizer = new StringTokenizer(line, " ");
		int countTokens = tokenizer.countTokens();
		if (countTokens < 2) {
			return null;
		}
		HttpRequestInfo httpRequestInfo = new HttpRequestInfo();
		// method and url
		String method = tokenizer.nextToken();
		String url = tokenizer.nextToken();
		httpRequestInfo.setMethod(method.trim());
		httpRequestInfo.setUrl(url.trim());
		// 解析 header
		for (; ; ) {
			line = reader.readLine();
			if (line != null && line.isEmpty()) {
				break;
			}
			// 已经结束，没有 body
			if (line == null) {
				return httpRequestInfo;
			}
			// 解析 header 行，name: value
			tokenizer = new StringTokenizer(line, ":");
			if (tokenizer.countTokens() > 1) {
				String name = tokenizer.nextToken();
				String value = tokenizer.nextToken();
				httpRequestInfo.addHeader(name.trim(), value.trim());
			}
		}
		// 解析 body
		StringBuilder bodyBuilder = new StringBuilder();
		for (; ; ) {
			line = reader.readLine();
			if (line == null) {
				break;
			}
			if (!line.isEmpty()) {
				bodyBuilder.append(line);
			}
		}
		// 处理 body
		String body = bodyBuilder.toString();
		if (!body.isEmpty()) {
			httpRequestInfo.setBody(body.trim());
		}
		return httpRequestInfo;
	}

}
