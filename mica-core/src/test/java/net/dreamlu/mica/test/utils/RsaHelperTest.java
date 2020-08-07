package net.dreamlu.mica.test.utils;

import lombok.SneakyThrows;
import net.dreamlu.mica.core.tuple.KeyPair;
import net.dreamlu.mica.core.utils.RsaHelper;
import net.dreamlu.mica.core.utils.RsaUtil;
import org.junit.Test;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

/**
 * @author caiqy
 * @date 2020/8/4 14:30
 */
public class RsaHelperTest {

	public static final String PKCS1_PUBLIC_KEY = "-----BEGIN RSA PUBLIC KEY-----\n" +
		"MIGJAoGBAKWQ0tiGaZYixh70pYoYqPRIhQcosSkhv6PED+jEbKd1AGSJQW+w4GDV\n" +
		"cAb5SeI8tdLwVQEaNXXDWpiv7V2GEchtZJGvv1lQVgqjDowtoQL2MBMUYuQ+9hwI\n" +
		"QyQNlloGUqHrHWL/yQUHLSogzVyHEQ4mfLdIIW46mocdKOR/RbupAgMBAAE=\n" +
		"-----END RSA PUBLIC KEY-----";
	public static final String PKCS1_PRIVATE_KEY = "-----BEGIN RSA PRIVATE KEY-----\n" +
		"MIICXQIBAAKBgQClkNLYhmmWIsYe9KWKGKj0SIUHKLEpIb+jxA/oxGyndQBkiUFv\n" +
		"sOBg1XAG+UniPLXS8FUBGjV1w1qYr+1dhhHIbWSRr79ZUFYKow6MLaEC9jATFGLk\n" +
		"PvYcCEMkDZZaBlKh6x1i/8kFBy0qIM1chxEOJny3SCFuOpqHHSjkf0W7qQIDAQAB\n" +
		"AoGABkIyHDYBiAe+vEUUfGLIATr8D1wYDVVdoo3K6apCLrbXKdW/695QbwymcFcM\n" +
		"VlFj8Ko91l20o2Pb+P1egExs/9pJ0fwNAvpuemIpbSEI8dhdhKoi+C36KizsHzNV\n" +
		"JJDceVVNnvgpNlClMRq54wHUjg8MZ20jkg7OscKAMrqRkEECQQC3InIOIjGnmxiL\n" +
		"ggK/Cu82V4sVJcydR5jLk97kERN/F7V/hMZ71RzDedA5UyFXP1Q0GvCwpmaIyN/+\n" +
		"0lgADEXpAkEA53DeEMbwJezjez+S4NQd0YEh5haoXvTHF+sTW/19mwB3phNfCmYe\n" +
		"+CYhepAnJKcSxABOtJ8WLJS3hAY1M+1vwQJBAK8R68zdHXDn0MgVCoENd+8QM3KN\n" +
		"BxVYC5aaDtsiA+xcgvKbwI9DBNNOESOr99SLQvjoxC1rddvPhGr7NIsfMHkCQQC9\n" +
		"9A/+2xitN/E47ePCLbZPhlnpO71zOhnSErlv7ezSdB6/qvR9V3Whm8IskvXdilbH\n" +
		"ka0HZ+7OJj082c9+0CjBAkBfQiOOPiSpX/NnpwtC7KJ/xNFLr5LxEbCOWDO0Hxws\n" +
		"NpeXM1ssX9ocoWsUGViKbCUHXy05++apJS4uOxMNHngW\n" +
		"-----END RSA PRIVATE KEY-----";
	public static final String PKCS8_PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----\n" +
		"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQClkNLYhmmWIsYe9KWKGKj0SIUH\n" +
		"KLEpIb+jxA/oxGyndQBkiUFvsOBg1XAG+UniPLXS8FUBGjV1w1qYr+1dhhHIbWSR\n" +
		"r79ZUFYKow6MLaEC9jATFGLkPvYcCEMkDZZaBlKh6x1i/8kFBy0qIM1chxEOJny3\n" +
		"SCFuOpqHHSjkf0W7qQIDAQAB\n" +
		"-----END PUBLIC KEY-----";
	public static final String PKCS8_PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----\n" +
		"MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKWQ0tiGaZYixh70\n" +
		"pYoYqPRIhQcosSkhv6PED+jEbKd1AGSJQW+w4GDVcAb5SeI8tdLwVQEaNXXDWpiv\n" +
		"7V2GEchtZJGvv1lQVgqjDowtoQL2MBMUYuQ+9hwIQyQNlloGUqHrHWL/yQUHLSog\n" +
		"zVyHEQ4mfLdIIW46mocdKOR/RbupAgMBAAECgYAGQjIcNgGIB768RRR8YsgBOvwP\n" +
		"XBgNVV2ijcrpqkIuttcp1b/r3lBvDKZwVwxWUWPwqj3WXbSjY9v4/V6ATGz/2knR\n" +
		"/A0C+m56YiltIQjx2F2EqiL4LfoqLOwfM1UkkNx5VU2e+Ck2UKUxGrnjAdSODwxn\n" +
		"bSOSDs6xwoAyupGQQQJBALcicg4iMaebGIuCAr8K7zZXixUlzJ1HmMuT3uQRE38X\n" +
		"tX+ExnvVHMN50DlTIVc/VDQa8LCmZojI3/7SWAAMRekCQQDncN4QxvAl7ON7P5Lg\n" +
		"1B3RgSHmFqhe9McX6xNb/X2bAHemE18KZh74JiF6kCckpxLEAE60nxYslLeEBjUz\n" +
		"7W/BAkEArxHrzN0dcOfQyBUKgQ137xAzco0HFVgLlpoO2yID7FyC8pvAj0ME004R\n" +
		"I6v31ItC+OjELWt128+Eavs0ix8weQJBAL30D/7bGK038Tjt48Ittk+GWek7vXM6\n" +
		"GdISuW/t7NJ0Hr+q9H1XdaGbwiyS9d2KVseRrQdn7s4mPTzZz37QKMECQF9CI44+\n" +
		"JKlf82enC0Lson/E0UuvkvERsI5YM7QfHCw2l5czWyxf2hyhaxQZWIpsJQdfLTn7\n" +
		"5qklLi47Ew0eeBY=\n" +
		"-----END PRIVATE KEY-----";


