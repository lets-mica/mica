package net.dreamlu.mica.test.utils;

import net.dreamlu.mica.core.utils.HexUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * Digest 测试
 *
 * @author L.cm
 */
public class HexUtilTest {

	@Test
	public void test() {
		String text = "mica 最牛逼";
		String hexText = HexUtil.encodeToString(text);
		String decode = HexUtil.decodeToString(hexText);
		Assert.assertEquals(text, decode);
	}

}
