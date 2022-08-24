package net.dreamlu.mica.test.xml;

import net.dreamlu.mica.core.utils.XmlHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXParseException;

class XXETest {

	@Test
	void test1() {
		Assertions.assertThrows(SAXParseException.class, () -> {
			String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
				"   <!DOCTYPE c [\n" +
				"       <!ENTITY file SYSTEM \"file:///etc/passwd\">\n" +
				"   ]>\n" +
				"   <c>&file;</c>";

			XmlHelper helper = XmlHelper.safe(xml);
			System.out.println(helper.getString("c"));
		});
	}

	@Test
	void test2() {
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
