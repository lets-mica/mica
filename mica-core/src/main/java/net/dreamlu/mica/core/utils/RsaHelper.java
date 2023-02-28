/*
 * MIT License
 *
 * Copyright (c) 2020 高坚果
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.dreamlu.mica.core.utils;

import org.springframework.lang.Nullable;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * RSA PEM格式秘钥对的解析和导出
 * <p>
 * caiqy: 一般来说:网上的公钥都是pkcs8的,虽然选了pkcs1加密,但是还是给的pkcs8的公钥,所以不要再对公钥转了,转私钥就行
 *
 * <p>
 * GitHub:https://github.com/xiangyuecn/RSA-java
 * <p>
 * https://github.com/xiangyuecn/RSA-java/blob/master/RSA_PEM.java
 * 移植自：https://github.com/xiangyuecn/RSA-csharp/blob/master/RSA_PEM.cs
 *
 * @author xiangyuecn
 * @author caiqyxyx
 * @author L.cm
 */
public class RsaHelper {
	private static final Pattern XML_EXP = Pattern.compile("\\s*<RSAKeyValue>([<>\\/\\+=\\w\\s]+)</RSAKeyValue>\\s*");
	private static final Pattern XML_TAG_EXP = Pattern.compile("<(.+?)>\\s*([^<]+?)\\s*</");
	private static final Pattern PEM_CODE = Pattern.compile("--+.+?--+|[\\s\\r\\n]+");
	private static final byte[] SEQ_OID = new byte[]{0x30, 0x0D, 0x06, 0x09, 0x2A, (byte) 0x86, 0x48, (byte) 0x86, (byte) 0xF7, 0x0D, 0x01, 0x01, 0x01, 0x05, 0x00};
	private static final byte[] VERSION = new byte[]{0x02, 0x01, 0x00};

	/**
	 * modulus 模数，公钥、私钥都有
	 **/
	private byte[] keyModulus;
	/**
	 * privateExponent 公钥指数，公钥、私钥都有
	 **/
	private byte[] keyExponent;
	/**
	 * privateExponent 私钥指数，只有私钥的时候才有
	 **/
	@Nullable
	private byte[] keyD;

	//以下参数只有私钥才有 https://docs.microsoft.com/zh-cn/dotnet/api/system.security.cryptography.rsaparameters?redirectedfrom=MSDN&view=netframework-4.8
	/**
	 * prime1
	 **/
	private byte[] valP;
	/**
	 * prime2
	 **/
	private byte[] valQ;
	/**
	 * exponent1
	 **/
	private byte[] valDP;
	/**
	 * exponent2
	 **/
	private byte[] valDQ;
	/**
	 * coefficient
	 **/
	private byte[] valInverseQ;

	private RsaHelper() {
	}

	/***
	 * 通过公钥和私钥构造一个PEM
	 * @param publicKey 必须提供公钥
	 * @param privateKeyOrNull 私钥可以不提供，导出的PEM就只包含公钥
	 **/
	public RsaHelper(RSAPublicKey publicKey, @Nullable RSAPrivateKey privateKeyOrNull) {
		this(
			bigB(publicKey.getModulus())
			, bigB(publicKey.getPublicExponent())
			, privateKeyOrNull == null ? null : bigB(privateKeyOrNull.getPrivateExponent())
		);
	}

	/***
	 * 通过全量的PEM字段数据构造一个PEM，除了模数modulus和公钥指数exponent必须提供外，其他私钥指数信息要么全部提供，要么全部不提供（导出的PEM就只包含公钥）
	 * 注意：所有参数首字节如果是0，必须先去掉
	 */
	public RsaHelper(byte[] modulus, byte[] exponent, byte[] d, byte[] p, byte[] q, byte[] dp, byte[] dq, byte[] inverseQ) {
		keyModulus = modulus;
		keyExponent = exponent;
		keyD = d;

		valP = p;
		valQ = q;
		valDP = dp;
		valDQ = dq;
		valInverseQ = inverseQ;
	}

