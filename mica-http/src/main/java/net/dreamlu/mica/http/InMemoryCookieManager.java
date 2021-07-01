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

package net.dreamlu.mica.http;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.internal.annotations.EverythingIsNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * cookie 管理
 *
 * @author L.cm
 */
@EverythingIsNonNull
public class InMemoryCookieManager implements CookieJar {
	private final Set<Cookie> cookieSet;

	public InMemoryCookieManager() {
		this.cookieSet = new CopyOnWriteArraySet<>();
	}

	@Override
	public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
		cookieSet.addAll(cookies);
	}

	@Override
	public List<Cookie> loadForRequest(HttpUrl url) {
		List<Cookie> needRemoveCookieList = new ArrayList<>();
		List<Cookie> matchedCookieList = new ArrayList<>();
		for (Cookie cookie : cookieSet) {
			if (isCookieExpired(cookie)) {
				needRemoveCookieList.add(cookie);
			} else if (cookie.matches(url)) {
				matchedCookieList.add(cookie);
			}
		}
		// 清除过期 cookie
		if (!needRemoveCookieList.isEmpty()) {
			cookieSet.removeAll(needRemoveCookieList);
		}
		return matchedCookieList;
	}

	private static boolean isCookieExpired(Cookie cookie) {
		return cookie.expiresAt() < System.currentTimeMillis();
	}

}
