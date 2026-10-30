package net.dreamlu.mica.xss;

import net.dreamlu.mica.xss.config.MicaXssProperties;
import net.dreamlu.mica.xss.core.DefaultXssCleaner;
import net.dreamlu.mica.xss.core.XssType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * 默认的 xss 清理器测试
 *
 * @author L.cm
 */
class DefaultXssCleanerTest {

	private final DefaultXssCleaner cleaner = new DefaultXssCleaner(new MicaXssProperties());

	private String clean(String html) {
		return cleaner.clean("content", html, XssType.FORM);
	}

	/**
	 * 实体编码的 script 不能被还原成真实标签
	 */
	@Test
	void clean_entityEncodedScript_isNotRestored() {
		String result = clean("&lt;script&gt;alert(1)&lt;/script&gt;");
		Assertions.assertFalse(result.contains("<script"));
		Assertions.assertTrue(result.contains("&lt;script&gt;"));
	}

	/**
	 * 数字实体编码的 script 不能被还原成真实标签
	 */
	@Test
	void clean_numericEntityScript_isNotRestored() {
		String result = clean("&#60;script&#62;alert(1)&#60;/script&#62;");
		Assertions.assertFalse(result.contains("<script"));
	}

	/**
	 * 二次编码的 script 不能被还原成真实标签
	 */
	@Test
	void clean_doubleEncodedScript_isNotRestored() {
		String result = clean("&amp;&lt;script&amp;&gt;alert(1)&amp;&lt;/script&amp;&gt;");
		Assertions.assertFalse(result.contains("<script"));
	}

	/**
	 * 属性值中的双引号实体不能被还原，否则会逃逸出属性注入事件处理器
	 */
	@Test
	void clean_eventHandlerInAttribute_isNotRestored() {
		String result = clean("<a title=\"x&quot; onmouseover=&quot;alert(1)\">y</a>");
		// 双引号保持转义，不会逃逸出属性变成事件处理器
		Assertions.assertFalse(result.contains("\" onmouseover="));
		Assertions.assertTrue(result.contains("&quot;"));
	}

	/**
	 * 带空格的 javascript 协议必须被过滤
	 */
	@Test
	void clean_javascriptProtocolWithSpace_isRemoved() {
		String result = clean("<a href=\" javascript:alert(1)\">x</a>");
		Assertions.assertFalse(result.contains("javascript:"));
	}

	/**
	 * 带制表符的 javascript 协议必须被过滤
	 */
	@Test
	void clean_javascriptProtocolWithTab_isRemoved() {
		String result = clean("<a href=\"&#9;javascript:alert(1)\">x</a>");
		Assertions.assertFalse(result.contains("javascript:"));
	}

	/**
	 * 协议中间带控制字符的 javascript 必须被过滤
	 */
	@Test
	void clean_javascriptProtocolWithControlChar_isRemoved() {
		String result = clean("<a href=\"java&#13;script:alert(1)\">x</a>");
		Assertions.assertFalse(result.contains("alert(1)"));
	}

	/**
	 * vbscript 协议必须被过滤
	 */
	@Test
	void clean_vbscriptProtocol_isRemoved() {
		String result = clean("<a href=\"vbscript:msgbox(1)\">x</a>");
		Assertions.assertFalse(result.contains("vbscript"));
	}

	/**
	 * a 标签中的 data 协议必须被过滤
	 */
	@Test
	void clean_dataProtocolInLink_isRemoved() {
		String result = clean("<a href=\"data:text/html;base64,PHNjcmlwdD5hbGVydCgxKTwvc2NyaXB0Pg==\">x</a>");
		Assertions.assertFalse(result.contains("data:text/html"));
	}

	/**
	 * embed 标签中的 data 协议必须被过滤
	 */
	@Test
	void clean_dataProtocolInEmbed_isRemoved() {
		String result = clean("<embed src=\"data:text/html;base64,PHNjcmlwdD5hbGVydCgxKTwvc2NyaXB0Pg==\">");
		Assertions.assertFalse(result.contains("data:text/html"));
	}

