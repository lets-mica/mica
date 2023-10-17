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

class HolidaysApiTest {

	private HolidaysApi holidaysApi;

	@BeforeEach
	public void setup() throws Exception {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		HolidaysApiConfiguration configuration = new HolidaysApiConfiguration();
		holidaysApi = configuration.holidaysApi(context, new HolidaysApiProperties());
		((HolidaysApiImpl) holidaysApi).afterPropertiesSet();
	}

	@Test
	void test() {
		DaysType daysType = holidaysApi.getDaysType(LocalDate.of(2023, 1, 1));
		Assertions.assertEquals(DaysType.HOLIDAYS, daysType);
		Assertions.assertFalse(holidaysApi.isWeekdays(LocalDate.of(2023, 9, 29)));
		Assertions.assertTrue(holidaysApi.isWeekdays(LocalDate.of(2023, 10, 7)));
	}

}
