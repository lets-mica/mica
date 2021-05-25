package net.dreamlu.mica.spider;

import net.dreamlu.mica.http.HttpRequest;
import net.dreamlu.mica.http.LogLevel;
import net.dreamlu.mica.spider.mapper.DomMapper;

import java.util.List;

public class OsChinaTest {

	public static void main(String[] args) {
		// 同步，异常返回 null
		Oschina oschina = HttpRequest.get("https://www.oschina.net")
			.useConsoleLog(LogLevel.BASIC)
			.execute()
			.onSuccess(responseSpec -> DomMapper.readValue(responseSpec, Oschina.class));
		if (oschina == null) {
			return;
		}
		System.out.println(oschina.getTitle());

		System.out.println("综合资讯");
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
