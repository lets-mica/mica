package net.dreamlu.mica.test.utils;

import net.dreamlu.mica.core.utils.AesUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * aes 单元测试
 *
 * @author L.cm
 */
public class AesUtilTest {

	@Test
	public void test1() {
		String text = "我爱mica";
		String aesKey = AesUtil.genAesKey();
		System.out.println(aesKey);
		String encrypt = AesUtil.encryptToHex(text, aesKey);
		System.out.println(encrypt);
		String decrypt = AesUtil.decryptFormHexToString(encrypt, aesKey);
		Assert.assertEquals(text, decrypt);
	}

	@Test
	public void test2() {
		String text = "我爱mica";
		String aesKey = AesUtil.genAesKey();
		System.out.println(aesKey);
		String encrypt = AesUtil.encryptToBase64(text, aesKey);
		System.out.println(encrypt);
		String decrypt = AesUtil.decryptFormBase64ToString(encrypt, aesKey);
		Assert.assertEquals(text, decrypt);
	}
}
