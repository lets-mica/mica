package net.dreamlu.mica.test.utils;

import net.dreamlu.mica.core.utils.AesUtil;
import net.dreamlu.mica.core.utils.Base64Util;
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

	@Test
	public void test4() {
		String text = "13912341234";
		String aesText = "bF4A/5d287weg+nHg5c5/g==";
		String aeskey = "O2BEeIv399qHQNhD6aGW8R8DEj4bqAAA";
		String decryptMysql = AesUtil.decryptMysqlToString(aesText, Base64Util::decodeFromString, aeskey);
		Assert.assertEquals(text, decryptMysql);
		String encryptMysql = AesUtil.encryptMysql(text, aeskey, Base64Util::encodeToString);
		Assert.assertEquals(aesText, encryptMysql);
	}
}
