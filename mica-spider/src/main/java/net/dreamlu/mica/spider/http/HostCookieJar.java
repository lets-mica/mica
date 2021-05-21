/*
 * Copyright (c) 2019-2029, Dreamlu (596392912@qq.com & www.dreamlu.net).
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

package net.dreamlu.mica.spider.http;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

import java.util.*;

/**
 * 多域名的 cookie 管理
 *
 * @author L.cm
 */
public enum HostCookieJar implements CookieJar {
	/**
	 * 实例
	 */
	INSTANCE;

	private static final Map<String, Map<String, Cookie>> COOKIE_MAP = new HashMap<>(8);

	@Override
	public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
		String host = url.host();
		Map<String, Cookie> cookieMap = COOKIE_MAP.get(host);
		if (cookieMap == null) {
			cookieMap = new HashMap<>(8);
		}
		// 便于新 cookie 替换老的 cookie
		for (Cookie cookie : cookies) {
			cookieMap.put(cookie.name(), cookie);
		}
		COOKIE_MAP.put(host, cookieMap);
	}

	@Override
	public List<Cookie> loadForRequest(HttpUrl url) {
		Map<String, Cookie> cookieMap = COOKIE_MAP.get(url.host());
		if (cookieMap == null) {
			return Collections.emptyList();
		}
		return new ArrayList<>(cookieMap.values());
	}

}
