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

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.exc.MismatchedInputException;

/**
 * jackson xss 处理
 *
 * @author L.cm
 */
public abstract class XssCleanDeserializerBase extends ValueDeserializer<String> {

	@Override
	public String deserialize(JsonParser p, DeserializationContext ctx) throws JacksonException {
		// json 字段名
		String name = p.currentName();
		// 字符串类型
		if (p.hasToken(JsonToken.VALUE_STRING)) {
			String text = p.getString();
			if (text == null) {
				return null;
			}
			return clean(name, text);
		}
		JsonToken jsonToken = p.currentToken();
		if (jsonToken.isScalarValue()) {
			String text = p.getValueAsString();
			if (text != null) {
				return text;
			}
		}
		throw MismatchedInputException.from(p, String.class, "mica-xss: can't deserialize json name:" + name + " value of type java.lang.String from " + jsonToken);
	}

	/**
	 * 清理 xss
	 *
	 * @param name  json name
	 * @param value json value
	 * @return String
	 */
	public abstract String clean(String name, String value) throws JacksonException;

}
