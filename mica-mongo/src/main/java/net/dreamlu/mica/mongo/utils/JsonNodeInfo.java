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

package net.dreamlu.mica.mongo.utils;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import org.springframework.util.Assert;

import java.util.LinkedList;
import java.util.StringJoiner;

/**
 * json tree 节点信息
 *
 * @author L.cm
 */
@Getter
public class JsonNodeInfo {
	/**
	 * mongo keys: class1.class2.item
	 */
	private final String nodeKeys;
	/**
	 * jsonPath语法：/class1/class2/item
	 */
	private final String nodePath;
	/**
	 * 节点关系
	 */
	private final LinkedList<String> elements;
	/**
	 * tree 的 叶子节点，此处为引用
	 */
	private final JsonNode leafNode;

	public JsonNodeInfo(LinkedList<String> elements, JsonNode leafNode) {
		Assert.notNull(elements, "elements can not be null.");
		this.nodeKeys = getNodeKeys(elements);
		this.nodePath = getNodePath(elements);
		this.elements = elements;
		this.leafNode = leafNode;
	}

	/**
	 * 获取 mongo db的 key 语法
	 *
	 * @return mongo db的 key 语法
	 */
	private static String getNodeKeys(LinkedList<String> elements) {
		StringJoiner nodeKeysJoiner = new StringJoiner(".");
		elements.forEach(nodeKeysJoiner::add);
		return nodeKeysJoiner.toString();
	}

	/**
	 * 获取 json path 语法路径
	 *
	 * @return jsonPath 路径
	 */
	private static String getNodePath(LinkedList<String> elements) {
		StringJoiner nodePathJoiner = new StringJoiner("/", "/", "");
		elements.forEach(nodePathJoiner::add);
		return nodePathJoiner.toString();
	}

	/**
	 * 获取第一个元素
	 *
	 * @return element
	 */
	public String getFirst() {
		return elements.getFirst();
	}

}
