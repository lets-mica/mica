package net.dreamlu.mica.test.convert;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户枚举
 */
@Getter
@AllArgsConstructor
public enum UserEnum3 {
	ADMIN(1),

	TEST(2);

	@JsonValue
	private Integer value;

	@JsonCreator
	public static UserEnum3 of(Integer value) {
		if (value == null) {
			return null;
		}
		for (UserEnum3 userEnum2: UserEnum3.values()) {
			if (userEnum2.value.equals(value)) {
				return userEnum2;
			}
		}
		return null;
	}
}
