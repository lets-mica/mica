package net.dreamlu.mica.xss;

import net.dreamlu.mica.xss.utils.XssUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.junit.Assert;
import org.junit.Test;

/**
 * jsoup 测试
 *
 * @author L.cm
 */
public class JsoupTest {

	@Test
	public void test1() {
		String html = "&&&&&&<script>哈哈哈</script>";
		Document.OutputSettings settings = new Document.OutputSettings()
			// 1. 转义，没找到关闭的方法
			.escapeMode(Entities.EscapeMode.xhtml)
			// 2. 保留换行
			.prettyPrint(false);
		// 注意会被转义
		String escapedText = Jsoup.clean(html, "", XssUtil.WHITE_LIST, settings);
		// 3. 反转义
		String text = Entities.unescape(escapedText);
		Assert.assertEquals(text, "&&&&&&");
	}

}
