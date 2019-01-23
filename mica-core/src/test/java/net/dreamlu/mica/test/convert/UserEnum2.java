package net.dreamlu.mica.test.convert;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dreamlu.mica.core.utils.$;

/**
 * 用户枚举
 */
@Getter
@AllArgsConstructor
public enum UserEnum2 {
	ADMIN("admin"),

	TEST("test");

	@JsonValue
	private String value;

	@JsonCreator
	public static UserEnum2 of(String value) {
		if ($.isBlank(value)) {
			return null;
		}
		for (UserEnum2 userEnum2: UserEnum2.values()) {
			if (userEnum2.value.equals(value)) {
				return userEnum2;
			}
		}
		return null;
	}
}
