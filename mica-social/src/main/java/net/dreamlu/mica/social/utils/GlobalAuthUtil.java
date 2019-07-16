package net.dreamlu.mica.social.utils;

import lombok.experimental.UtilityClass;
import net.dreamlu.mica.core.utils.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局的工具类
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 */
@UtilityClass
public class GlobalAuthUtil {

	public static String generateDingTalkSignature(String timestamp, String secret) {
		byte[] hmacSha256Bytes = DigestUtil.hmacSha256(timestamp, secret);
		return Base64Util.encodeToString(hmacSha256Bytes);
	}

	public static Map<String, String> parseStringToMap(String paramsStr) {
		int pathEndPos = paramsStr.indexOf(CharPool.QUESTION_MARK);
		if (pathEndPos > -1) {
			paramsStr = paramsStr.split("\\?")[1];
		}
		Map<String, String> res = new HashMap<>();
		if (paramsStr.contains(StringPool.AMPERSAND)) {
			String[] fields = paramsStr.split(StringPool.AMPERSAND);
			for (String field : fields) {
				if (field.contains(StringPool.EQUALS)) {
					String[] keyValue = field.split(StringPool.EQUALS);
					res.put(UrlUtil.decode(keyValue[0]), keyValue.length == 2 ? UrlUtil.decode(keyValue[1]) : null);
				}
			}
		}
		return res;
	}

}
