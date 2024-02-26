package net.dreamlu;

import net.dreamlu.mica.http.HttpRequest;
import net.dreamlu.mica.http.LogLevel;

public class HttpTest {

	public static void main(String[] args) {
		String html = HttpRequest.get("https://www.dreamlu.net/")
			.useConsoleLog(LogLevel.NONE)
			.useSSL()
			.execute()
			.onResponse(responseSpec -> {
				return responseSpec.asString();
			}, (request, e) -> {
				e.printStackTrace();
				return null;
			});
		System.out.println(html);
	}

}
