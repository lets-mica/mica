package net.dreamlu.mica.test.utils;

import net.dreamlu.mica.core.utils.DesUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * des 单元测试
 *
 * @author L.cm
 */
class DesUtilTest {

	@Test
	void test() {
		String text = "我爱mica";
		String aesKey = DesUtil.genDesKey();
		String encryptHex = DesUtil.encryptToHex(text, aesKey);
		String decryptHex = DesUtil.decryptFormHex(encryptHex, aesKey);
		Assertions.assertEquals(text, decryptHex);
		String encryptBase64 = DesUtil.encryptToBase64(text, aesKey);
		String decryptBase64 = DesUtil.decryptFormBase64(encryptBase64, aesKey);
		Assertions.assertEquals(text, decryptBase64);
	}
}
