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

package net.dreamlu.mica.xss.core;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.spring.SpringContextUtil;
import net.dreamlu.mica.xss.config.MicaXssProperties;
import net.dreamlu.mica.xss.utils.XssUtil;

import java.io.IOException;

/**
 * jackson xss 处理
 *
 * @author L.cm
 */
@Slf4j
public class XssCleanDeserializer extends JsonDeserializer<String> {

	@Override
	public String deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
		// XSS filter
		String text = p.getValueAsString();
		if (text == null) {
			return null;
		}
		// 读取 xss 配置
		MicaXssProperties properties = SpringContextUtil.getBean(MicaXssProperties.class);
		if (properties == null) {
			return text;
		}
		// 读取 XssCleaner bean
		XssCleaner xssCleaner = SpringContextUtil.getBean(XssCleaner.class);
		if (xssCleaner == null) {
			return XssUtil.trim(text, properties.isTrimText());
		}
		String value = xssCleaner.clean(XssUtil.trim(text, properties.isTrimText()));
		log.debug("Json property value:{} cleaned up by mica-xss, current value is:{}.", text, value);
		return value;
	}

}
