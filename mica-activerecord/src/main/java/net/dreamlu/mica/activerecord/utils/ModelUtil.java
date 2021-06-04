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

package net.dreamlu.mica.activerecord.utils;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import net.dreamlu.mica.core.utils.BeanUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * model 工具
 *
 * @author L.cm
 */
public class ModelUtil {

	/**
	 * 将 model 转为 bean
	 */
	public static <T> T toBean(Model<?> model, Class<T> valueType) {
		// model 有 get set 方法，可以直接转换
		return BeanUtil.copy(model, valueType);
	}

	/**
	 * 将 record 转为 bean
	 */
	public static <T> T toBean(Record record, Class<T> valueType) {
		// 默认下划线转驼峰
		return toBean(record, FieldStrategy.LOWER_TO_CAMEL, valueType);
	}

	/**
	 * 将 record 转为 bean
	 */
	public static <T> T toBean(Record record, FieldStrategy strategy, Class<T> valueType) {
		Map<String, Object> data = new HashMap<>(16);
		record.getColumns().forEach((key, value) -> data.put(strategy.convert(key), value));
		return BeanUtil.toBean(data, valueType);
	}

}
