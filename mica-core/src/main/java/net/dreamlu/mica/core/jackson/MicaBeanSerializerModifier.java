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

package net.dreamlu.mica.core.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import net.dreamlu.mica.core.utils.StringPool;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * jackson 默认值为 null 时的处理
 *
 * @author L.cm
 */
public class MicaBeanSerializerModifier extends BeanSerializerModifier {

	@Override
	public List<BeanPropertyWriter> changeProperties(
		SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
		// 循环所有的beanPropertyWriter
		for (BeanPropertyWriter writer : beanProperties) {
			// 如果已经有 null 序列化处理如注解：@JsonSerialize(nullsUsing = xxx) 跳过
			if (writer.hasNullSerializer()) {
				continue;
			}
			JavaType type = writer.getType();
			Class<?> clazz = type.getRawClass();
			if (type.isTypeOrSubTypeOf(Number.class)) {
				writer.assignNullSerializer(NullJsonSerializers.NUMBER_JSON_SERIALIZER);
			} else if (type.isTypeOrSubTypeOf(Boolean.class)) {
				writer.assignNullSerializer(NullJsonSerializers.BOOLEAN_JSON_SERIALIZER);
			} else if (type.isTypeOrSubTypeOf(Character.class)) {
				writer.assignNullSerializer(NullJsonSerializers.STRING_JSON_SERIALIZER);
			} else if (type.isTypeOrSubTypeOf(String.class)) {
				writer.assignNullSerializer(NullJsonSerializers.STRING_JSON_SERIALIZER);
			} else if (type.isArrayType() || clazz.isArray() || type.isTypeOrSubTypeOf(Collection.class)) {
				writer.assignNullSerializer(NullJsonSerializers.ARRAY_JSON_SERIALIZER);
			} else if (type.isTypeOrSubTypeOf(OffsetDateTime.class)) {
				writer.assignNullSerializer(NullJsonSerializers.STRING_JSON_SERIALIZER);
			} else if (type.isTypeOrSubTypeOf(Date.class) || type.isTypeOrSubTypeOf(TemporalAccessor.class)) {
				writer.assignNullSerializer(NullJsonSerializers.STRING_JSON_SERIALIZER);
			} else {
				writer.assignNullSerializer(NullJsonSerializers.OBJECT_JSON_SERIALIZER);
			}
		}
		return super.changeProperties(config, beanDesc, beanProperties);
	}

	public interface NullJsonSerializers {

		JsonSerializer<Object> STRING_JSON_SERIALIZER = new JsonSerializer<Object>() {
			@Override
			public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
				gen.writeString(StringPool.EMPTY);
			}
		};

		JsonSerializer<Object> NUMBER_JSON_SERIALIZER = new JsonSerializer<Object>() {
			@Override
			public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
				gen.writeNumber(-1);
			}
		};

		JsonSerializer<Object> BOOLEAN_JSON_SERIALIZER = new JsonSerializer<Object>() {
			@Override
			public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
				gen.writeObject(Boolean.FALSE);
			}
		};

		JsonSerializer<Object> ARRAY_JSON_SERIALIZER = new JsonSerializer<Object>() {
			@Override
			public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
				gen.writeStartArray();
				gen.writeEndArray();
			}
		};

		JsonSerializer<Object> OBJECT_JSON_SERIALIZER = new JsonSerializer<Object>() {
			@Override
			public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
				gen.writeStartObject();
				gen.writeEndObject();
			}
		};

	}
}
