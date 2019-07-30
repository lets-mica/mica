package net.dreamlu;

import net.dreamlu.mica.http.HttpRequest;

import java.util.List;

public class OschinaTest {

	public static void main(String[] args) {
		// 同步，异常返回 null
		Oschina oschina = HttpRequest.get("https://www.oschina.net")
			.execute()
			.onSuccess(responseSpec -> responseSpec.asDomValue(Oschina.class));
		if (oschina == null) {
			return;
		}
		System.out.println(oschina.getTitle());

		List<VNews> vNews = oschina.getVNews();
		for (VNews vNew : vNews) {
			System.out.println(vNew.getTitle() + "\t" + vNew.getHref());
		}
	}
}
