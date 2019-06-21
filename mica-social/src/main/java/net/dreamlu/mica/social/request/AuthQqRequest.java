package net.dreamlu.mica.social.request;

import com.fasterxml.jackson.databind.JsonNode;
import net.dreamlu.http.HttpRequest;
import net.dreamlu.http.HttpResponse;
import net.dreamlu.mica.core.utils.JsonUtil;
import net.dreamlu.mica.core.utils.ObjectUtil;
import net.dreamlu.mica.core.utils.StringUtil;
import net.dreamlu.mica.social.config.AuthConfig;
import net.dreamlu.mica.social.config.AuthSource;
import net.dreamlu.mica.social.exception.AuthException;
import net.dreamlu.mica.social.model.AuthToken;
import net.dreamlu.mica.social.model.AuthUser;
import net.dreamlu.mica.social.model.AuthUserGender;
import net.dreamlu.mica.social.utils.GlobalAuthUtil;

import java.util.Map;
import java.util.Optional;

/**
 * qq登录
 *
 * @author L.cm
 */
public class AuthQqRequest extends BaseAuthRequest {
	public AuthQqRequest(AuthConfig config) {
		super(config, AuthSource.QQ);
	}

	@Override
	protected AuthToken getAccessToken(String code) {
		String response = doGetAuthorizationCode(code).asString();
		Map<String, String> accessTokenObject = GlobalAuthUtil.parseStringToMap(response);
		if (!accessTokenObject.containsKey("access_token")) {
			throw new AuthException("Unable to get token from qq using code [" + code + "]");
		}
		return AuthToken.builder()
			.accessToken(accessTokenObject.get("access_token"))
			.expireIn(ObjectUtil.toInt(accessTokenObject.get("expires_in")))
			.refreshToken(accessTokenObject.get("refresh_token"))
			.build();
	}

	@Override
	protected AuthUser getUserInfo(AuthToken authToken) {
		String accessToken = authToken.getAccessToken();
		// 获取 openId
		String openId = this.getOpenId(accessToken, authToken);
		// 用户信息
		JsonNode object = this.getUserInfo(accessToken, openId);
		if (object.get("ret").asInt() != 0) {
			throw new AuthException(object.get("msg").asText());
		}
		String avatar = null;
		if (object.has("figureurl_qq_2")) {
			avatar = object.get("figureurl_qq_2").asText();
		}
		if (StringUtil.isBlank(avatar)) {
			avatar = object.get("figureurl_qq_1").asText();
		}
		return AuthUser.builder()
			.username(object.get("nickname").asText())
			.nickname(object.get("nickname").asText())
			.avatar(avatar)
			.location(object.at("/province").asText() + "-" + object.at("/city").asText())
			.uuid(Optional.ofNullable(authToken.getUnionId()).orElse(openId))
			.gender(AuthUserGender.getRealGender(object.get("gender").asText()))
			.token(authToken)
			.source(authSource)
			.build();
	}

	private String getOpenId(String accessToken, AuthToken authToken) {
		HttpResponse response = HttpRequest.get("https://graph.qq.com/oauth2.0/me")
			.query("access_token", accessToken)
			.execute();
		if (response.isOk()) {
			String body = response.asString();
			String removePrefix = StringUtil.replace(body, "callback(", "");
			String removeSuffix = StringUtil.replace(removePrefix, ");", "");
			JsonNode object = JsonUtil.readTree(StringUtil.trimWhitespace(removeSuffix));
			String unionId = object.at("/unionid").asText();
			String openId = object.get("openid").asText();
			authToken.setUnionId(unionId);
			authToken.setOpenId(openId);
			return openId;
		}
		throw new AuthException("request error");
	}

	private JsonNode getUserInfo(String accessToken, String openId) {
		// {"ret":0,"msg":"","nickname":"YOUR_NICK_NAME",...}
		return HttpRequest.get(authSource.userInfo())
			.query("access_token", accessToken)
			.query("oauth_consumer_key", config.getClientId())
			.query("openid", openId)
			.query("format", "json")
			.execute()
			.asJsonNode();
	}
}
