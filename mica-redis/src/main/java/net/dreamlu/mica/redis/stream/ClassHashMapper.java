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

package net.dreamlu.mica.redis.stream;

import net.dreamlu.mica.core.utils.BeanUtil;
import net.dreamlu.mica.core.utils.JsonUtil;
import org.springframework.data.redis.hash.HashMapper;

import java.util.Map;

/**
 * ClassHashMapper
 *
 * @author L.cm
 */
public class ClassHashMapper implements HashMapper<Object, String, String> {
	@Override
	public Map<String, String> toHash(Object object) {
		return null;
	}

	@Override
	public Object fromHash(Map<String, String> hash) {
		String clazz = hash.get("@class");
		return JsonUtil.convertValue(hash, BeanUtil.forName(clazz));
	}

}
