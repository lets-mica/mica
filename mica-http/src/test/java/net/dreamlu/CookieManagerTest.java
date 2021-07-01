package net.dreamlu;

import net.dreamlu.mica.http.HttpRequest;
import net.dreamlu.mica.http.InMemoryCookieManager;

/**
 * cookie 管理测试
 *
 * @author L.cm
 */
public class CookieManagerTest {

	public static void main(String[] args) {
		InMemoryCookieManager cookieManager = new InMemoryCookieManager();

		HttpRequest.get("https://demo.dreamlu.net/captcha.jpg")
			.cookieManager(cookieManager)
			.execute()
			.asString();

		HttpRequest.get("https://demo.dreamlu.net")
			.cookieManager(cookieManager)
			.execute()
			.asString();
	}

}
