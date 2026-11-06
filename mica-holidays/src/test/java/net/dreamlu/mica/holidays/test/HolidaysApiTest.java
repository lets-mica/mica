package net.dreamlu.mica.holidays.test;

import net.dreamlu.mica.holidays.config.HolidaysApiConfiguration;
import net.dreamlu.mica.holidays.config.HolidaysApiProperties;
import net.dreamlu.mica.holidays.core.DaysType;
import net.dreamlu.mica.holidays.core.HolidaysApi;
import net.dreamlu.mica.holidays.impl.HolidaysApiImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.util.Collections;

class HolidaysApiTest {

	private HolidaysApi holidaysApi;

	@BeforeEach
	public void setup() throws Exception {
		holidaysApi = createHolidaysApi(new HolidaysApiProperties());
	}

	private HolidaysApi createHolidaysApi(HolidaysApiProperties properties) throws Exception {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		HolidaysApiConfiguration configuration = new HolidaysApiConfiguration();
		HolidaysApi api = configuration.holidaysApi(context, properties);
		((HolidaysApiImpl) api).afterPropertiesSet();
		return api;
	}

	@Test
	void test() {
		DaysType daysType = holidaysApi.getDaysType(LocalDate.of(2023, 1, 1));
		Assertions.assertEquals(DaysType.HOLIDAYS, daysType);
		Assertions.assertFalse(holidaysApi.isWeekdays(LocalDate.of(2023, 9, 29)));
		Assertions.assertTrue(holidaysApi.isWeekdays(LocalDate.of(2023, 10, 7)));
		Assertions.assertFalse(holidaysApi.isWeekdays(LocalDate.of(2025, 10, 8)));
	}

	/**
	 * 内置数据按 classpath 扫描自动加载，无需维护年份列表
	 */
	@Test
	void testBuiltInDataAutoDiscover() {
		// 2027 年数据文件存在，应自动被扫描到
		Assertions.assertEquals(DaysType.HOLIDAYS, holidaysApi.getDaysType(LocalDate.of(2027, 1, 1)));
		// 2027-10-09 是调休上班日
		Assertions.assertTrue(holidaysApi.isWeekdays(LocalDate.of(2027, 10, 9)));
	}

	/**
	 * 最早年份的数据同样可以正常加载
	 */
	@Test
	void testEarliestYearData() {
		Assertions.assertEquals(DaysType.HOLIDAYS, holidaysApi.getDaysType(LocalDate.of(2019, 1, 1)));
	}

	/**
	 * 没有数据文件的年份，按正常的周六日判断
	 */
	@Test
	void testYearWithoutData() {
		// 2099 年没有数据文件，1 月 1 日是周四，应为工作日
		Assertions.assertEquals(DaysType.WEEKDAYS, holidaysApi.getDaysType(LocalDate.of(2099, 1, 1)));
		// 2099-01-03 是周六，应为休息日
		Assertions.assertEquals(DaysType.REST_DAYS, holidaysApi.getDaysType(LocalDate.of(2099, 1, 3)));
	}

	/**
	 * 扩展的配置优先于内置的数据
	 */
	@Test
	void testExtDataOverridesBuiltInData() throws Exception {
		HolidaysApiProperties properties = new HolidaysApiProperties();
		HolidaysApiProperties.ExtData extData = new HolidaysApiProperties.ExtData();
		// 复用 2023 年的数据文件作为 2098 年的扩展数据
		extData.setYear(2098);
		extData.setDataPath("classpath:data/2023_data.json");
		properties.setExtData(Collections.singletonList(extData));

		HolidaysApi api = createHolidaysApi(properties);
		// 2098-01-01 在 2023 的数据里是节假日
		Assertions.assertEquals(DaysType.HOLIDAYS, api.getDaysType(LocalDate.of(2098, 1, 1)));
	}

	/**
	 * 扩展的数据不存在时，应抛出异常
	 */
	@Test
	void testExtDataNotFound() {
		HolidaysApiProperties properties = new HolidaysApiProperties();
		HolidaysApiProperties.ExtData extData = new HolidaysApiProperties.ExtData();
		extData.setYear(2097);
		extData.setDataPath("classpath:data/2097_not_exist_data.json");
		properties.setExtData(Collections.singletonList(extData));

		Assertions.assertThrows(Exception.class, () -> createHolidaysApi(properties));
	}

}
