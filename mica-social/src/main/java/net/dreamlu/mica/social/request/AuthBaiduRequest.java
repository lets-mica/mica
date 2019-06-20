package net.dreamlu.mica.social.request;

import com.fasterxml.jackson.databind.JsonNode;
import net.dreamlu.http.HttpRequest;
import net.dreamlu.mica.social.config.AuthConfig;
import net.dreamlu.mica.social.config.AuthSource;
import net.dreamlu.mica.social.exception.AuthException;
import net.dreamlu.mica.social.model.*;

/**
 * 百度账号登录
 *
 * @author L.cm
 */
public class AuthBaiduRequest extends BaseAuthRequest {

	public AuthBaiduRequest(AuthConfig config) {
		super(config, AuthSource.BAIDU);
	}

	@Override
	protected AuthToken getAccessToken(String code) {
		JsonNode jsonNode = doPostAuthorizationCode(code).asJsonNode();
		// {"expires_in":2592000,"refresh_token":"","access_token":"","session_secret":"","session_key":"","scope":"basic"}
		if (jsonNode.has("error")) {
			AuthBaiduErrorCode errorCode = AuthBaiduErrorCode.getErrorCode(jsonNode.at("error").asText());
			throw new AuthException(errorCode.getDesc());
		}
		return AuthToken.builder()
			.accessToken(jsonNode.get("access_token").asText())
			.build();
	}

	@Override
	protected AuthUser getUserInfo(AuthToken authToken) {
		String accessToken = authToken.getAccessToken();
		JsonNode jsonNode = HttpRequest.post(authSource.userInfo())
			.query("access_token", accessToken)
			.execute()
			.asJsonNode();
		// {"marriage":"1","userdetail":"javaer","constellation":"11","job":"0","userid":"","figure":"7","blood":"0","trade":"5","username":"","education":"7","sex":"1","portrait":"","birthday":"","is_bind_mobile":"1","is_realname":"1"}
		if (jsonNode.has("error")) {
			AuthBaiduErrorCode errorCode = AuthBaiduErrorCode.getErrorCode(jsonNode.get("error").asText());
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
		JsonNode jsonNode = HttpRequest.post(authSource.revoke())
			.query("access_token", accessToken)
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
