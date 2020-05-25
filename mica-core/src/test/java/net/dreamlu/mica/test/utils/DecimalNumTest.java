package net.dreamlu.mica.test.utils;

import net.dreamlu.mica.core.utils.DecimalNum;

import java.math.BigDecimal;

public class DecimalNumTest {

	public static void main(String[] args) {
		BigDecimal value = DecimalNum.of(1.0D)
			.add(2.999999)
			.add(3.1111)
			.divide(2)
			.scale(2)
			.getValue();
		System.out.println(value);
	}
}
