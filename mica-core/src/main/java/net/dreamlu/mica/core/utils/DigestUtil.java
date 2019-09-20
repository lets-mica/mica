/*
 * Copyright (c) 2019-2029, Dreamlu 卢春梦 (596392912@qq.com & www.dreamlu.net).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.dreamlu.mica.core.utils;

import lombok.experimental.UtilityClass;
import org.springframework.lang.Nullable;
import org.springframework.util.DigestUtils;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密相关工具类直接使用Spring util封装，减少jar依赖
 *
 * @author L.cm
 */
@UtilityClass
public class DigestUtil {
	private static final String HEX_VALUE = "0123456789abcdef";
	private static final char[] HEX_CODE = HEX_VALUE.toCharArray();

	/**
	 * Calculates the MD5 digest.
	 *
	 * @param data Data to digest
	 * @return MD5 digest as a hex array
	 */
	public static byte[] md5(final byte[] data) {
		return DigestUtils.md5Digest(data);
	}

	/**
	 * Calculates the MD5 digest.
	 *
	 * @param data Data to digest
	 * @return MD5 digest as a hex array
	 */
	public static byte[] md5(final String data) {
		return DigestUtils.md5Digest(data.getBytes(Charsets.UTF_8));
	}

	/**
	 * Calculates the MD5 digest and returns the value as a 32 character hex string.
	 *
	 * @param data Data to digest
	 * @return MD5 digest as a hex string
	 */
	public static String md5Hex(final String data) {
		return DigestUtils.md5DigestAsHex(data.getBytes(Charsets.UTF_8));
	}

	/**
	 * Return a hexadecimal string representation of the MD5 digest of the given bytes.
	 *
	 * @param bytes the bytes to calculate the digest over
	 * @return a hexadecimal digest string
	 */
	public static String md5Hex(final byte[] bytes) {
		return DigestUtils.md5DigestAsHex(bytes);
	}

	/**
	 * sha1
	 *
	 * @param data Data to digest
	 * @return digest as a hex array
	 */
	public static byte[] sha1(String data) {
		return DigestUtil.sha1(data.getBytes(Charsets.UTF_8));
	}

	/**
	 * sha1
	 *
	 * @param bytes Data to digest
	 * @return digest as a hex array
	 */
	public static byte[] sha1(final byte[] bytes) {
		return DigestUtil.digest("SHA-1", bytes);
	}

	/**
	 * sha1Hex
	 *
	 * @param data Data to digest
	 * @return digest as a hex string
	 */
	public static String sha1Hex(String data) {
		return DigestUtil.encodeHex(sha1(data.getBytes(Charsets.UTF_8)));
	}

	/**
	 * sha1Hex
	 *
	 * @param bytes Data to digest
	 * @return digest as a hex string
	 */
	public static String sha1Hex(final byte[] bytes) {
		return DigestUtil.encodeHex(sha1(bytes));
	}

	/**
	 * SHA224
	 *
	 * @param data Data to digest
	 * @return digest as a byte array
	 */
	public static byte[] sha224(String data) {
		return DigestUtil.sha224(data.getBytes(Charsets.UTF_8));
	}

	/**
	 * SHA224
	 *
	 * @param bytes Data to digest
	 * @return digest as a byte array
	 */
	public static byte[] sha224(final byte[] bytes) {
		return DigestUtil.digest("SHA-224", bytes);
	}

	/**
	 * SHA224Hex
	 *
	 * @param data Data to digest
	 * @return digest as a hex string
	 */
	public static String sha224Hex(String data) {
		return DigestUtil.encodeHex(sha224(data.getBytes(Charsets.UTF_8)));
	}

	/**
	 * SHA224Hex
	 *
	 * @param bytes Data to digest
	 * @return digest as a hex string
	 */
	public static String sha224Hex(final byte[] bytes) {
		return DigestUtil.encodeHex(sha224(bytes));
	}

	/**
	 * sha256Hex
	 *
	 * @param data Data to digest
	 * @return digest as a byte array
	 */
	public static byte[] sha256(String data) {
		return DigestUtil.sha256(data.getBytes(Charsets.UTF_8));
	}

	/**
	 * sha256Hex
	 *
	 * @param bytes Data to digest
	 * @return digest as a byte array
	 */
	public static byte[] sha256(final byte[] bytes) {
		return DigestUtil.digest("SHA-256", bytes);
	}