	@SneakyThrows
	@Test
	public void gen() {
		KeyPair keyPair = RsaUtil.genKeyPair(2048);
		RsaHelper rsaHelper = new RsaHelper((RSAPublicKey) keyPair.getPublic(), (RSAPrivateKey) keyPair.getPrivate());

		System.out.println("- - - - <pkcs1> - - - -");
		System.out.println(rsaHelper.toPemPKCS1(true));
		System.out.println(rsaHelper.toPemPKCS1(false));

		System.out.println("\n\n- - - - <pkcs8> - - - -");
		System.out.println(rsaHelper.toPemPKCS8(true));
		System.out.println(rsaHelper.toPemPKCS8(false));
	}

	@SneakyThrows
	@Test
	public void pkcs1Test() {
		RsaHelper pem = RsaHelper.fromPem(PKCS1_PRIVATE_KEY);

		String str = "我爱mica";

		//加密内容
		Cipher enc = Cipher.getInstance("RSA");
		enc.init(Cipher.ENCRYPT_MODE, pem.getRSAPublicKey());
		byte[] en = enc.doFinal(str.getBytes(StandardCharsets.UTF_8));
		System.out.println("【公钥加密】：");
		System.out.println(Base64.getEncoder().encodeToString(en));

		//解密内容
		Cipher dec = Cipher.getInstance("RSA");
		dec.init(Cipher.DECRYPT_MODE, pem.getRSAPrivateKey());
		byte[] de = dec.doFinal(en);
		System.out.println("【私钥解密】：");
		System.out.println(new String(de, StandardCharsets.UTF_8));


		//加密内容
		enc.init(Cipher.ENCRYPT_MODE, pem.getRSAPrivateKey());
		en = enc.doFinal(str.getBytes(StandardCharsets.UTF_8));
		System.out.println("【私钥加密】：");
		System.out.println(Base64.getEncoder().encodeToString(en));

		//解密内容
		dec.init(Cipher.DECRYPT_MODE, pem.getRSAPublicKey());
		de = dec.doFinal(en);
		System.out.println("【公钥解密】：");
		System.out.println(new String(de, StandardCharsets.UTF_8));
	}


