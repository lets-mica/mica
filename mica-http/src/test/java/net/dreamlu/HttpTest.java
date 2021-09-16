package net.dreamlu;

import net.dreamlu.mica.http.HttpRequest;
import net.dreamlu.mica.http.LogLevel;
import net.dreamlu.mica.http.ResponseSpec;

public class HttpTest {

	public static void main(String[] args) {
		String html = HttpRequest.get("https://wwww.baiduxxx.com/123123")
			.useSlf4jLog(LogLevel.NONE)
			.execute()
			.onFailed((request, e) -> {
				e.printStackTrace();
				ResponseSpec response = e.getResponse();
				if (response != null) {
					System.out.println(response.asString());
				}
			})
			.onSuccessful(ResponseSpec::asString);
		System.out.println(html);
	}

}
