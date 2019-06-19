package net.dreamlu.mica.social.request;

import com.fasterxml.jackson.databind.JsonNode;
import net.dreamlu.http.HttpRequest;
import net.dreamlu.mica.social.config.AuthConfig;
import net.dreamlu.mica.social.config.AuthSource;
import net.dreamlu.mica.social.exception.AuthException;
import net.dreamlu.mica.social.model.AuthDingTalkErrorCode;
import net.dreamlu.mica.social.model.AuthToken;
import net.dreamlu.mica.social.model.AuthUser;
import net.dreamlu.mica.social.utils.GlobalAuthUtil;
import net.dreamlu.mica.social.utils.UrlBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * 钉钉登录
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com), L.cm
 * @version 1.0
 * @since 1.8
 */
public class AuthDingTalkRequest extends BaseAuthRequest {

	public AuthDingTalkRequest(AuthConfig config) {
		super(config, AuthSource.DINGTALK);
	}

	@Override
	public String authorize() {
		return UrlBuilder.getDingTalkQrConnectUrl(config.getClientId(), config.getRedirectUri());
	}

	@Override
	protected AuthToken getAccessToken(String code) {
		return AuthToken.builder()
			.accessCode(code)
			.build();
	}

	@Override
	protected AuthUser getUserInfo(AuthToken authToken) {
		String code = authToken.getAccessCode();
		// 根据timestamp, appSecret计算签名值
		String stringToSign = System.currentTimeMillis() + "";
		String urlEncodeSignature = GlobalAuthUtil.generateDingTalkSignature(config.getClientSecret(), stringToSign);
		String dingTalkUserInfoUrl = UrlBuilder.getDingTalkUserInfoUrl(urlEncodeSignature, stringToSign, config.getClientId());

		Map<String, Object> bodyJson = new HashMap<>(1);
		bodyJson.put("tmp_auth_code", code);
		JsonNode object = HttpRequest.post(dingTalkUserInfoUrl)
			.bodyJson(bodyJson)
			.execute()
			.asJsonNode();
		AuthDingTalkErrorCode errorCode = AuthDingTalkErrorCode.getErrorCode(object.get("errcode").asInt());
		if (!AuthDingTalkErrorCode.EC0.equals(errorCode)) {
			throw new AuthException(errorCode.getDesc());
		}
		JsonNode userInfo = object.get("user_info");
		return AuthUser.builder()
			.uuid(userInfo.get("openid").asText())
			.nickname(userInfo.get("nick").asText())
			.username(userInfo.get("nick").asText())
			.source(AuthSource.DINGTALK)
			.build();
	}
}