	@SneakyThrows
	@Test
	public void pkcs8Test() {
		RsaHelper pem = RsaHelper.fromPem(PKCS8_PRIVATE_KEY);
		String str = "我爱mica";

		//加密内容
		Cipher enc = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		enc.init(Cipher.ENCRYPT_MODE, pem.getRSAPublicKey());
		byte[] en = enc.doFinal(str.getBytes(StandardCharsets.UTF_8));
		System.out.println("【公钥加密】：");
		System.out.println(Base64.getEncoder().encodeToString(en));

		//解密内容
		Cipher dec = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		dec.init(Cipher.DECRYPT_MODE, pem.getRSAPrivateKey());
		byte[] de = dec.doFinal(en);
		System.out.println("【私钥解密】：");
		System.out.println(new String(de, StandardCharsets.UTF_8));


		//加密内容
		enc.init(Cipher.ENCRYPT_MODE, pem.getRSAPrivateKey());
		en = enc.doFinal(str.getBytes(StandardCharsets.UTF_8));
		System.out.println("【私钥加密】：");
		System.out.println(Base64.getEncoder().encodeToString(en));

		//解密内容
		dec.init(Cipher.DECRYPT_MODE, pem.getRSAPublicKey());
		de = dec.doFinal(en);
		System.out.println("【公钥解密】：");
		System.out.println(new String(de, StandardCharsets.UTF_8));
	}


	@SneakyThrows
	@Test
	public void pkcs1Pkcs8Test() {
		RsaHelper pem = RsaHelper.fromPem(PKCS1_PRIVATE_KEY);
		String str = "我爱mica";

		//加密内容
		Cipher enc = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		enc.init(Cipher.ENCRYPT_MODE, pem.getRSAPublicKey());
		byte[] en = enc.doFinal(str.getBytes(StandardCharsets.UTF_8));
		System.out.println("【公钥加密】：");
		System.out.println(Base64.getEncoder().encodeToString(en));

		RsaHelper pem2 = RsaHelper.fromPem(PKCS8_PRIVATE_KEY);
		//解密内容
		Cipher dec = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		dec.init(Cipher.DECRYPT_MODE, pem2.getRSAPrivateKey());
		byte[] de = dec.doFinal(en);
		System.out.println("【私钥解密】：");
		System.out.println(new String(de, StandardCharsets.UTF_8));
	}

	@SneakyThrows
	@Test
	public void pkcs8Pkcs1Test() {
		RsaHelper pem = RsaHelper.fromPem(PKCS8_PRIVATE_KEY);
		String str = "我爱mica";

		//加密内容
		Cipher enc = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		enc.init(Cipher.ENCRYPT_MODE, pem.getRSAPublicKey());
		byte[] en = enc.doFinal(str.getBytes(StandardCharsets.UTF_8));
		System.out.println("【公钥加密】：");
		System.out.println(Base64.getEncoder().encodeToString(en));

		RsaHelper pem2 = RsaHelper.fromPem(PKCS1_PRIVATE_KEY);
		//解密内容
		Cipher dec = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		dec.init(Cipher.DECRYPT_MODE, pem2.getRSAPrivateKey());
		byte[] de = dec.doFinal(en);
		System.out.println("【私钥解密】：");
		System.out.println(new String(de, StandardCharsets.UTF_8));
	}

	@SneakyThrows
	@Test
	public void xmlTest() {
		RsaHelper helper = RsaHelper.fromPem(PKCS8_PRIVATE_KEY);
		String xml = helper.toXml(false);
		System.out.println(xml);
		RsaHelper rsaHelper = RsaHelper.fromXml(xml);
		RSAPrivateKey privateKey = rsaHelper.getRSAPrivateKey();
		System.out.println();
		System.out.println(RsaUtil.getKeyString(privateKey));
	}
}