	/***
	 * 通过公钥指数和私钥指数构造一个PEM，会反推计算出P、Q但和原始生成密钥的P、Q极小可能相同
	 * 注意：所有参数首字节如果是0，必须先去掉
	 * @param modulus 必须提供模数
	 * @param exponent 必须提供公钥指数
	 * @param dOrNull 私钥指数可以不提供，导出的PEM就只包含公钥
	 **/
	public RsaHelper(byte[] modulus, byte[] exponent, @Nullable byte[] dOrNull) {
		//modulus
		keyModulus = modulus;
		//publicExponent
		keyExponent = exponent;

		if (dOrNull != null) {
			//privateExponent
			keyD = dOrNull;

			//反推P、Q
			BigInteger n = bigX(modulus);
			BigInteger e = bigX(exponent);
			BigInteger d = bigX(dOrNull);
			BigInteger p = findFactor(e, d, n);
			BigInteger q = n.divide(p);
			if (p.compareTo(q) > 0) {
				BigInteger t = p;
				p = q;
				q = t;
			}
			BigInteger exp1 = d.mod(p.subtract(BigInteger.ONE));
			BigInteger exp2 = d.mod(q.subtract(BigInteger.ONE));
			BigInteger coeff = q.modInverse(p);

			//prime1
			valP = bigB(p);
			//prime2
			valQ = bigB(q);
			//exponent1
			valDP = bigB(exp1);
			//exponent2
			valDQ = bigB(exp2);
			//coefficient
			valInverseQ = bigB(coeff);
		}
	}

	/**
	 * 秘钥位数
	 **/
	public int keySize() {
		return keyModulus.length * 8;
	}

	/**
	 * 是否包含私钥
	 **/
	public boolean hasPrivate() {
		return keyD != null;
	}

	/**
	 * 得到公钥Java对象
	 **/
	public RSAPublicKey getRSAPublicKey() throws Exception {
		RSAPublicKeySpec spec = new RSAPublicKeySpec(bigX(keyModulus), bigX(keyExponent));
		KeyFactory factory = KeyFactory.getInstance("RSA");
		return (RSAPublicKey) factory.generatePublic(spec);
	}

	/**
	 * 得到私钥Java对象
	 **/
	public RSAPrivateKey getRSAPrivateKey() throws Exception {
		if (keyD == null) {
			throw new IllegalArgumentException("当前为公钥，无法获得私钥");
		}
		RSAPrivateKeySpec spec = new RSAPrivateKeySpec(bigX(keyModulus), bigX(keyD));
		KeyFactory factory = KeyFactory.getInstance("RSA");
		return (RSAPrivateKey) factory.generatePrivate(spec);
	}

	/**
	 * 转成正整数，如果是负数，需要加前导0转成正整数
	 **/
	public static BigInteger bigX(byte[] bigb) {
		if (bigb[0] < 0) {
			byte[] c = new byte[bigb.length + 1];
			System.arraycopy(bigb, 0, c, 1, bigb.length);
			bigb = c;
		}
		return new BigInteger(bigb);
	}

	/**
	 * BigInt导出byte整数首字节>0x7F的会加0前导，保证正整数，因此需要去掉0
	 **/
	public static byte[] bigB(BigInteger bigx) {
		byte[] val = bigx.toByteArray();
		if (val[0] == 0) {
			byte[] c = new byte[val.length - 1];
			System.arraycopy(val, 1, c, 0, c.length);
			val = c;
		}
		return val;
	}

