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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * MicaTemplate 测试
 *
 * @author L.cm
 */
public class MicaTemplateTest {

	private MicaTemplate micaTemplate;

	@Before
	public void setup() throws Exception {
		micaTemplate = new MicaTemplate(new MicaLayTplProperties());
		micaTemplate.afterPropertiesSet();
	}

	@Test
	public void test1() {
		Map<String, Object> data = new HashMap<>();
		data.put("title", "mica");

		String html = micaTemplate.render("<h3>{{ d.title }}</h3>", data);
		Assert.assertEquals(html, "<h3>mica</h3>");
	}

	@Test
	public void test2() {
		String html =
			"{{#\n" +
			"console.log();\n" +
			"console.log(\"im {}\", \"L.cm\");\n" +
			"\n" +
			"console.error(\"hi im {}\", \"L.cm\");\n" +
			"\n" +
			"console.log(\"laytpl version:{}\", laytpl.v)\n" +
			"}}";

		micaTemplate.render(html, new HashMap<>());
	}
}
