package net.dreamlu.mica.test.utils;

import net.dreamlu.mica.core.utils.AesUtil;
import net.dreamlu.mica.core.utils.Base64Util;
import net.dreamlu.mica.core.utils.HexUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * aes 单元测试
 *
 * @author L.cm
 */
class AesUtilTest {

	@Test
	void test1() {
		String text = "我爱mica";
		String aesKey = AesUtil.genAesKey();
		System.out.println(aesKey);
		String encrypt = AesUtil.encryptToHex(text, aesKey);
		System.out.println(encrypt);
		String decrypt = AesUtil.decryptFormHexToString(encrypt, aesKey);
		Assertions.assertEquals(text, decrypt);
	}

	@Test
	void test2() {
		String text = "我爱mica";
		String aesKey = AesUtil.genAesKey();
		System.out.println(aesKey);
		String encrypt = AesUtil.encryptToBase64(text, aesKey);
		System.out.println(encrypt);
		String decrypt = AesUtil.decryptFormBase64ToString(encrypt, aesKey);
		Assertions.assertEquals(text, decrypt);
	}

	@Test
	void test4() {
		String text = "13912341234";
		String aesText = "bF4A/5d287weg+nHg5c5/g==";
		String aeskey = "O2BEeIv399qHQNhD6aGW8R8DEj4bqAAA";
		String decryptMysql = AesUtil.decryptMysqlToString(aesText, Base64Util::decodeFromString, aeskey);
		Assertions.assertEquals(text, decryptMysql);
		String encryptMysql = AesUtil.encryptMysql(text, aeskey, Base64Util::encodeToString);
		Assertions.assertEquals(aesText, encryptMysql);
	}

	@Test
	void testSafe() {
		String text = "mica_safe_aes_test";
		String keyStr = AesUtil.genAesKey();
		byte[] key = HexUtil.decode(keyStr);
		byte[] content = text.getBytes(StandardCharsets.UTF_8);

		// 1. 测试加解密正确性
		byte[] encrypted = AesUtil.encryptSafe(content, key);
		byte[] decrypted = AesUtil.decryptSafe(encrypted, key);
		Assertions.assertEquals(text, new String(decrypted, StandardCharsets.UTF_8));

		// 2. 测试随机IV (相同内容两次加密结果应该不同)
		byte[] encrypted2 = AesUtil.encryptSafe(content, key);
		Assertions.assertFalse(Arrays.equals(encrypted, encrypted2), "Random IV should produce different ciphertexts");
	}

	@Test
	void testSafeConvenience() {
		String text = "mica_safe_aes_convenience_test";
		String keyStr = AesUtil.genAesKey();

		// 1. String -> byte[] -> String
		byte[] encryptedBytes = AesUtil.encryptSafe(text, keyStr);
		byte[] decryptedBytes = AesUtil.decryptSafe(encryptedBytes, keyStr);
		Assertions.assertEquals(text, new String(decryptedBytes, StandardCharsets.UTF_8));

		// 2. Base64
		String base64 = AesUtil.encryptSafeToBase64(text, keyStr);
		String decryptedBase64 = AesUtil.decryptSafeFromBase64(base64, keyStr);
		Assertions.assertEquals(text, decryptedBase64);

		// 3. Hex
		String hex = AesUtil.encryptSafeToHex(text, keyStr);
		String decryptedHex = AesUtil.decryptSafeFromHex(hex, keyStr);
		Assertions.assertEquals(text, decryptedHex);
	}
}
