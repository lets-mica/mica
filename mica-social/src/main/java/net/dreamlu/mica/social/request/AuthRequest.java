package net.dreamlu.mica.social.request;

import net.dreamlu.mica.core.utils.StringUtil;
import net.dreamlu.mica.social.config.AuthSource;
import net.dreamlu.mica.social.exception.AuthException;
import net.dreamlu.mica.social.model.AuthResponse;
import net.dreamlu.mica.social.model.AuthToken;

/**
 * 抽象的认证授权接口
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com), L.cm
 */
public interface AuthRequest {

	/**
	 * 获取 AuthSource
	 *
	 * @return AuthSource
	 */
	default AuthSource getAuthSource() {
		return null;
	}

	/**
	 * 返回认证url，可自行跳转页面，state 使用的 uuid，不太建议忽略 state 校验
	 *
	 * @return 返回授权地址
	 */
	default String authorize() {
		return this.authorize(StringUtil.getUUID());
	}

	/**
	 * 返回认证url，可自行跳转页面
	 *
	 * @param state OAuth2.0标准协议建议，利用state参数来防止CSRF攻击。可存储于session或其他cache中
	 * @return 返回授权地址
	 */
	default String authorize(String state) {
		throw new AuthException(ResponseStatus.NOT_IMPLEMENTED);
	}

	/**
	 * 第三方登录
	 *
	 * @param code 通过authorize换回的code
	 * @return 返回登录成功后的用户信息
	 */
	default AuthResponse login(String code) {
		throw new AuthException(ResponseStatus.NOT_IMPLEMENTED);
	}

	/**
	 * 撤销授权
	 *
	 * @param authToken 登录成功后返回的Token信息
	 * @return AuthResponse
	 */
	default AuthResponse revoke(AuthToken authToken) {
		throw new AuthException(ResponseStatus.NOT_IMPLEMENTED);
	}

	/**
	 * 刷新access token （续期）
	 *
	 * @param authToken 登录成功后返回的Token信息
	 * @return AuthResponse
	 */
	default AuthResponse refresh(AuthToken authToken) {
		throw new AuthException(ResponseStatus.NOT_IMPLEMENTED);
	}
}
