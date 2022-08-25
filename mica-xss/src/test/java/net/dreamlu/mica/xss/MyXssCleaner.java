package net.dreamlu.mica.xss;

import net.dreamlu.mica.xss.core.XssCleaner;
import net.dreamlu.mica.xss.core.XssType;
import net.dreamlu.mica.xss.utils.XssUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;

public class MyXssCleaner implements XssCleaner {

	@Override
	public String clean(String html, XssType type) {
		Document.OutputSettings settings = new Document.OutputSettings()
			// 1. 转义用最少的规则，没找到关闭的方法
			.escapeMode(Entities.EscapeMode.xhtml)
			// 2. 保留换行
			.prettyPrint(false);
		// 注意会被转义
		String escapedText = Jsoup.clean(html, "", XssUtil.WHITE_LIST, settings);
		// 3. 反转义
		return Entities.unescape(escapedText);
	}

}
