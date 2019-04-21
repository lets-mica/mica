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

package net.dreamlu.mica.mongo.converter;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.lang.Nullable;

/**
 * JsonNode 转 mongo Document
 *
 * @author L.cm
 */
@WritingConverter
public enum JsonNodeToDocumentConverter implements Converter<ObjectNode, Document> {
	/**
	 * 实例
	 */
	INSTANCE;

	@Override
	public Document convert(@Nullable ObjectNode source) {
		return source == null ? null : Document.parse(source.toString());
	}
}
