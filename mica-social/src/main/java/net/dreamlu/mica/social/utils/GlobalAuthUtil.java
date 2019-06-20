package net.dreamlu.mica.social.utils;

import lombok.experimental.UtilityClass;
import net.dreamlu.mica.core.utils.Base64Util;
import net.dreamlu.mica.core.utils.DigestUtil;
import net.dreamlu.mica.core.utils.UrlUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局的工具类
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 */
@UtilityClass
public class GlobalAuthUtil {
	private static final String DEFAULT_ENCODING = "UTF-8";

	public static String generateDingTalkSignature(String canonicalString, String secret) {
		String hmacSha256Hex = DigestUtil.hmacSha256Hex(canonicalString, secret);
		return UrlUtil.encode(Base64Util.encode(hmacSha256Hex));
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
					res.put(UrlUtil.decode(keyValue[0]), keyValue.length == 2 ? UrlUtil.decode(keyValue[1]) : null);
				}
			}
		}
		return res;
	}

}
