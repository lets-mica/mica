package net.dreamlu.mica.test.utils;

import net.dreamlu.mica.core.utils.HexUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Digest 测试
 *
 * @author L.cm
 */
class HexUtilTest {

	@Test
	void test() {
		String text = "mica 最牛逼";
		String hexText = HexUtil.encodeToString(text);
		String decode = HexUtil.decodeToString(hexText);
		Assertions.assertEquals(text, decode);
	}

}
