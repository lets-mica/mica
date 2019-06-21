package net.dreamlu.mica.social.request;

import com.fasterxml.jackson.databind.JsonNode;
import net.dreamlu.http.HttpRequest;
import net.dreamlu.http.HttpResponse;
import net.dreamlu.mica.social.config.AuthConfig;
import net.dreamlu.mica.social.config.AuthSource;
import net.dreamlu.mica.social.exception.AuthException;
import net.dreamlu.mica.social.model.AuthDingTalkErrorCode;
import net.dreamlu.mica.social.model.AuthToken;
import net.dreamlu.mica.social.model.AuthUser;
import net.dreamlu.mica.social.model.AuthUserGender;
import net.dreamlu.mica.social.utils.AuthConfigChecker;
import net.dreamlu.mica.social.utils.GlobalAuthUtil;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * 钉钉登录
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com), L.cm
 */
public class AuthDingTalkRequest extends BaseAuthRequest {

	public AuthDingTalkRequest(AuthConfig config) {
		super(config, AuthSource.DINGTALK);
	}

	@Override
	public String authorize(String state) {
		return UriComponentsBuilder.fromUriString(authSource.authorize())
			.queryParam("response_type", "code")
			.queryParam("appid", config.getClientId())
			.queryParam("redirect_uri", config.getRedirectUri())
			.queryParam("state", state)
			.queryParam("scope", "snsapi_login")
			.build()
			.toUriString();
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
		String timestamp = String.valueOf(System.currentTimeMillis());
		String urlEncodeSignature = GlobalAuthUtil.generateDingTalkSignature(timestamp, config.getClientSecret());
		Map<String, Object> bodyJson = new HashMap<>(1);
		bodyJson.put("tmp_auth_code", code);
		JsonNode object = HttpRequest.post(authSource.userInfo())
			.log()
			.query("signature", urlEncodeSignature)
			.query("timestamp", timestamp)
			.query("accessKey", config.getClientId())
			.bodyJson(bodyJson)
			.execute()
			.asJsonNode();
		AuthDingTalkErrorCode errorCode = AuthDingTalkErrorCode.getErrorCode(object.get("errcode").asInt());
		if (AuthDingTalkErrorCode.EC0 != errorCode) {
			throw new AuthException(errorCode.getDesc());
		}
		JsonNode userInfo = object.get("user_info");
		AuthToken token = AuthToken.builder()
			.openId(userInfo.get("openid").asText())
			.unionId(userInfo.get("unionid").asText())
			.build();
		return AuthUser.builder()
			.uuid(userInfo.get("unionid").asText())
			.nickname(userInfo.get("nick").asText())
			.username(userInfo.get("nick").asText())
			.gender(AuthUserGender.UNKNOW)
			.source(authSource)
			.token(token)
			.build();
	}
}
