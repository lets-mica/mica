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

package net.dreamlu.mica.jetcache.jackson;

import com.alicp.jetcache.support.CacheEncodeException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

/**
 * Jackson ValueEncoder
 *
 * @author L.cm
 */
@RequiredArgsConstructor
public class JacksonValueEncoder implements Function<Object, byte[]> {
	private final ObjectMapper mapper;

	@Override
	public byte[] apply(Object o) {
		try {
			return mapper.writeValueAsBytes(o);
		} catch (JsonProcessingException e) {
			throw new CacheEncodeException("decode error", e);
		}
	}

}
