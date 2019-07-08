package net.dreamlu.mica.test.utils;

import net.dreamlu.mica.core.utils.StringUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class StringUtilTest {

	@Test
	public void testFormat1() {
		String str = "my name is L.cm, and i like Java!";

		Map<String, Object> param = new HashMap<>();
		param.put("name", "L.cm");
		param.put("like", "Java");
		String msg = StringUtil.format("my name is ${ name }, and i like ${ like }!", param);

		Assert.assertEquals(str, msg);
	}

	@Test
	public void testFormat2() {
		String str = "my name is L.cm 4 years old, and i like Java!";

		String msg = StringUtil.format("my name is {} {} years old, and i like {}!", "L.cm", 4, "Java");
		Assert.assertEquals(str, msg);
	}

	@Test
	public void testFormat3() {
		String str = "my name is L.cm 4 years old, and i like Java,{},{}!";

		String msg = StringUtil.format("my name is {} {} years old, and i like {},{},{}!", "L.cm", 4, "Java");
		Assert.assertEquals(str, msg);
	}

	@Test
	public void cleanTextTest() {
		String s = StringUtil.cleanText(null);
		Assert.assertNull(s);

		String s1 = StringUtil.cleanText(" 123123;123\t1\n2|3,1231`'' ");
		Assert.assertEquals(s1, "1231231231231231");
	}
}
