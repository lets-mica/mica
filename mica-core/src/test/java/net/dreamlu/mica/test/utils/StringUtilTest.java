package net.dreamlu.mica.test.utils;

import net.dreamlu.mica.core.utils.StringUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class StringUtilTest {

	@Test
	void testFormat1() {
		String str = "my name is L.cm, and i like Java!";

		Map<String, Object> param = new HashMap<>();
		param.put("name", "L.cm");
		param.put("like", "Java");
		String msg = StringUtil.format("my name is ${ name }, and i like ${ like }!", param);

		Assertions.assertEquals(str, msg);
	}

	@Test
	void testFormat2() {
		String str = "my name is L.cm 4 years old, and i like Java!";

		String msg = StringUtil.format("my name is {} {} years old, and i like {}!", "L.cm", 4, "Java");
		Assertions.assertEquals(str, msg);
	}

	@Test
	void testFormat3() {
		String str = "my name is L.cm 4 years old, and i like Java,{},{}!";

		String msg = StringUtil.format("my name is {} {} years old, and i like {},{},{}!", "L.cm", 4, "Java");
		Assertions.assertEquals(str, msg);
	}

	@Test
	void testFormat4() {
		String str = "my name is L.cm, and i like Java!";

		Map<String, Object> param1 = new HashMap<>();
		param1.put("name", "L.cm");
		param1.put("like", "Java");
		String msg1 = StringUtil.format("my name is ${ name }, and i like ${ like }!", param1);

		Map<String, String> param2 = new HashMap<>();
		param2.put("name", "L.cm");
		param2.put("like", "Java");
		String msg2 = StringUtil.format("my name is ${ name }, and i like ${ like }!", param2);

		Assertions.assertEquals(str, msg1);
		Assertions.assertEquals(str, msg2);
	}

	@Test
	void cleanTextTest() {
		String s = StringUtil.cleanText(null);
		Assertions.assertNull(s);

		String s1 = StringUtil.cleanText(" 123123;123\t1\n2|3,1231`'' ");
		Assertions.assertEquals("1231231231231231", s1);
	}

	@Test
	void testNanoId() {
		String nanoId62 = StringUtil.getNanoId62();
		Assertions.assertNotNull(nanoId62);
	}

}
