package net.dreamlu.mica.test.convert;

import net.dreamlu.mica.core.convert.MicaConversionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.support.GenericConversionService;

class ConvertTest {

	private GenericConversionService conversionService;

	@BeforeEach
	public void setUp() {
		conversionService = MicaConversionService.getInstance();
	}

	@Test
	void test() {
		UserEnum userEnum = UserEnum.ADMIN;
		String str1 = conversionService.convert(userEnum, String.class);
		Assertions.assertEquals(UserEnum.ADMIN.name(), str1);

		UserEnum enum1 = conversionService.convert(UserEnum.TEST.name(), UserEnum.class);
		Assertions.assertEquals(UserEnum.TEST, enum1);
	}

	@Test
	void test1() {
		UserEnum1 userEnum = UserEnum1.ADMIN;
		String str1 = conversionService.convert(userEnum, String.class);
		Assertions.assertEquals("admin", str1);

		UserEnum1 enum1 = conversionService.convert(UserEnum1.TEST.name(), UserEnum1.class);
		Assertions.assertEquals(UserEnum1.TEST, enum1);
	}

	@Test
	void test2() {
		UserEnum2 userEnum = UserEnum2.ADMIN;
		String str1 = conversionService.convert(userEnum, String.class);
		Assertions.assertEquals("admin", str1);

		UserEnum2 enum1 = conversionService.convert("test", UserEnum2.class);
		Assertions.assertEquals(UserEnum2.TEST, enum1);
	}

	@Test
	void testx() {
		UserEnum3 userEnum = UserEnum3.ADMIN;
		Long str1 =  conversionService.convert(userEnum, Long.class);

		UserEnum3 enum1 = conversionService.convert("2", UserEnum3.class);
		Assertions.assertEquals(UserEnum3.TEST, enum1);
	}
}
