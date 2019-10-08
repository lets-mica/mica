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

		System.out.println("热门新闻");

		List<VNews> vNews = oschina.getVNews();
		for (VNews vNew : vNews) {
			System.out.println("title:\t" + vNew.getTitle());
			System.out.println("href:\t" + vNew.getHref());
			System.out.println("时间:\t" + vNew.getDate());
		}

		System.out.println("热门博客");
		List<VBlog> vBlogList = oschina.getVBlogList();
		for (VBlog vBlog : vBlogList) {
			System.out.println("title:\t" + vBlog.getTitle());
			System.out.println("href:\t" + vBlog.getHref());
			System.out.println("阅读数:\t" + vBlog.getRead());
			System.out.println("评价数:\t" + vBlog.getPing());
			System.out.println("点赞数:\t" + vBlog.getZhan());
		}
	}
}
