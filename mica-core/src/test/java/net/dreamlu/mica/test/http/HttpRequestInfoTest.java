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

package net.dreamlu.mica.test.http;


import net.dreamlu.mica.core.http.HttpRequestInfo;
import net.dreamlu.mica.core.http.HttpRequestParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * HttpRequestInfo 测试
 *
 * @author L.cm
 */
public class HttpRequestInfoTest {

	@Test
	public void test() {
		String text =
			"POST http://{{host}}/api/v1/mqtt/publish?a=123\n" +
			"Content-Type: application/json\n" +
			"Authorization: Basic {{username}} {{password}}\n" +
			"\n" +
			"{\n" +
			"    \"topic\":\"a/b/c\",\n" +
			"    \"payload\":\"Hello World\",\n" +
			"    \"qos\":1,\n" +
			"    \"retain\":false,\n" +
			"    \"clientId\":\"example\"\n" +
			"}";
		HttpRequestInfo request = HttpRequestParser.parser(text);
		Assert.assertEquals("POST", request.getMethod());
	}

}