	/**
	 * img 标签中的 html data 协议必须被过滤
	 */
	@Test
	void clean_dataProtocolInImg_isRemoved() {
		String result = clean("<img src=\"data:text/html;base64,PHNjcmlwdD5hbGVydCgxKTwvc2NyaXB0Pg==\">");
		Assertions.assertFalse(result.contains("data:text/html"));
	}

	/**
	 * img 标签中的 base64 图片必须保留
	 */
	@Test
	void clean_base64ImageInImg_isKept() {
		String result = clean("<img src=\"data:image/png;base64,iVBORw0KGgo=\">");
		Assertions.assertTrue(result.contains("data:image/png;base64"));
	}

	/**
	 * 相对地址必须保留
	 */
	@Test
	void clean_relativeUrl_isKept() {
		String result = clean("<a href=\"/local/path\">x</a>");
		Assertions.assertTrue(result.contains("/local/path"));
	}

	/**
	 * http 地址必须保留
	 */
	@Test
	void clean_httpUrl_isKept() {
		String result = clean("<a href=\"https://www.dreamlu.net\">x</a>");
		Assertions.assertTrue(result.contains("https://www.dreamlu.net"));
	}

	/**
	 * 普通富文本中的 & 仍然是原样输出，与之前的版本保持一致
	 */
	@Test
	void clean_ampersandText_keepsRawAmpersand() {
		Assertions.assertEquals("<p>a & b</p>", clean("<p>a & b</p>"));
	}

	/**
	 * style 里的脚本协议必须被过滤
	 */
	@Test
	void clean_styleWithJavascriptProtocol_isRemoved() {
		String result = clean("<div style=\"background:url(javascript:alert(1))\">x</div>");
		Assertions.assertFalse(result.contains("javascript:"));
		Assertions.assertFalse(result.contains("style"));
	}

	/**
	 * style 里的旧式表达式必须被过滤
	 */
	@Test
	void clean_styleWithExpression_isRemoved() {
		Assertions.assertFalse(clean("<div style=\"width:expression(alert(1))\">x</div>").contains("style"));
	}

	/**
	 * style 里用注释拆开的关键字也要被过滤
	 */
	@Test
	void clean_styleWithCommentSplit_isRemoved() {
		Assertions.assertFalse(clean("<div style=\"width:expr/**/ession(alert(1))\">x</div>").contains("style"));
	}

	/**
	 * 正常的行内样式必须保留
	 */
	@Test
	void clean_normalStyle_isKept() {
		Assertions.assertTrue(clean("<div style=\"color:red;text-align:center\">x</div>").contains("color:red"));
	}

	/**
	 * 背景图等相对地址不受影响
	 */
	@Test
	void clean_styleWithRelativeBackground_isKept() {
		Assertions.assertTrue(clean("<div style=\"background:url(/img/a.png)\">x</div>").contains("url(/img/a.png)"));
	}

	/**
	 * 不间断空格等实体应该被还原，不能留在输出里
	 */
	@Test
	void clean_nbspEntity_isDecoded() {
		Assertions.assertEquals("<p>a" + (char) 160 + "b</p>", clean("<p>a&nbsp;b</p>"));
	}

	/**
	 * 纯文本原样输出
	 */
	@Test
	void clean_plainText_isUnchanged() {
		Assertions.assertEquals("hello world", clean("hello world"));
	}

	/**
	 * 原始的 script 标签必须被清理
	 */
	@Test
	void clean_rawScript_isRemoved() {
		Assertions.assertFalse(clean("<script>alert(1)</script>").contains("alert"));
	}

	/**
	 * 转义模式下实体保持转义状态
	 */
	@Test
	void clean_escapeMode_keepsEntities() {
		MicaXssProperties properties = new MicaXssProperties();
		properties.setEnableEscape(true);
		String result = new DefaultXssCleaner(properties).clean("content",
			"&lt;script&gt;alert(1)&lt;/script&gt;", XssType.FORM);
		Assertions.assertFalse(result.contains("<script"));
	}
}