	/**
	 * 由n e d 反推 P Q
	 * 资料： https://stackoverflow.com/questions/43136036/how-to-get-a-rsaprivatecrtkey-from-a-rsaprivatekey
	 * https://v2ex.com/t/661736
	 ***/
	private static BigInteger findFactor(BigInteger e, BigInteger d, BigInteger n) {
		BigInteger edMinus1 = e.multiply(d).subtract(BigInteger.ONE);
		int s = edMinus1.getLowestSetBit();
		BigInteger t = edMinus1.shiftRight(s);

		long now = System.currentTimeMillis();
		for (int aInt = 2; true; aInt++) {
			if (aInt % 10 == 0 && System.currentTimeMillis() - now > 3000) {
				//测试最多循环2次，1024位的速度很快 8ms
				throw new IllegalArgumentException("推算RSA.P超时");
			}

			BigInteger aPow = BigInteger.valueOf(aInt).modPow(t, n);
			for (int i = 1; i <= s; i++) {
				if (aPow.equals(BigInteger.ONE)) {
					break;
				}
				if (aPow.equals(n.subtract(BigInteger.ONE))) {
					break;
				}
				BigInteger aPowSquared = aPow.multiply(aPow).mod(n);
				if (aPowSquared.equals(BigInteger.ONE)) {
					return aPow.subtract(BigInteger.ONE).gcd(n);
				}
				aPow = aPowSquared;
			}
		}
	}

	/**
	 * 用PEM格式密钥对创建RSA，支持PKCS#1、PKCS#8格式的PEM
	 */
	public static RsaHelper fromPem(String pem) throws Exception {
		RsaHelper param = new RsaHelper();

		String base64 = PEM_CODE.matcher(pem).replaceAll("");
		//java byte是正负数
		byte[] dataX = Base64.getDecoder().decode(base64);
		if (dataX == null) {
			throw new IllegalArgumentException("PEM内容无效");
		}
		//转成正整数的bytes数组，不然byte是负数难搞
		short[] data = new short[dataX.length];
		for (int i = 0; i < dataX.length; i++) {
			data[i] = (short) (dataX[i] & 0xff);
		}
		int[] idx = new int[]{0};

		if (pem.contains("PUBLIC KEY")) {
			//读取数据总长度
			readLen(0x30, data, idx);

			//检测PKCS8
			int[] idx2 = new int[]{idx[0]};
			if (eq(SEQ_OID, data, idx)) {
				//读取1长度
				readLen(0x03, data, idx);
				//跳过0x00
				idx[0]++;
				//读取2长度
				readLen(0x30, data, idx);
			} else {
				idx = idx2;
			}

			//Modulus
			param.keyModulus = readBlock(data, idx);

			//Exponent
			param.keyExponent = readBlock(data, idx);
		} else if (pem.contains("PRIVATE KEY")) {
			//读取数据总长度
			readLen(0x30, data, idx);

			//读取版本号
			if (!eq(VERSION, data, idx)) {
				throw new IllegalArgumentException("PEM未知版本");
			}

			//检测PKCS8
			int[] idx2 = new int[]{idx[0]};
			if (eq(SEQ_OID, data, idx)) {
				//读取1长度
				readLen(0x04, data, idx);
				//读取2长度
				readLen(0x30, data, idx);

				//读取版本号
				if (!eq(VERSION, data, idx)) {
					throw new IllegalArgumentException("PEM版本无效");
				}
			} else {
				idx = idx2;
			}

			//读取数据
			param.keyModulus = readBlock(data, idx);
			param.keyExponent = readBlock(data, idx);
			param.keyD = readBlock(data, idx);
			param.valP = readBlock(data, idx);
			param.valQ = readBlock(data, idx);
			param.valDP = readBlock(data, idx);
			param.valDQ = readBlock(data, idx);
			param.valInverseQ = readBlock(data, idx);
		} else {
			throw new IllegalArgumentException("pem需要BEGIN END标头");
		}
		return param;
	}

	/**
	 * 从数组start开始到指定长度复制一份
	 **/
	private static byte[] sub(short[] arr, int start, int count) {
		byte[] val = new byte[count];
		for (int i = 0; i < count; i++) {
			val[i] = (byte) arr[start + i];
		}
		return val;
	}

