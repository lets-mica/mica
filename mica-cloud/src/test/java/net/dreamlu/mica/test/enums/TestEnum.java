package net.dreamlu.mica.test.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TestEnum {
	A(1, "A"),
	B(2, "B");

	@JsonValue
	private Integer value;
	private String desc;
}
