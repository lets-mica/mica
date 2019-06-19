package net.dreamlu.mica.social.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com), L.cm
 * @version 1.0
 * @since 1.8
 */
@Getter
@AllArgsConstructor
public enum ResponseStatus {
	/**
	 * 相应状态
	 */
	SUCCESS(2000, "Success"),
	FAILURE(5000, "Failure"),
	NOT_IMPLEMENTED(5001, "Not Implemented"),
	PARAMETER_INCOMPLETE(5002, "Parameter incomplete"),
	UNSUPPORTED(5003, "Unsupported operation"),
	NO_AUTH_SOURCE(5004, "AuthSource cannot be null"),
	UNIDENTIFIED_PLATFORM(5005, "Unidentified platform"),
	;

	private int code;
	private String msg;

}