	/**
	 * 读取长度
	 **/
	private static int readLen(int first, short[] data, int[] idxO) throws Exception {
		int idx = idxO[0];
		try {
			if (data[idx] == first) {
				idx++;
				if (data[idx] == 0x81) {
					idx++;
					return data[idx++];
				} else if (data[idx] == 0x82) {
					idx++;
					return (((int) data[idx++]) << 8) + data[idx++];
				} else if (data[idx] < 0x80) {
					return data[idx++];
				}
			}
			throw new IllegalArgumentException("PEM未能提取到数据");
		} finally {
			idxO[0] = idx;
		}
	}

	/**
	 * 读取块数据
	 **/
	private static byte[] readBlock(short[] data, int[] idxO) throws Exception {
		int idx = idxO[0];
		try {
			int len = readLen(0x02, data, idxO);
			idx = idxO[0];
			if (data[idx] == 0x00) {
				idx++;
				len--;
			}
			byte[] val = sub(data, idx, len);
			idx += len;
			return val;
		} finally {
			idxO[0] = idx;
		}
	}

	/**
	 * 比较data从idx位置开始是否是bytes内容
	 **/
	private static boolean eq(byte[] bytes, short[] data, int[] idxO) {
		int idx = idxO[0];
		try {
			for (int i = 0; i < bytes.length; i++, idx++) {
				if (idx >= data.length) {
					return false;
				}
				if ((bytes[i] & 0xff) != data[idx]) {
					return false;
				}
			}
			return true;
		} finally {
			idxO[0] = idx;
		}
	}


	/***
	 * 将RSA中的密钥对转换成PEM PKCS#8格式
	 * 。convertToPublic：等于true时含私钥的RSA将只返回公钥，仅含公钥的RSA不受影响
	 * 。公钥如：-----BEGIN RSA PUBLIC KEY-----，私钥如：-----BEGIN RSA PRIVATE KEY-----
	 * 。似乎导出PKCS#1公钥用的比较少，PKCS#8的公钥用的多些，私钥#1#8都差不多
	 */
	public String toPemPKCS1(boolean convertToPublic) throws Exception {
		return toPem(convertToPublic, false, false);
	}

	/***
	 * 将RSA中的密钥对转换成PEM PKCS#8格式
	 * 。convertToPublic：等于true时含私钥的RSA将只返回公钥，仅含公钥的RSA不受影响
	 * 。公钥如：-----BEGIN PUBLIC KEY-----，私钥如：-----BEGIN PRIVATE KEY-----
	 */
	public String toPemPKCS8(boolean convertToPublic) throws Exception {
		return toPem(convertToPublic, true, true);
	}

