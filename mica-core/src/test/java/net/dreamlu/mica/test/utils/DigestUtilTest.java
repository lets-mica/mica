package net.dreamlu.mica.test.utils;

import net.dreamlu.mica.core.utils.DigestUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * Digest 测试
 *
 * @author L.cm
 */
public class DigestUtilTest {

	@Test
	public void test() {
		String text = "mica 最牛逼";
		String hexText = DigestUtil.encodeHex(text.getBytes());
		String t = new String(DigestUtil.decodeHex(hexText));
		Assert.assertEquals(text, t);
	}

}
