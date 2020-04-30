package net.dreamlu.mica.test.utils;

import net.dreamlu.mica.core.tuple.KeyPair;
import net.dreamlu.mica.core.utils.Charsets;
import net.dreamlu.mica.core.utils.RsaUtil;
import org.junit.Assert;
import org.junit.Test;

public class RsaUtilTest {
	private final static String text = "我爱mica";

	@Test
	public void test0() {
		KeyPair keyPair = RsaUtil.genKeyPair();
		String encryptToBase64 = RsaUtil.encryptToBase64(keyPair.getPublicBase64(), text);
		String fromBase64 = RsaUtil.decryptFromBase64(keyPair.getPrivateBase64(), encryptToBase64);
		Assert.assertEquals(text, fromBase64);
	}

	@Test
	public void test1() {
		// js 生成的 data 采用 java 解密
		String data = "nSeojDaYM0+9hlXiKAtAxloBJL9M41pOfl5llHqh6oRAeVGL3dytyjQmBg4uAheTv7RQRn0xtlW3gnrsXoIHRRWZlD8wNp3Fogv86QXd5GvjRHKODMWskJRvlzXOVIoph9lodd8j50ycmjXNlwQfiBG/oYqcycufDDBgHU57wzE=";
		String pubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDN7Y4Cx2lJtZcPxLmSrHrcHUiDZGTVeHReOn9aTSTC/IGPMiOI+kFuC+3tS2k9GdQL68SUDHHsysoJ2CP6z1C3gPyhM2nVEJOhVu8OczVw0uGO6t7PGzVufwEQA19BLWYWY159EiFmNHiwkzmbsM4hAhEGnmjY6+f/8FtlN85cpwIDAQAB";
		String privateBase64 = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAM3tjgLHaUm1lw/EuZKsetwdSINkZNV4dF46f1pNJML8gY8yI4j6QW4L7e1LaT0Z1AvrxJQMcezKygnYI/rPULeA/KEzadUQk6FW7w5zNXDS4Y7q3s8bNW5/ARADX0EtZhZjXn0SIWY0eLCTOZuwziECEQaeaNjr5//wW2U3zlynAgMBAAECgYEAuaugyVymz/DMvUjw0RB2zhQRF3bje53ZvkQcI30+0hf9cPESHSUW7XQQGE5QGuk4yW7QlXQLHCdTt0CMYE2gRdUPkJS2S53efLGfSohCt0LusYFj5uxn3jZqMsr6STspWaWxNjF4DNWT7XOb73B5qG0Vt/v+Zav6/qk2Zy6btlECQQD2f5FU2JKJA7f5+2w3AGRno1AF+SyfmyLLPRwPdyC9DwWK6PGarWGOLC//DkQ8X4x55gtyRVao5KQ+TlWpWV67AkEA1d2kDYAlRJm8JmPMWQnAuaaRPliE7RBtn5wFQoRwGKRD5MtGOUGu7kyydICW7B0GEBmPdBkI27wu46J//pnZBQJAKIT9ydmz9MvksTYQKtZoqtSgseqs2CuzQ39vHmexvQI8IgJ6vLdlgu5mkWGx+86lE+Cp3wXg4fB7wzYzRJxJQwJBAJpAi4PlSnwJOkiiMJCD1UrSFe5W4EEwWTEG0lBgv402ZXXPG65ObsKX9RrMaI6SfH3+QZamO9ppB5TN7u2Ov5UCQQC3xJx2qMbwltY6mPYYU+CdkNeODKmAj1ZtaGEt4oGcEOhSKo6Q34cWlqurgE/vt5jlze0D2Z1ku75ARsx3TpMe";
		String decrypt = RsaUtil.decryptFromBase64(privateBase64, data);
		Assert.assertEquals(text, decrypt);
	}

	@Test
	public void test2() {
		// java 加密，js 解密
		String pubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDN7Y4Cx2lJtZcPxLmSrHrcHUiDZGTVeHReOn9aTSTC/IGPMiOI+kFuC+3tS2k9GdQL68SUDHHsysoJ2CP6z1C3gPyhM2nVEJOhVu8OczVw0uGO6t7PGzVufwEQA19BLWYWY159EiFmNHiwkzmbsM4hAhEGnmjY6+f/8FtlN85cpwIDAQAB";
		String privateBase64 = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAM3tjgLHaUm1lw/EuZKsetwdSINkZNV4dF46f1pNJML8gY8yI4j6QW4L7e1LaT0Z1AvrxJQMcezKygnYI/rPULeA/KEzadUQk6FW7w5zNXDS4Y7q3s8bNW5/ARADX0EtZhZjXn0SIWY0eLCTOZuwziECEQaeaNjr5//wW2U3zlynAgMBAAECgYEAuaugyVymz/DMvUjw0RB2zhQRF3bje53ZvkQcI30+0hf9cPESHSUW7XQQGE5QGuk4yW7QlXQLHCdTt0CMYE2gRdUPkJS2S53efLGfSohCt0LusYFj5uxn3jZqMsr6STspWaWxNjF4DNWT7XOb73B5qG0Vt/v+Zav6/qk2Zy6btlECQQD2f5FU2JKJA7f5+2w3AGRno1AF+SyfmyLLPRwPdyC9DwWK6PGarWGOLC//DkQ8X4x55gtyRVao5KQ+TlWpWV67AkEA1d2kDYAlRJm8JmPMWQnAuaaRPliE7RBtn5wFQoRwGKRD5MtGOUGu7kyydICW7B0GEBmPdBkI27wu46J//pnZBQJAKIT9ydmz9MvksTYQKtZoqtSgseqs2CuzQ39vHmexvQI8IgJ6vLdlgu5mkWGx+86lE+Cp3wXg4fB7wzYzRJxJQwJBAJpAi4PlSnwJOkiiMJCD1UrSFe5W4EEwWTEG0lBgv402ZXXPG65ObsKX9RrMaI6SfH3+QZamO9ppB5TN7u2Ov5UCQQC3xJx2qMbwltY6mPYYU+CdkNeODKmAj1ZtaGEt4oGcEOhSKo6Q34cWlqurgE/vt5jlze0D2Z1ku75ARsx3TpMe";
		String encrypt = RsaUtil.encryptByPrivateKeyToBase64(privateBase64, text.getBytes(Charsets.UTF_8));
		System.out.println(encrypt);
		String decrypt = RsaUtil.decryptByPublicKeyFromBase64(pubKey, encrypt);
		System.out.println(decrypt);
		Assert.assertEquals(text, decrypt);
	}

}