	/**
	 * sha256Hex
	 *
	 * @param data Data to digest
	 * @return digest as a hex string
	 */
	public static String sha256Hex(String data) {
		return DigestUtil.encodeHex(sha256(data.getBytes(Charsets.UTF_8)));
	}

	/**
	 * sha256Hex
	 *
	 * @param bytes Data to digest
	 * @return digest as a hex string
	 */
	public static String sha256Hex(final byte[] bytes) {
		return DigestUtil.encodeHex(sha256(bytes));
	}

	/**
	 * sha384
	 *
	 * @param data Data to digest
	 * @return digest as a byte array
	 */
	public static byte[] sha384(String data) {
		return DigestUtil.sha384(data.getBytes(Charsets.UTF_8));
	}

	/**
	 * sha384
	 *
	 * @param bytes Data to digest
	 * @return digest as a byte array
	 */
	public static byte[] sha384(final byte[] bytes) {
		return DigestUtil.digest("SHA-384", bytes);
	}

	/**
	 * sha384Hex
	 *
	 * @param data Data to digest
	 * @return digest as a hex string
	 */
	public static String sha384Hex(String data) {
		return DigestUtil.encodeHex(sha384(data.getBytes(Charsets.UTF_8)));
	}

	/**
	 * sha384Hex
	 *
	 * @param bytes Data to digest
	 * @return digest as a hex string
	 */
	public static String sha384Hex(final byte[] bytes) {
		return DigestUtil.encodeHex(sha384(bytes));
	}

	/**
	 * sha512Hex
	 *
	 * @param data Data to digest
	 * @return digest as a byte array
	 */
	public static byte[] sha512(String data) {
		return DigestUtil.sha512(data.getBytes(Charsets.UTF_8));
	}

	/**
	 * sha512Hex
	 *
	 * @param bytes Data to digest
	 * @return digest as a byte array
	 */
	public static byte[] sha512(final byte[] bytes) {
		return DigestUtil.digest("SHA-512", bytes);
	}

	/**
	 * sha512Hex
	 *
	 * @param data Data to digest
	 * @return digest as a hex string
	 */
	public static String sha512Hex(String data) {
		return DigestUtil.encodeHex(sha512(data.getBytes(Charsets.UTF_8)));
	}

	/**
	 * sha512Hex
	 *
	 * @param bytes Data to digest
	 * @return digest as a hex string
	 */
	public static String sha512Hex(final byte[] bytes) {
		return DigestUtil.encodeHex(sha512(bytes));
	}

