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

import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.spring.SpringContextUtil;
import net.dreamlu.mica.xss.config.MicaXssProperties;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * jackson xss 处理
 *
 * @author L.cm
 */
@Slf4j
public class XssCleanDeserializer extends XssCleanDeserializerBase {

	@Override
	public String clean(String name, String text) throws IOException {
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
		String value = text;
		if (xssCleaner != null) {
			value = xssCleaner.clean(name, XssType.JACKSON);
		}
		MicaXssProperties.JacksonConfig jackson = properties.getJackson();
		String charsToDelete = jackson.getCharsToDelete();
		if (!charsToDelete.isEmpty()) {
			value = StringUtils.deleteAny(value, charsToDelete);
		}
		boolean isTrimText = properties.isTrimText() || jackson.isTrimText();
		if (isTrimText) {
			value = value.trim();
		}
		boolean emptyAsNull = jackson.isEmptyAsNull();
		if (emptyAsNull && value.isEmpty()) {
			value = null;
		}
		log.debug("Json property name:{} value:{} cleaned up by mica-xss, current value is:{}.", name, text, value);
		return value;
	}

}
