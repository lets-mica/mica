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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.jfinal.plugin.activerecord.Record;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * Record 序列化
 *
 * @author L.cm
 */
public class RecordSerializer extends StdSerializer<Record> {
	public static final RecordSerializer INSTANCE = new RecordSerializer();

	protected RecordSerializer() {
		super(Record.class);
	}

	@Override
	public void serialize(Record value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		serializeRecord(value, gen, null);
	}

	@Override
	public void serializeWithType(Record value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
		serializeRecord(value, gen, typeSer);
	}

	private static void serializeRecord(Record value, JsonGenerator gen, TypeSerializer typeSer) throws IOException {
		gen.writeStartObject();
		if (typeSer != null) {
			gen.writeStringField(typeSer.getPropertyName(), value.getClass().getName());
		}
		Map<String, Object> columns = value.getColumns();
		for (Map.Entry<String, Object> entry : columns.entrySet()) {
			// json 不包含 null 的数据
			Object entryValue = entry.getValue();
			if (Objects.nonNull(entryValue)) {
				gen.writeObjectField(entry.getKey(), entry.getValue());
			}
		}
		gen.writeEndObject();
	}
}