	/**
	 * digest
	 *
	 * @param algorithm 算法
	 * @param bytes     Data to digest
	 * @return digest byte array
	 */
	public static byte[] digest(String algorithm, byte[] bytes) {
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			return md.digest(bytes);
		} catch (NoSuchAlgorithmException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * digest Hex
	 *
	 * @param algorithm 算法
	 * @param bytes     Data to digest
	 * @return digest as a hex string
	 */
	public static String digestHex(String algorithm, byte[] bytes) {
		return DigestUtil.encodeHex(digest(algorithm, bytes));
	}

	/**
	 * hmacMd5
	 *
	 * @param data Data to digest
	 * @param key  key
	 * @return digest as a byte array
	 */
	public static byte[] hmacMd5(String data, String key) {
		return DigestUtil.hmacMd5(data.getBytes(Charsets.UTF_8), key);
	}

	/**
	 * hmacMd5
	 *
	 * @param bytes Data to digest
	 * @param key   key
	 * @return digest as a byte array
	 */
	public static byte[] hmacMd5(final byte[] bytes, String key) {
		return DigestUtil.digestHmac("HmacMD5", bytes, key);
	}

	/**
	 * hmacMd5 Hex
	 *
	 * @param data Data to digest
	 * @param key  key
	 * @return digest as a hex string
	 */
	public static String hmacMd5Hex(String data, String key) {
		return DigestUtil.encodeHex(hmacMd5(data.getBytes(Charsets.UTF_8), key));
	}

	/**
	 * hmacMd5 Hex
	 *
	 * @param bytes Data to digest
	 * @param key   key
	 * @return digest as a hex string
	 */
	public static String hmacMd5Hex(final byte[] bytes, String key) {
		return DigestUtil.encodeHex(hmacMd5(bytes, key));
	}

	/**
	 * hmacSha1
	 *
	 * @param data Data to digest
	 * @param key  key
	 * @return digest as a byte array
	 */
	public static byte[] hmacSha1(String data, String key) {
		return DigestUtil.hmacSha1(data.getBytes(Charsets.UTF_8), key);
	}

	/**
	 * hmacSha1
	 *
	 * @param bytes Data to digest
	 * @param key   key
	 * @return digest as a byte array
	 */
	public static byte[] hmacSha1(final byte[] bytes, String key) {
		return DigestUtil.digestHmac("HmacSHA1", bytes, key);
	}

	/**
	 * hmacSha1 Hex
	 *
	 * @param data Data to digest
	 * @param key  key
	 * @return digest as a hex string
	 */
	public static String hmacSha1Hex(String data, String key) {
		return DigestUtil.encodeHex(hmacSha1(data.getBytes(Charsets.UTF_8), key));
	}

	/**
	 * hmacSha1 Hex
	 *
	 * @param bytes Data to digest
	 * @param key   key
	 * @return digest as a hex string
	 */
	public static String hmacSha1Hex(final byte[] bytes, String key) {
		return DigestUtil.encodeHex(hmacSha1(bytes, key));
	}

	/**
	 * hmacSha224
	 *
	 * @param data Data to digest
	 * @param key  key
	 * @return digest as a hex string
	 */
	public static byte[] hmacSha224(String data, String key) {
		return DigestUtil.hmacSha224(data.getBytes(Charsets.UTF_8), key);
	}

	/**
	 * hmacSha224
	 *
	 * @param bytes Data to digest
	 * @param key   key
	 * @return digest as a hex string
	 */
	public static byte[] hmacSha224(final byte[] bytes, String key) {
		return DigestUtil.digestHmac("HmacSHA224", bytes, key);
	}

	/**
	 * hmacSha224 Hex
	 *
	 * @param data Data to digest
	 * @param key  key
	 * @return digest as a hex string
	 */
	public static String hmacSha224Hex(String data, String key) {
		return DigestUtil.encodeHex(hmacSha224(data.getBytes(Charsets.UTF_8), key));
	}

	/**
	 * hmacSha224 Hex
	 *
	 * @param bytes Data to digest
	 * @param key   key
	 * @return digest as a hex string
	 */
	public static String hmacSha224Hex(final byte[] bytes, String key) {
		return DigestUtil.encodeHex(hmacSha224(bytes, key));
	}

	/**
	 * hmacSha256
	 *
	 * @param data Data to digest
	 * @param key  key
	 * @return digest as a hex string
	 */
	public static byte[] hmacSha256(String data, String key) {
		return DigestUtil.hmacSha256(data.getBytes(Charsets.UTF_8), key);
	}

	/**
	 * hmacSha256
	 *
	 * @param bytes Data to digest
	 * @param key   key
	 * @return digest as a byte array
	 */
	public static byte[] hmacSha256(final byte[] bytes, String key) {
		return DigestUtil.digestHmac("HmacSHA256", bytes, key);
	}

	/**
	 * hmacSha256 Hex
	 *
	 * @param data Data to digest
	 * @param key  key
	 * @return digest as a byte array
	 */
	public static String hmacSha256Hex(String data, String key) {
		return DigestUtil.encodeHex(hmacSha256(data.getBytes(Charsets.UTF_8), key));
	}

	/**
	 * hmacSha256 Hex
	 *
	 * @param bytes Data to digest
	 * @param key   key
	 * @return digest as a hex string
	 */
	public static String hmacSha256Hex(final byte[] bytes, String key) {
		return DigestUtil.encodeHex(hmacSha256(bytes, key));
	}

	/**
	 * hmacSha384
	 *
	 * @param data Data to digest
	 * @param key  key
	 * @return digest as a byte array
	 */
	public static byte[] hmacSha384(String data, String key) {
		return DigestUtil.hmacSha384(data.getBytes(Charsets.UTF_8), key);
	}

	/**
	 * hmacSha384
	 *
	 * @param bytes Data to digest
	 * @param key   key
	 * @return digest as a byte array
	 */
	public static byte[] hmacSha384(final byte[] bytes, String key) {
		return DigestUtil.digestHmac("HmacSHA384", bytes, key);
	}

	/**
	 * hmacSha384 Hex
	 *
	 * @param data Data to digest
	 * @param key  key
	 * @return digest as a hex string
	 */
	public static String hmacSha384Hex(String data, String key) {
		return DigestUtil.encodeHex(hmacSha384(data.getBytes(Charsets.UTF_8), key));
	}

	/**
	 * hmacSha384 Hex
	 *
	 * @param bytes Data to digest
	 * @param key   key
	 * @return digest as a hex string
	 */
	public static String hmacSha384Hex(final byte[] bytes, String key) {
		return DigestUtil.encodeHex(hmacSha384(bytes, key));
	}

	/**
	 * hmacSha512
	 *
	 * @param data Data to digest
	 * @param key  key
	 * @return digest as a byte array
	 */
	public static byte[] hmacSha512(String data, String key) {
		return DigestUtil.hmacSha512(data.getBytes(Charsets.UTF_8), key);
	}

	/**
	 * hmacSha512
	 *
	 * @param bytes Data to digest
	 * @param key   key
	 * @return digest as a byte array
	 */
	public static byte[] hmacSha512(final byte[] bytes, String key) {
		return DigestUtil.digestHmac("HmacSHA512", bytes, key);
	}

	/**
	 * hmacSha512 Hex
	 *
	 * @param data Data to digest
	 * @param key  key
	 * @return digest as a hex string
	 */
	public static String hmacSha512Hex(String data, String key) {
		return DigestUtil.encodeHex(hmacSha512(data.getBytes(Charsets.UTF_8), key));
	}

	/**
	 * hmacSha512 Hex
	 *
	 * @param bytes Data to digest
	 * @param key   key
	 * @return digest as a hex string
	 */
	public static String hmacSha512Hex(final byte[] bytes, String key) {
		return DigestUtil.encodeHex(hmacSha512(bytes, key));
	}

	/**
	 * digest Hmac Hex
	 *
	 * @param algorithm 算法
	 * @param bytes     Data to digest
	 * @return digest as a hex string
	 */
	public static String digestHmacHex(String algorithm, final byte[] bytes, String key) {
		return DigestUtil.encodeHex(DigestUtil.digestHmac(algorithm, bytes, key));
	}

	/**
	 * digest Hmac
	 *
	 * @param algorithm 算法
	 * @param bytes     Data to digest
	 * @return digest as a byte array
	 */
	public static byte[] digestHmac(String algorithm, final byte[] bytes, String key) {
		SecretKey secretKey = new SecretKeySpec(key.getBytes(Charsets.UTF_8), algorithm);
		try {
			Mac mac = Mac.getInstance(secretKey.getAlgorithm());
			mac.init(secretKey);
			return mac.doFinal(bytes);
		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * encode Hex
	 *
	 * @param bytes Data to Hex
	 * @return bytes as a hex string
	 */
	public static String encodeHex(byte[] bytes) {
		StringBuilder r = new StringBuilder(bytes.length * 2);
		for (byte b : bytes) {
			r.append(HEX_CODE[(b >> 4) & 0xF]);
			r.append(HEX_CODE[(b & 0xF)]);
		}
		return r.toString();
	}

	/**
	 * decode Hex
	 *
	 * @param hexStr Hex string
	 * @return decode hex to bytes
	 */
	public static byte[] decodeHex(final String hexStr) {
		int len = hexStr.length();
		if ((len & 0x01) != 0) {
			throw new IllegalArgumentException("hexBinary needs to be even-length: " + hexStr);
		}
		String hexText = hexStr.toLowerCase();
		byte[] out = new byte[len >> 1];
		for (int i = 0; i < len; i += 2) {
			int hn = HEX_VALUE.indexOf(hexText.charAt(i));
			int ln = HEX_VALUE.indexOf(hexText.charAt(i + 1));
			if (hn == -1 || ln == -1) {
				throw new IllegalArgumentException("contains illegal character for hexBinary: " + hexStr);
			}
			out[i / 2] = (byte) ((hn << 4) | ln);
		}
		return out;
	}

	/**
	 * 比较字符串，避免字符串因为过长，产生耗时
	 *
	 * @param a String
	 * @param b String
	 * @return 是否相同
	 */
	public static boolean slowEquals(@Nullable String a, @Nullable String b) {
		if (a == null || b == null) {
			return false;
		}
		return DigestUtil.slowEquals(a.getBytes(Charsets.UTF_8), b.getBytes(Charsets.UTF_8));
	}

	/**
	 * 比较 byte 数组，避免字符串因为过长，产生耗时
	 *
	 * @param a byte array
	 * @param b byte array
	 * @return 是否相同
	 */
	public static boolean slowEquals(@Nullable byte[] a, @Nullable byte[] b) {
		if (a == null || b == null) {
			return false;
		}
		if (a.length != b.length) {
			return false;
		}
		int diff = a.length ^ b.length;
		for (int i = 0; i < a.length && i < b.length; i++) {
			diff |= a[i] ^ b[i];
		}
		return diff == 0;
	}
}
