package net.dreamlu.mica.xss.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dreamlu.mica.core.result.IResultCode;

/**
 * xss 校验错误码
 *
 * @author L.cm
 */
@Getter
@RequiredArgsConstructor
public enum XssResultCode implements IResultCode {

	/**
	 * 表单校验失败
	 */
	XSS_FORM_FAILED(XssResultCode.XSS_FORM_FAILEDCODE, "表单校验失败"),

	/**
	 * body json 校验失败
	 */
	XSS_JSON_FAILED(XssResultCode.XSS_JSON_FAILEDCODE, "body json 校验失败"),
	;

	public static final int XSS_FORM_FAILEDCODE = 400101;
	public static final int XSS_JSON_FAILEDCODE = 400102;

	/**
	 * code编码
	 */
	final int code;
	/**
	 * 中文信息描述
	 */
	final String msg;
}
