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

import net.dreamlu.mica.core.tuple.KeyPair;
import org.springframework.lang.Nullable;
import org.springframework.util.FastByteArrayOutputStream;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAKey;
import java.security.spec.*;
import java.util.Objects;

/**
 * RSA加、解密工具
 *
 * <p>
 * 1. 公钥负责加密，私钥负责解密；
 * 2. 私钥负责签名，公钥负责验证。
 * </p>
 *
 * @author L.cm
 */
public class RsaUtil {
	/**
	 * 数字签名，密钥算法
	 */
	public static final String RSA_ALGORITHM = "RSA";
	public static final String RSA_PADDING = "RSA/ECB/PKCS1Padding";

	/**
	 * 获取 KeyPair
	 *
	 * @return KeyPair
	 */
	public static KeyPair genKeyPair() {
		return genKeyPair(1024);
	}

	public static void main(String[] args) throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(1024); // 指定密钥长度
		java.security.KeyPair keyPair = keyPairGenerator.generateKeyPair();

		PrivateKey privateKey = keyPair.getPrivate();
		PublicKey publicKey = keyPair.getPublic();
		byte[] privateBytes = genKeyPair().getPrivateBytes();
		System.out.println(privateBytes);
	}

	/**
	 * 获取 KeyPair
	 *
	 * @param keySize key size
	 * @return KeyPair
	 */
	public static KeyPair genKeyPair(int keySize) {
		try {
			KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(RSA_ALGORITHM);
			// 密钥位数
			keyPairGen.initialize(keySize);
			// 密钥对
			return new KeyPair(keyPairGen.generateKeyPair());
		} catch (NoSuchAlgorithmException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 生成RSA私钥
	 *
	 * @param modulus  N特征值
	 * @param exponent d特征值
	 * @return {@link PrivateKey}
	 */
	public static PrivateKey generatePrivateKey(String modulus, String exponent) {
		return generatePrivateKey(new BigInteger(modulus), new BigInteger(exponent));
	}

	/**
	 * 生成RSA私钥
	 *
	 * @param modulus  N特征值
	 * @param exponent d特征值
	 * @return {@link PrivateKey}
	 */
	public static PrivateKey generatePrivateKey(BigInteger modulus, BigInteger exponent) {
		RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(modulus, exponent);
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
			return keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 生成RSA公钥
	 *
	 * @param modulus  N特征值
	 * @param exponent e特征值
	 * @return {@link PublicKey}
	 */
	public static PublicKey generatePublicKey(String modulus, String exponent) {
		return generatePublicKey(new BigInteger(modulus), new BigInteger(exponent));
	}

	/**
	 * 生成RSA公钥
	 *
	 * @param modulus  N特征值
	 * @param exponent e特征值
	 * @return {@link PublicKey}
	 */
	public static PublicKey generatePublicKey(BigInteger modulus, BigInteger exponent) {
		RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
			return keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 得到公钥
	 *
	 * @param base64PubKey 密钥字符串（经过base64编码）
	 * @return PublicKey
	 */
	public static PublicKey getPublicKey(String base64PubKey) {
		Objects.requireNonNull(base64PubKey, "base64 public key is null.");
		byte[] keyBytes = Base64Util.decodeFromString(base64PubKey);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
			return keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 得到公钥字符串
	 *
	 * @param base64PubKey 密钥字符串（经过base64编码）
	 * @return PublicKey String
	 */
	public static String getPublicKeyToBase64(String base64PubKey) {
		PublicKey publicKey = getPublicKey(base64PubKey);
		return getKeyString(publicKey);
	}

	/**
	 * 得到私钥
	 *
	 * @param base64PriKey 密钥字符串（经过base64编码）
	 * @return PrivateKey
	 */
	public static PrivateKey getPrivateKey(String base64PriKey) {
		Objects.requireNonNull(base64PriKey, "base64 private key is null.");
		byte[] keyBytes = Base64Util.decodeFromString(base64PriKey);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
			return keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 得到密钥字符串（经过base64编码）
	 *
	 * @param key key
	 * @return base 64 编码后的 key
	 */
	public static String getKeyString(Key key) {
		return Base64Util.encodeToString(key.getEncoded());
	}

	/**
	 * 得到私钥 base64
	 *
	 * @param base64PriKey 密钥字符串（经过base64编码）
	 * @return PrivateKey String
	 */
	public static String getPrivateKeyToBase64(String base64PriKey) {
		PrivateKey privateKey = getPrivateKey(base64PriKey);
		return getKeyString(privateKey);
	}

	/**
	 * 公钥加密
	 *
	 * @param base64PublicKey base64 的公钥
	 * @param data            待加密的内容
	 * @return 加密后的内容
	 */
	public static byte[] encrypt(String base64PublicKey, byte[] data) {
		return encrypt(getPublicKey(base64PublicKey), data);
	}

	/**
	 * 公钥加密
	 *
	 * @param publicKey 公钥
	 * @param data      待加密的内容
	 * @return 加密后的内容
	 */
	public static byte[] encrypt(PublicKey publicKey, byte[] data) {
		return encryptRsa(publicKey, data);
	}

	/**
	 * 私钥加密，用于 qpp 内，公钥解密
	 *
	 * @param base64PrivateKey base64 的私钥
	 * @param data             待加密的内容
	 * @return 加密后的内容
	 */
	public static byte[] encryptByPrivateKey(String base64PrivateKey, byte[] data) {
		return encryptByPrivateKey(getPrivateKey(base64PrivateKey), data);
	}

	/**
	 * 私钥加密，加密成 base64 字符串，用于 qpp 内，公钥解密
	 *
	 * @param base64PrivateKey base64 的私钥
	 * @param data             待加密的内容
	 * @return 加密后的内容
	 */
	public static String encryptByPrivateKeyToBase64(String base64PrivateKey, byte[] data) {
		return Base64Util.encodeToString(encryptByPrivateKey(base64PrivateKey, data));
	}

	/**
	 * 私钥加密，用于 qpp 内，公钥解密
	 *
	 * @param privateKey 私钥
	 * @param data       待加密的内容
	 * @return 加密后的内容
	 */
	public static byte[] encryptByPrivateKey(PrivateKey privateKey, byte[] data) {
		return encryptRsa(privateKey, data);
	}

	/**
	 * 公钥加密
	 *
	 * @param publicKey PublicKey
	 * @param data      待加密的内容
	 * @return 加密后的内容
	 */
	@Nullable
	public static String encryptToBase64(PublicKey publicKey, @Nullable String data) {
		if (StringUtil.isBlank(data)) {
			return null;
		}
		return Base64Util.encodeToString(encrypt(publicKey, data.getBytes(Charsets.UTF_8)));
	}

	/**
	 * 公钥加密
	 *
	 * @param base64PublicKey base64 公钥
	 * @param data            待加密的内容
	 * @return 加密后的内容
	 */
	@Nullable
	public static String encryptToBase64(String base64PublicKey, @Nullable String data) {
		return encryptToBase64(getPublicKey(base64PublicKey), data);
	}

	/**
	 * 解密
	 *
	 * @param base64PrivateKey base64 私钥
	 * @param data             数据
	 * @return 解密后的数据
	 */
	public static byte[] decrypt(String base64PrivateKey, byte[] data) {
		return decrypt(getPrivateKey(base64PrivateKey), data);
	}

	/**
	 * 解密
	 *
	 * @param base64publicKey base64 公钥
	 * @param data            数据
	 * @return 解密后的数据
	 */
	public static byte[] decryptByPublicKey(String base64publicKey, byte[] data) {
		return decryptByPublicKey(getPublicKey(base64publicKey), data);
	}

	/**
	 * 解密
	 *
	 * @param privateKey privateKey
	 * @param data       数据
	 * @return 解密后的数据
	 */
	public static byte[] decrypt(PrivateKey privateKey, byte[] data) {
		return decryptRsa(privateKey, data);
	}

	/**
	 * 解密
	 *
	 * @param publicKey PublicKey
	 * @param data      数据
	 * @return 解密后的数据
	 */
	public static byte[] decryptByPublicKey(PublicKey publicKey, byte[] data) {
		return decryptRsa(publicKey, data);
	}

	/**
	 * 公钥加密
	 *
	 * @param key  Key
	 * @param data 待加密的内容
	 * @return 加密后的内容
	 */
	public static byte[] encryptRsa(Key key, byte[] data) {
		int blockSize = ((RSAKey) key).getModulus().bitLength() / 8 - 11;
		return rsa(key, data, blockSize, Cipher.ENCRYPT_MODE);
	}

	/**
	 * 解密
	 *
	 * @param key  Key
	 * @param data 数据
	 * @return 解密后的数据
	 */
	public static byte[] decryptRsa(Key key, byte[] data) {
		int blockSize = ((RSAKey) key).getModulus().bitLength() / 8;
		return rsa(key, data, blockSize, Cipher.DECRYPT_MODE);
	}

	/**
	 * rsa 加、解密
	 *
	 * @param key       key
	 * @param data      数据
	 * @param blockSize blockSize
	 * @param mode      模式
	 * @return 解密后的数据
	 */
	private static byte[] rsa(Key key, byte[] data, int maxBlockSize, int mode) {
		// 数据长度
		final int dataLength = data.length;
		try {
			Cipher cipher = Cipher.getInstance(RSA_PADDING);
			cipher.init(mode, key);
			// 不需要分段
			if (dataLength <= maxBlockSize) {
				return cipher.doFinal(data, 0, dataLength);
			}
			final FastByteArrayOutputStream out = new FastByteArrayOutputStream();
			int offSet = 0;
			// 剩余长度
			int remainLength = dataLength;
			int blockSize;
			// 对数据分段处理
			while (remainLength > 0) {
				blockSize = Math.min(remainLength, maxBlockSize);
				out.write(cipher.doFinal(data, offSet, blockSize));
				offSet += blockSize;
				remainLength = dataLength - offSet;
			}
			return out.toByteArray();
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * base64 数据解密
	 *
	 * @param publicKey  PublicKey
	 * @param base64Data base64数据
	 * @return 解密后的数据
	 */
	public static byte[] decryptByPublicKeyFromBase64(PublicKey publicKey, byte[] base64Data) {
		return decryptByPublicKey(publicKey, Base64Util.decode(base64Data));
	}

	/**
	 * base64 数据解密
	 *
	 * @param base64PublicKey base64 公钥
	 * @param base64Data      base64数据
	 * @return 解密后的数据
	 */
	public static byte[] decryptByPublicKeyFromBase64(String base64PublicKey, byte[] base64Data) {
		return decryptByPublicKeyFromBase64(getPublicKey(base64PublicKey), base64Data);
	}

	/**
	 * base64 数据解密
	 *
	 * @param privateKey PrivateKey
	 * @param base64Data base64数据
	 * @return 解密后的数据
	 */
	@Nullable
	public static String decryptFromBase64(PrivateKey privateKey, @Nullable String base64Data) {
		if (StringUtil.isBlank(base64Data)) {
			return null;
		}
		return new String(decrypt(privateKey, Base64Util.decodeFromString(base64Data)), Charsets.UTF_8);
	}

	/**
	 * base64 数据解密
	 *
	 * @param base64PrivateKey base64 私钥
	 * @param base64Data       base64数据
	 * @return 解密后的数据
	 */
	@Nullable
	public static String decryptFromBase64(String base64PrivateKey, @Nullable String base64Data) {
		return decryptFromBase64(getPrivateKey(base64PrivateKey), base64Data);
	}

	/**
	 * base64 数据解密
	 *
	 * @param base64PrivateKey base64 私钥
	 * @param base64Data       base64数据
	 * @return 解密后的数据
	 */
	public static byte[] decryptFromBase64(String base64PrivateKey, byte[] base64Data) {
		return decrypt(base64PrivateKey, Base64Util.decode(base64Data));
	}

	/**
	 * base64 数据解密
	 *
	 * @param publicKey  PublicKey
	 * @param base64Data base64数据
	 * @return 解密后的数据
	 */
	@Nullable
	public static String decryptByPublicKeyFromBase64(PublicKey publicKey, @Nullable String base64Data) {
		if (StringUtil.isBlank(base64Data)) {
			return null;
		}
		return new String(decryptByPublicKey(publicKey, Base64Util.decodeFromString(base64Data)), Charsets.UTF_8);
	}

	/**
	 * base64 数据解密
	 *
	 * @param base64PublicKey base64 公钥
	 * @param base64Data      base64数据
	 * @return 解密后的数据
	 */
	@Nullable
	public static String decryptByPublicKeyFromBase64(String base64PublicKey, @Nullable String base64Data) {
		return decryptByPublicKeyFromBase64(getPublicKey(base64PublicKey), base64Data);
	}

}
