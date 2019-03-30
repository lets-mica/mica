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

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.PackageVersion;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import net.dreamlu.mica.core.utils.DateUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * java 8 时间默认序列化
 *
 * @author L.cm
 */
public class MicaJavaTimeModule extends SimpleModule {

	public MicaJavaTimeModule() {
		super(PackageVersion.VERSION);
		this.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateUtil.DATETIME_FORMATTER));
		this.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateUtil.DATE_FORMATTER));
		this.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateUtil.TIME_FORMATTER));
		this.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateUtil.DATETIME_FORMATTER));
		this.addSerializer(LocalDate.class, new LocalDateSerializer(DateUtil.DATE_FORMATTER));
		this.addSerializer(LocalTime.class, new LocalTimeSerializer(DateUtil.TIME_FORMATTER));
	}
}
