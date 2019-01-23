package net.dreamlu.mica.test.convert;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户枚举
 */
@Getter
@AllArgsConstructor
public enum UserEnum1 {
	ADMIN("admin"),

	TEST("test");

	@JsonValue
	private String value;
}
