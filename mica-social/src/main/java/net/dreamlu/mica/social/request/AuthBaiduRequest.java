package net.dreamlu.mica.social.request;

import com.fasterxml.jackson.databind.JsonNode;
import net.dreamlu.http.HttpRequest;
import net.dreamlu.mica.social.config.AuthConfig;
import net.dreamlu.mica.social.config.AuthSource;
import net.dreamlu.mica.social.exception.AuthException;
import net.dreamlu.mica.social.model.*;
import net.dreamlu.mica.social.utils.UrlBuilder;

/**
 * 百度账号登录
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com), L.cm
 * @version 1.0
 * @since 1.8
 */
public class AuthBaiduRequest extends BaseAuthRequest {

	public AuthBaiduRequest(AuthConfig config) {
		super(config, AuthSource.BAIDU);
	}

	@Override
	public String authorize() {
		return UrlBuilder.getBaiduAuthorizeUrl(config.getClientId(), config.getRedirectUri());
	}

	@Override
	protected AuthToken getAccessToken(String code) {
		String accessTokenUrl = UrlBuilder.getBaiduAccessTokenUrl(config.getClientId(), config.getClientSecret(), code, config.getRedirectUri());

		JsonNode jsonNode = HttpRequest.post(accessTokenUrl)
			.execute()
			.asJsonNode();
		AuthBaiduErrorCode errorCode = AuthBaiduErrorCode.getErrorCode(jsonNode.get("error").asText());
		if (!AuthBaiduErrorCode.OK.equals(errorCode)) {
			throw new AuthException(errorCode.getDesc());
		}
		return AuthToken.builder()
			.accessToken(jsonNode.get("access_token").asText())
			.build();
	}

	@Override
	protected AuthUser getUserInfo(AuthToken authToken) {
		String accessToken = authToken.getAccessToken();

		JsonNode jsonNode = HttpRequest.get(UrlBuilder.getBaiduUserInfoUrl(accessToken))
			.execute()
			.asJsonNode();
		AuthBaiduErrorCode errorCode = AuthBaiduErrorCode.getErrorCode(jsonNode.get("error").asText());
		if (!AuthBaiduErrorCode.OK.equals(errorCode)) {
			throw new AuthException(errorCode.getDesc());
		}
		return AuthUser.builder()
			.uuid(jsonNode.get("userid").asText())
			.username(jsonNode.get("username").asText())
			.nickname(jsonNode.get("username").asText())
			.gender(AuthUserGender.getRealGender(jsonNode.get("sex").asText()))
			.token(authToken)
			.source(AuthSource.BAIDU)
			.build();
	}

	@Override
	public AuthResponse revoke(AuthToken authToken) {
		String accessToken = authToken.getAccessToken();

		JsonNode jsonNode = HttpRequest.get(UrlBuilder.getBaiduRevokeUrl(accessToken))
			.execute()
			.asJsonNode();
		if (jsonNode.has("error_code")) {
			return AuthResponse.builder()
				.code(ResponseStatus.FAILURE.getCode())
				.msg(jsonNode.get("error_msg").asText())
				.build();
		}
		ResponseStatus status = jsonNode.get("result").asInt() == 1 ? ResponseStatus.SUCCESS : ResponseStatus.FAILURE;
		return AuthResponse.builder().code(status.getCode()).msg(status.getMsg()).build();
	}

}
