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

package net.dreamlu.mica.activerecord.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.jfinal.plugin.activerecord.Record;
import net.dreamlu.mica.core.utils.JsonUtil;
import tools.jackson.databind.DefaultTyping;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.jsontype.PolymorphicTypeValidator;

public class JacksonTest {

	public static void main(String[] args) {
		JsonMapper jsonMapper = JsonUtil.getInstance();
		PolymorphicTypeValidator polymorphicTypeValidator = jsonMapper.serializationConfig().getPolymorphicTypeValidator();
		JsonMapper.Builder jsonMapperBuilder = jsonMapper.rebuild();
		jsonMapperBuilder.activateDefaultTyping(polymorphicTypeValidator, DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
		JsonMapper mapper = jsonMapperBuilder.build();

		Record record = new Record();
		record.set("a", "123");
		String valueAsString = mapper.writeValueAsString(record);
		System.out.println(valueAsString);
		Record record1 = mapper.readValue(valueAsString, Record.class);
		System.out.println(record1);

		UserModel userModel = new UserModel();
		userModel.setId(123L);
		userModel.setName("张三");
		String valueAsString1 = mapper.writeValueAsString(userModel);
		System.out.println(valueAsString1);
		UserModel userModel1 = mapper.readValue(valueAsString1, UserModel.class);
		System.out.println(userModel1);
	}

}
