package net.dreamlu.mica.core.utils;

import org.jspecify.annotations.Nullable;
import org.springframework.util.Assert;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

/**
 * 完全兼容微信所使用的AES加密方式。
 * aes的key必须是256byte长（比如32个字符），可以使用AesKit.genAesKey()来生成一组key
 * <p>
 * 参考自：jFinal AESKit，优化，方便使用
 *
 * @author L.cm
 */
public class AesUtil {

	/**
	 * 生成 aes key
	 *
	 * @return aes key
	 */
	public static String genAesKey() {
		try {
			KeyGenerator keyGen = KeyGenerator.getInstance("AES");
			keyGen.init(128, Holder.SECURE_RANDOM);
			byte[] aesKey = keyGen.generateKey().getEncoded();
			return HexUtil.encodeToString(aesKey);
		} catch (NoSuchAlgorithmException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 转换成mysql aes
	 *
	 * @param key key
	 * @return SecretKeySpec
	 */
	public static SecretKeySpec genMySqlAesKey(final byte[] key) {
		final byte[] finalKey = new byte[16];
		int i = 0;
		for (byte b : key) {
			finalKey[i++ % 16] ^= b;
		}
		return new SecretKeySpec(finalKey, "AES");
	}

	/**
	 * 转换成mysql aes
	 *
	 * @param key key
	 * @return SecretKeySpec
	 */
	public static SecretKeySpec genMySqlAesKey(final String key) {
		return genMySqlAesKey(key.getBytes(StandardCharsets.UTF_8));
	}

	public static String encryptToHex(String content, String aesTextKey) {
		return HexUtil.encodeToString(encrypt(content, aesTextKey));
	}

	public static String encryptToHex(byte[] content, String aesTextKey) {
		return HexUtil.encodeToString(encrypt(content, aesTextKey));
	}

	public static String encryptToBase64(String content, String aesTextKey) {
		return Base64Util.encodeToString(encrypt(content, aesTextKey));
	}

	public static String encryptToBase64(byte[] content, String aesTextKey) {
		return Base64Util.encodeToString(encrypt(content, aesTextKey));
	}

	public static byte[] encrypt(String content, String aesTextKey) {
		return encrypt(content.getBytes(StandardCharsets.UTF_8), aesTextKey);
	}

	public static byte[] encrypt(String content, Charset charset, String aesTextKey) {
		return encrypt(content.getBytes(charset), aesTextKey);
	}

	public static byte[] encrypt(byte[] content, String aesTextKey) {
		return encrypt(content, Objects.requireNonNull(aesTextKey).getBytes(StandardCharsets.UTF_8));
	}

	@Nullable
	public static String decryptFormHexToString(@Nullable String content, String aesTextKey) {
		byte[] hexBytes = decryptFormHex(content, aesTextKey);
		if (hexBytes == null) {
			return null;
		}
		return new String(hexBytes, StandardCharsets.UTF_8);
	}

	public static byte @Nullable [] decryptFormHex(@Nullable String content, String aesTextKey) {
		if (StringUtil.isBlank(content)) {
			return null;
		}
		return decryptFormHex(content.getBytes(StandardCharsets.UTF_8), aesTextKey);
	}

	public static byte[] decryptFormHex(byte[] content, String aesTextKey) {
		return decrypt(HexUtil.decode(content), aesTextKey);
	}

	@Nullable
	public static String decryptFormBase64ToString(@Nullable String content, String aesTextKey) {
		byte[] hexBytes = decryptFormBase64(content, aesTextKey);
		if (hexBytes == null) {
			return null;
		}
		return new String(hexBytes, StandardCharsets.UTF_8);
	}

	public static byte @Nullable [] decryptFormBase64(@Nullable String content, String aesTextKey) {
		if (StringUtil.isBlank(content)) {
			return null;
		}
		return decryptFormBase64(content.getBytes(StandardCharsets.UTF_8), aesTextKey);
	}

	public static byte[] decryptFormBase64(byte[] content, String aesTextKey) {
		return decrypt(Base64Util.decode(content), aesTextKey);
	}

	public static String decryptToString(byte[] content, String aesTextKey) {
		return new String(decrypt(content, aesTextKey), StandardCharsets.UTF_8);
	}

	public static byte[] decrypt(byte[] content, String aesTextKey) {
		return decrypt(content, Objects.requireNonNull(aesTextKey).getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * AES加密
	 *
	 * @param content 内容
	 * @param aesKey  key
	 * @return encrypted
	 * @deprecated use {@link #encryptSafe(byte[], byte[])}
	 */
	@Deprecated
	public static byte[] encrypt(byte[] content, byte[] aesKey) {
		return aes(Pkcs7Encoder.encode(content), aesKey, Cipher.ENCRYPT_MODE);
	}

	/**
	 * AES解密
	 *
	 * @param encrypted encrypted
	 * @param aesKey    key
	 * @return decrypted
	 * @deprecated use {@link #decryptSafe(byte[], byte[])}
	 */
	@Deprecated
	public static byte[] decrypt(byte[] encrypted, byte[] aesKey) {
		return Pkcs7Encoder.decode(aes(encrypted, aesKey, Cipher.DECRYPT_MODE));
	}

	@Deprecated
	private static byte[] aes(byte[] encrypted, byte[] aesKey, int mode) {
		Assert.isTrue(aesKey.length == 32, "IllegalAesKey, aesKey's length must be 32");
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
			SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
			IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(aesKey, 0, 16));
			cipher.init(mode, keySpec, iv);
			return cipher.doFinal(encrypted);
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * AES 加密，使用随机 IV，兼容性强，更安全
	 *
	 * @param content 内容
	 * @param aesKey  key
	 * @return iv + encrypted
	 */
	public static byte[] encryptSafe(byte[] content, byte[] aesKey) {
		try {
			byte[] iv = new byte[16];
			Holder.SECURE_RANDOM.nextBytes(iv);
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(aesKey, "AES"), new IvParameterSpec(iv));
			byte[] encrypted = cipher.doFinal(content);
			byte[] result = new byte[iv.length + encrypted.length];
			System.arraycopy(iv, 0, result, 0, iv.length);
			System.arraycopy(encrypted, 0, result, iv.length, encrypted.length);
			return result;
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * AES 加密，使用随机 IV，兼容性强，更安全
	 *
	 * @param content 内容
	 * @param aesKey  key
	 * @return iv + encrypted
	 */
	public static byte[] encryptSafe(String content, String aesKey) {
		return encryptSafe(content.getBytes(StandardCharsets.UTF_8), Objects.requireNonNull(aesKey).getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * AES 加密，使用随机 IV，兼容性强，更安全
	 *
	 * @param content 内容
	 * @param aesKey  key
	 * @return iv + encrypted base64
	 */
	public static String encryptSafeToBase64(String content, String aesKey) {
		return Base64Util.encodeToString(encryptSafe(content, aesKey));
	}

	/**
	 * AES 加密，使用随机 IV，兼容性强，更安全
	 *
	 * @param content 内容
	 * @param aesKey  key
	 * @return iv + encrypted hex
	 */
	public static String encryptSafeToHex(String content, String aesKey) {
		return HexUtil.encodeToString(encryptSafe(content, aesKey));
	}

	/**
	 * AES 解密，使用随机 IV
	 *
	 * @param content iv + encrypted
	 * @param aesKey  key
	 * @return decrypted
	 */
	public static byte[] decryptSafe(byte[] content, byte[] aesKey) {
		try {
			byte[] iv = new byte[16];
			System.arraycopy(content, 0, iv, 0, iv.length);
			byte[] encrypted = new byte[content.length - 16];
			System.arraycopy(content, 16, encrypted, 0, encrypted.length);
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(aesKey, "AES"), new IvParameterSpec(iv));
			return cipher.doFinal(encrypted);
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * AES 解密，使用随机 IV
	 *
	 * @param content iv + encrypted
	 * @param aesKey  key
	 * @return decrypted
	 */
	public static byte[] decryptSafe(byte[] content, String aesKey) {
		return decryptSafe(content, Objects.requireNonNull(aesKey).getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * AES 解密，使用随机 IV
	 *
	 * @param content iv + encrypted base64
	 * @param aesKey  key
	 * @return decrypted string
	 */
	public static String decryptSafeFromBase64(String content, String aesKey) {
		byte[] bytes = Base64Util.decodeFromString(content);
		return new String(decryptSafe(bytes, aesKey), StandardCharsets.UTF_8);
	}

	/**
	 * AES 解密，使用随机 IV
	 *
	 * @param content iv + encrypted hex
	 * @param aesKey  key
	 * @return decrypted string
	 */
	@Nullable
	public static String decryptSafeFromHex(String content, String aesKey) {
		byte[] bytes = HexUtil.decode(content);
		if (bytes == null) {
			return null;
		}
		return new String(decryptSafe(bytes, aesKey), StandardCharsets.UTF_8);
	}


	/**
	 * 兼容 mysql 的 aes 加密
	 *
	 * @param input  input
	 * @param aesKey aesKey
	 * @return byte array
	 */
	@Deprecated
	public static byte[] encryptMysql(String input, String aesKey) {
		return encryptMysql(input, aesKey, Function.identity());
	}

	/**
	 * 兼容 mysql 的 aes 加密
	 *
	 * @param input  input
	 * @param aesKey aesKey
	 * @param <T>    泛型标记
	 * @return T 泛型对象
	 */
	public static <T> T encryptMysql(String input, String aesKey, Function<byte[], T> mapper) {
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, genMySqlAesKey(aesKey));
			byte[] bytes = cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
			return mapper.apply(bytes);
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 兼容 mysql 的 aes 加密
	 *
	 * @param input  input
	 * @param aesKey aesKey
	 * @return byte 数组
	 */
	public static byte[] decryptMysql(String input, String aesKey) {
		return decryptMysql(input, txt -> txt.getBytes(StandardCharsets.UTF_8), aesKey);
	}

	/**
	 * 兼容 mysql 的 aes 加密
	 *
	 * @param input  input
	 * @param aesKey aesKey
	 * @return byte 数组
	 */
	public static byte[] decryptMysql(String input, Function<String, byte[]> inputMapper, String aesKey) {
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, genMySqlAesKey(aesKey));
			return cipher.doFinal(inputMapper.apply(input));
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 兼容 mysql 的 aes 加密
	 *
	 * @param input       input
	 * @param inputMapper Function
	 * @param aesKey      aesKey
	 * @return 字符串
	 */
	public static String decryptMysqlToString(String input, Function<String, byte[]> inputMapper, String aesKey) {
		return new String(decryptMysql(input, inputMapper, aesKey), StandardCharsets.UTF_8);
	}

	/**
	 * 兼容 mysql 的 aes 加密
	 *
	 * @param input  input
	 * @param aesKey aesKey
	 * @return 字符串
	 */
	public static String decryptMysqlToString(String input, String aesKey) {
		return decryptMysqlToString(input, txt -> txt.getBytes(StandardCharsets.UTF_8), aesKey);
	}

}
