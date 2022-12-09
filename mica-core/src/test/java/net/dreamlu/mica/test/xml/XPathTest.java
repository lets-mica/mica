package net.dreamlu.mica.test.xml;

import net.dreamlu.mica.core.utils.XmlHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Node;

/**
 * Created by L.cm on 2016/5/13.
 */
class XPathTest {

	@Test
	void test001() {
		String xml = """
				<?xml version="1.0" encoding="ISO-8859-1"?>
				<bookstore>
				<book>
				<title lang="xxx">Harry Potter</title>
				<price>29.99</price>
				</book>
				<book>
				<title lang="eng">Learning XML</title>
				<price>39.95</price>
				</book>
				</bookstore>""";

		XmlHelper xmlHelper = XmlHelper.safe(xml);
		String title1 = xmlHelper.getString("//book[1]/title");
		Assertions.assertEquals("Harry Potter", title1);

		String titleLang = xmlHelper.getString("//book[2]/title/@lang");
		Assertions.assertEquals("eng", titleLang);

		Number price1 = xmlHelper.getNumber("//book[1]/price");
		System.out.println(price1.doubleValue());

		Node node = xmlHelper.getNode("//book[2]/title");
		String titleLang2 = xmlHelper.getString(node, "@lang");
		Assertions.assertEquals("eng", titleLang2);

		Assertions.assertEquals(titleLang, titleLang2);

		boolean isEn = xmlHelper.getBoolean("//book[1]/title/@lang=\"eng\"");
		System.out.println(isEn);
	}
}
