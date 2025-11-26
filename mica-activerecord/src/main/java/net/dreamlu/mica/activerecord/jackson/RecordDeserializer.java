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

package net.dreamlu.mica.activerecord.jackson;

import com.jfinal.plugin.activerecord.Record;
import net.dreamlu.mica.core.utils.JsonUtil;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.deser.std.StdDeserializer;
import tools.jackson.databind.jsontype.TypeDeserializer;

import java.io.IOException;
import java.util.Map;

/**
 * Record 反序列化
 *
 * @author L.cm
 */
public class RecordDeserializer extends StdDeserializer<Record> {
	public static final RecordDeserializer INSTANCE = new RecordDeserializer();

	protected RecordDeserializer() {
		super(Record.class);
	}

	@Override
	public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
		return deserializeRecord(jp, typeDeserializer);
	}

	@Override
	public Record deserialize(JsonParser jp, DeserializationContext ctxt) throws JacksonException {
		return deserializeRecord(jp, null);
	}

	private static Record deserializeRecord(JsonParser jp, TypeDeserializer typeDeserializer) throws JacksonException {
		JsonNode node = jp.getCodec().readTree(jp);
		Map<String, Object> columns = JsonUtil.convertValue(node, JsonUtil.getMapType(Object.class));
		if (typeDeserializer != null) {
			columns.remove(typeDeserializer.getPropertyName());
		}
		Record record = new Record();
		record.setColumns(columns);
		return record;
	}

}
