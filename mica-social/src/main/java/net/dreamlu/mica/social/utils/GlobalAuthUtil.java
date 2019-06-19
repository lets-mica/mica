package net.dreamlu.mica.social.utils;

import lombok.experimental.UtilityClass;
import net.dreamlu.mica.core.utils.Base64Util;
import net.dreamlu.mica.core.utils.DigestUtil;
import net.dreamlu.mica.social.exception.AuthException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局的工具类
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @since 1.8
 */
@UtilityClass
public class GlobalAuthUtil {
	private static final String DEFAULT_ENCODING = "UTF-8";

	public static String generateDingTalkSignature(String canonicalString, String secret) {
		String hmacSha256Hex = DigestUtil.hmacSha256Hex(canonicalString, secret);
		return urlEncode(Base64Util.encode(hmacSha256Hex));
	}

	private static String urlEncode(String value) {
		if (value == null) {
			return "";
		}

		try {
			String encoded = URLEncoder.encode(value, GlobalAuthUtil.DEFAULT_ENCODING);
			return encoded.replace("+", "%20").replace("*", "%2A")
				.replace("~", "%7E").replace("/", "%2F");
		} catch (UnsupportedEncodingException e) {
			throw new AuthException("Failed To Encode Uri", e);
		}
	}

	public static String urlDecode(String value) {
		if (value == null) {
			return "";
		}
		try {
			return URLDecoder.decode(value, GlobalAuthUtil.DEFAULT_ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw new AuthException("Failed To Decode Uri", e);
		}
	}

	public static Map<String, String> parseStringToMap(String paramsStr) {
		int pathEndPos = paramsStr.indexOf('?');
		if (pathEndPos > -1) {
			paramsStr = paramsStr.split("\\?")[1];
		}
		Map<String, String> res = new HashMap<>();
		if (paramsStr.contains("&")) {
			String[] fields = paramsStr.split("&");
			for (String field : fields) {
				if (field.contains("=")) {
					String[] keyValue = field.split("=");
					res.put(GlobalAuthUtil.urlDecode(keyValue[0]), keyValue.length == 2 ? GlobalAuthUtil.urlDecode(keyValue[1]) : null);
				}
			}
		}
		return res;
	}

}
