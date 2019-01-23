package net.dreamlu.mica.test.xml;

import net.dreamlu.mica.core.utils.XmlHelper;
import org.junit.Test;

public class XXETest {

	@Test(expected = RuntimeException.class)
	public void test() {
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
			"   <!DOCTYPE c [\n" +
			"       <!ENTITY file SYSTEM \"file:///etc/passwd\">\n" +
			"   ]>\n" +
			"   <c>&file;</c>";

		XmlHelper helper = XmlHelper.of(xml);
		System.out.println(helper.getString("c"));
	}
}
