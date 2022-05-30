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

package net.dreamlu.mica.core.http;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * http 请求信息
 *
 * @author L.cm
 */
@Getter
@Setter
@ToString
public class HttpRequestInfo implements Serializable {
	/**
	 * 请求方法
	 */
	private String method;
	/**
	 * 请求 url
	 */
	private String url;
	/**
	 * 请求 headers
	 */
	private Map<String, String> headers = new HashMap<>();
	/**
	 * 请求 body
	 */
	private String body;

	public void addHeader(String name, String value) {
		headers.put(name, value);
	}

}
