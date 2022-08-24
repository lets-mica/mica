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

package net.dreamlu.mica.laytpl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * MicaTemplate 测试
 *
 * @author L.cm
 */
class MicaTemplateTest {

	private MicaTemplate micaTemplate;

	@BeforeEach
	public void setup() throws Exception {
		MicaLayTplProperties properties = new MicaLayTplProperties();
		micaTemplate = new MicaTemplate(properties, new FmtFunc(properties));
		micaTemplate.afterPropertiesSet();
	}

	@Test
	void test1() {
		Map<String, Object> data = new HashMap<>();
		data.put("title", "mica");

		String html = micaTemplate.render("<h3>{{ d.title }}</h3>", data);
		Assertions.assertEquals(html, "<h3>mica</h3>");
	}

	@Test
	void test2() {
		String html =
			"{{#\n" +
			"console.log();\n" +
			"console.log(\"im {}\", \"L.cm\");\n" +
			"console.error(\"hi im {}\", \"L.cm\");\n" +
			"console.log(fmt.format( d.date ));\n" +
			"console.log(\"laytpl version:{}\", laytpl.v);\n" +
			"}}";

		Map<String, Object> data = new HashMap<>();
		data.put("date", new Date());
		micaTemplate.render(html, data);
	}

	@Test
	void test3() {
		String html = "{{!#1+1!}}";
		String render = micaTemplate.render(html);
		Assertions.assertEquals(render, "#1+1");
	}
}
