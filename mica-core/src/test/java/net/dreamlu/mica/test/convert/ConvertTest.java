package net.dreamlu.mica.test.convert;

import net.dreamlu.mica.core.convert.EnumToStringConverter;
import net.dreamlu.mica.core.convert.MicaConversionService;
import net.dreamlu.mica.core.convert.StringToEnumConverter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ConvertTest {

	private MicaConversionService conversionService;

	@Before
	public void setUp() {
		conversionService = new MicaConversionService();
		conversionService.addConverter(new EnumToStringConverter());
		conversionService.addConverter(new StringToEnumConverter());
	}

	@Test
	public void test() {
		UserEnum userEnum = UserEnum.ADMIN;
		String str1 = conversionService.convert(userEnum, String.class);
		Assert.assertEquals(UserEnum.ADMIN.name(), str1);

		UserEnum enum1 = conversionService.convert(UserEnum.TEST.name(), UserEnum.class);
		Assert.assertEquals(UserEnum.TEST, enum1);
	}

	@Test
	public void test1() {
		UserEnum1 userEnum = UserEnum1.ADMIN;
		String str1 = conversionService.convert(userEnum, String.class);
		Assert.assertEquals("admin", str1);

		UserEnum1 enum1 = conversionService.convert(UserEnum1.TEST.name(), UserEnum1.class);
		Assert.assertEquals(UserEnum1.TEST, enum1);
	}

	@Test
	public void test2() {
		UserEnum2 userEnum = UserEnum2.ADMIN;
		String str1 = conversionService.convert(userEnum, String.class);
		Assert.assertEquals("admin", str1);

		UserEnum2 enum1 = conversionService.convert("test", UserEnum2.class);
		Assert.assertEquals(UserEnum2.TEST, enum1);
	}

	@Test
	public void testx() {
		UserEnum3 userEnum = UserEnum3.ADMIN;
		Long str1 =  conversionService.convert(userEnum, Long.class);

		UserEnum3 enum1 = conversionService.convert("2", UserEnum3.class);
		Assert.assertEquals(UserEnum3.TEST, enum1);
	}
}
