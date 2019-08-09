package net.dreamlu.mica.test.xml;

import net.dreamlu.mica.core.utils.XmlHelper;
import org.junit.Test;
import org.xml.sax.SAXParseException;

public class XXETest {

	@Test(expected = SAXParseException.class)
	public void test1() {
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
			"   <!DOCTYPE c [\n" +
			"       <!ENTITY file SYSTEM \"file:///etc/passwd\">\n" +
			"   ]>\n" +
			"   <c>&file;</c>";

		XmlHelper helper = XmlHelper.safe(xml);
		System.out.println(helper.getString("c"));
	}

	@Test
	public void test2() {
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
			"   <!DOCTYPE c [\n" +
			"       <!ENTITY file SYSTEM \"file:///etc/passwd\">\n" +
			"   ]>\n" +
			"   <c>&file;</c>";

		// 注意：windows 下找不到文件会报错
		try {
			XmlHelper helper = XmlHelper.unsafe(xml);
			System.out.println(helper.getString("c"));
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}
}