	/***
	 * 将RSA中的密钥对转换成PEM格式
	 * 。convertToPublic：等于true时含私钥的RSA将只返回公钥，仅含公钥的RSA不受影响
	 * 。privateUsePKCS8：私钥的返回格式，等于true时返回PKCS#8格式（-----BEGIN PRIVATE KEY-----），否则返回PKCS#1格式（-----BEGIN RSA PRIVATE KEY-----），返回公钥时此参数无效；两种格式使用都比较常见
	 * 。publicUsePKCS8：公钥的返回格式，等于true时返回PKCS#8格式（-----BEGIN PUBLIC KEY-----），否则返回PKCS#1格式（-----BEGIN RSA PUBLIC KEY-----），返回私钥时此参数无效；一般用的多的是true PKCS#8格式公钥，PKCS#1格式公钥似乎比较少见
	 */
	public String toPem(boolean convertToPublic, boolean privateUsePKCS8, boolean publicUsePKCS8) throws Exception {
		//https://www.jianshu.com/p/25803dd9527d
		//https://www.cnblogs.com/ylz8401/p/8443819.html
		//https://blog.csdn.net/jiayanhui2877/article/details/47187077
		//https://blog.csdn.net/xuanshao_/article/details/51679824
		//https://blog.csdn.net/xuanshao_/article/details/51672547
		ByteArrayOutputStream ms = new ByteArrayOutputStream();

		if (this.keyD == null || convertToPublic) {
			/****生成公钥****/

			//写入总字节数，不含本段长度，额外需要24字节的头，后续计算好填入
			ms.write(0x30);
			int index1 = ms.size();

			//PKCS8 多一段数据
			int index2 = -1, index3 = -1;
			if (publicUsePKCS8) {
				//固定内容
				// encoded OID sequence for PKCS #1 rsaEncryption szOID_RSA_RSA = "1.2.840.113549.1.1.1"
				ms.write(SEQ_OID);

				//从0x00开始的后续长度
				ms.write(0x03);
				index2 = ms.size();
				ms.write(0x00);

				//后续内容长度
				ms.write(0x30);
				index3 = ms.size();
			}

			//写入Modulus
			writeBlock(keyModulus, ms);

			//写入Exponent
			writeBlock(keyExponent, ms);


			//计算空缺的长度
			byte[] bytes = ms.toByteArray();

			if (index2 != -1) {
				bytes = writeLen(index3, bytes, ms);
				bytes = writeLen(index2, bytes, ms);
			}
			bytes = writeLen(index1, bytes, ms);


			String flag = " PUBLIC KEY";
			if (!publicUsePKCS8) {
				flag = " RSA" + flag;
			}
			return "-----BEGIN" + flag + "-----\n" + textBreak(Base64.getEncoder().encodeToString(bytes), 64) + "\n-----END" + flag + "-----";
		} else {

			//写入总字节数，后续写入
			ms.write(0x30);
			int index1 = ms.size();

			//写入版本号
			ms.write(VERSION);

			//PKCS8 多一段数据
			int index2 = -1, index3 = -1;
			if (privateUsePKCS8) {
				//固定内容
				ms.write(SEQ_OID);

				//后续内容长度
				ms.write(0x04);
				index2 = ms.size();

				//后续内容长度
				ms.write(0x30);
				index3 = ms.size();

				//写入版本号
				ms.write(VERSION);
			}

			//写入数据
			writeBlock(keyModulus, ms);
			writeBlock(keyExponent, ms);
			writeBlock(keyD, ms);
			writeBlock(valP, ms);
			writeBlock(valQ, ms);
			writeBlock(valDP, ms);
			writeBlock(valDQ, ms);
			writeBlock(valInverseQ, ms);


			//计算空缺的长度
			byte[] bytes = ms.toByteArray();

			if (index2 != -1) {
				bytes = writeLen(index3, bytes, ms);
				bytes = writeLen(index2, bytes, ms);
			}
			bytes = writeLen(index1, bytes, ms);


			String flag = " PRIVATE KEY";
			if (!privateUsePKCS8) {
				flag = " RSA" + flag;
			}
			return "-----BEGIN" + flag + "-----\n" + textBreak(Base64.getEncoder().encodeToString(bytes), 64) + "\n-----END" + flag + "-----";
		}
	}

	/**
	 * 写入一个长度字节码
	 **/
	private static void writeLenByte(int len, ByteArrayOutputStream ms) {
		if (len < 0x80) {
			ms.write((byte) len);
		} else if (len <= 0xff) {
			ms.write(0x81);
			ms.write((byte) len);
		} else {
			ms.write(0x82);
			ms.write((byte) (len >> 8 & 0xff));
			ms.write((byte) (len & 0xff));
		}
	}

	/**
	 * 写入一块数据
	 **/
	private static void writeBlock(byte[] bytes, ByteArrayOutputStream ms) throws Exception {
		boolean addZero = ((bytes[0] & 0xff) >> 4) >= 0x8;
		ms.write(0x02);
		int len = bytes.length + (addZero ? 1 : 0);
		writeLenByte(len, ms);

		if (addZero) {
			ms.write(0x00);
		}
		ms.write(bytes);
	}

