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
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.util.Objects;

/**
 * DES加、解密处理工具
 *
 * @author L.cm
 */
@UtilityClass
public class DesUtil {
	/**
	 * 数字签名，密钥算法
	 */
	public static final String DES_ALGORITHM = "DES";

	/**
	 * 生成 des 密钥
	 *
	 * @return 密钥
	 */
	public static String genDesKey() {
		return StringUtil.random(16);
	}

	/**
	 * DES加密
	 *
	 * @param data     byte array
	 * @param password 密钥
	 * @return des hex
	 */
	public static String encryptToHex(byte[] data, String password) {
		return HexUtil.encodeToString(encrypt(data, password));
	}

	/**
	 * DES加密
	 *
	 * @param data     字符串内容
	 * @param password 密钥
	 * @return des hex
	 */
	@Nullable
	public static String encryptToHex(@Nullable String data, String password) {
		if (StringUtil.isBlank(data)) {
			return null;
		}
		byte[] dataBytes = data.getBytes(Charsets.UTF_8);
		return encryptToHex(dataBytes, password);
	}

	/**
	 * DES解密
	 *
	 * @param data     字符串内容
	 * @param password 密钥
	 * @return des context
	 */
	@Nullable
	public static String decryptFormHex(@Nullable String data, String password) {
		if (StringUtil.isBlank(data)) {
			return null;
		}
		byte[] hexBytes = HexUtil.decode(data);
		return new String(decrypt(hexBytes, password), Charsets.UTF_8);
	}

	/**
	 * DES加密
	 *
	 * @param data     byte array
	 * @param password 密钥
	 * @return des hex
	 */
	public static String encryptToBase64(byte[] data, String password) {
		return Base64Utils.encodeToString(encrypt(data, password));
	}

	/**
	 * DES加密
	 *
	 * @param data     字符串内容
	 * @param password 密钥
	 * @return des hex
	 */
	@Nullable
	public static String encryptToBase64(@Nullable String data, String password) {
		if (StringUtil.isBlank(data)) {
			return null;
		}
		byte[] dataBytes = data.getBytes(Charsets.UTF_8);
		return encryptToBase64(dataBytes, password);
	}

	/**
	 * DES解密
	 *
	 * @param data     字符串内容
	 * @param password 密钥
	 * @return des context
	 */
	public static byte[] decryptFormBase64(byte[] data, String password) {
		byte[] dataBytes = Base64Utils.decode(data);
		return decrypt(dataBytes, password);
	}

	/**
	 * DES解密
	 *
	 * @param data     字符串内容
	 * @param password 密钥
	 * @return des context
	 */
	@Nullable
	public static String decryptFormBase64(@Nullable String data, String password) {
		if (StringUtil.isBlank(data)) {
			return null;
		}
		byte[] dataBytes = Base64Utils.decodeFromString(data);
		return new String(decrypt(dataBytes, password), Charsets.UTF_8);
	}

	/**
	 * DES加密
	 *
	 * @param data   内容
	 * @param desKey 密钥
	 * @return byte array
	 */
	public static byte[] encrypt(byte[] data, byte[] desKey) {
		return des(data, desKey, Cipher.ENCRYPT_MODE);
	}

	/**
	 * DES加密
	 *
	 * @param data   内容
	 * @param desKey 密钥
	 * @return byte array
	 */
	public static byte[] encrypt(byte[] data, String desKey) {
		return encrypt(data, Objects.requireNonNull(desKey).getBytes(Charsets.UTF_8));
	}

	/**
	 * DES解密
	 *
	 * @param data   内容
	 * @param desKey 密钥
	 * @return byte array
	 */
	public static byte[] decrypt(byte[] data, byte[] desKey) {
		return des(data, desKey, Cipher.DECRYPT_MODE);
	}

	/**
	 * DES解密
	 *
	 * @param data   内容
	 * @param desKey 密钥
	 * @return byte array
	 */
	public static byte[] decrypt(byte[] data, String desKey) {
		return decrypt(data, Objects.requireNonNull(desKey).getBytes(Charsets.UTF_8));
	}

	/**
	 * DES加密/解密公共方法
	 *
	 * @param data   byte数组
	 * @param desKey 密钥
	 * @param mode   加密：{@link Cipher#ENCRYPT_MODE}，解密：{@link Cipher#DECRYPT_MODE}
	 * @return des
	 */
	private static byte[] des(byte[] data, byte[] desKey, int mode) {
		try {
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES_ALGORITHM);
			Cipher cipher = Cipher.getInstance(DES_ALGORITHM);
			DESKeySpec desKeySpec = new DESKeySpec(desKey);
			cipher.init(mode, keyFactory.generateSecret(desKeySpec), Holder.SECURE_RANDOM);
			return cipher.doFinal(data);
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}
	}

}