	/**
	 * 根据后续内容长度写入长度数据
	 **/
	private static byte[] writeLen(int index, byte[] bytes, ByteArrayOutputStream ms) {
		int len = bytes.length - index;

		ms.reset();
		ms.write(bytes, 0, index);
		writeLenByte(len, ms);
		ms.write(bytes, index, len);

		return ms.toByteArray();
	}

	/**
	 * 把字符串按每行多少个字断行
	 *
	 * @param text text
	 * @param line line
	 * @return 字符串
	 **/
	private static String textBreak(String text, int line) {
		int idx = 0;
		int len = text.length();
		StringBuilder str = new StringBuilder();
		while (idx < len) {
			if (idx > 0) {
				str.append('\n');
			}
			if (idx + line >= len) {
				str.append(text.substring(idx));
			} else {
				str.append(text, idx, idx + line);
			}
			idx += line;
		}
		return str.toString();
	}

	/***
	 * 将XML格式密钥转成PEM，支持公钥xml、私钥xml
	 */
	public static RsaHelper fromXml(String xml) {
		RsaHelper rtv = new RsaHelper();
		Matcher xmlM = XML_EXP.matcher(xml);
		if (!xmlM.find()) {
			throw new IllegalArgumentException("XML内容不符合要求");
		}
		Matcher tagM = XML_TAG_EXP.matcher(xmlM.group(1));
		Base64.Decoder dec = Base64.getDecoder();
		while (tagM.find()) {
			String tag = tagM.group(1);
			String b64 = tagM.group(2);
			byte[] val = dec.decode(b64);
			switch (tag) {
				case "Modulus":
					rtv.keyModulus = val;
					break;
				case "Exponent":
					rtv.keyExponent = val;
					break;
				case "D":
					rtv.keyD = val;
					break;
				case "P":
					rtv.valP = val;
					break;
				case "Q":
					rtv.valQ = val;
					break;
				case "DP":
					rtv.valDP = val;
					break;
				case "DQ":
					rtv.valDQ = val;
					break;
				case "InverseQ":
					rtv.valInverseQ = val;
					break;
				default: break;
			}
		}
		if (rtv.keyModulus == null || rtv.keyExponent == null) {
			throw new IllegalArgumentException("XML公钥丢失");
		}
		if (rtv.keyD != null) {
			if (rtv.valP == null || rtv.valQ == null || rtv.valDP == null || rtv.valDQ == null || rtv.valInverseQ == null) {
				return new RsaHelper(rtv.keyModulus, rtv.keyExponent, rtv.keyD);
			}
		}
		return rtv;
	}

	/***
	 * 将RSA中的密钥对转换成XML格式
	 * ，如果convertToPublic含私钥的RSA将只返回公钥，仅含公钥的RSA不受影响
	 */
	public String toXml(boolean convertToPublic) {
		StringBuilder str = new StringBuilder();
		str.append("<RSAKeyValue>");
		str.append("<Modulus>").append(Base64Util.encodeToString(keyModulus)).append("</Modulus>");
		str.append("<Exponent>").append(Base64Util.encodeToString(keyExponent)).append("</Exponent>");
		if (this.keyD == null || convertToPublic) {
			//NOOP
		} else {
			str.append("<P>").append(Base64Util.encodeToString(valP)).append("</P>");
			str.append("<Q>").append(Base64Util.encodeToString(valQ)).append("</Q>");
			str.append("<DP>").append(Base64Util.encodeToString(valDP)).append("</DP>");
			str.append("<DQ>").append(Base64Util.encodeToString(valDQ)).append("</DQ>");
			str.append("<InverseQ>").append(Base64Util.encodeToString(valInverseQ)).append("</InverseQ>");
			str.append("<D>").append(Base64Util.encodeToString(keyD)).append("</D>");
		}
		str.append("</RSAKeyValue>");
		return str.toString();
	}
}
