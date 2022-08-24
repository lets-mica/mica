package net.dreamlu.mica.test.utils;

import net.dreamlu.mica.core.utils.DatePattern;
import net.dreamlu.mica.core.utils.DateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * DateUtil Tester.
 *
 * @author L.cm
 * @version 1.0
 * @since <pre>Apr 29, 2019</pre>
 */
class DateUtilTest {

	/**
	 * Method: plusYears(Date date, int yearsToAdd)
	 */
	@Test
	void testPlusYears() {
		Date date = new Date();
		Date date1 = DateUtil.plusYears(date, 1);

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, 1);
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);

		Assertions.assertEquals(c.get(Calendar.YEAR), c1.get(Calendar.YEAR));
	}

	/**
	 * Method: plusMonths(Date date, int monthsToAdd)
	 */
	@Test
	void testPlusMonths() {
		Date date = new Date();
		Date date1 = DateUtil.plusMonths(date, 1);
		DateUtil.plusWeeks(date, 1);

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, 1);
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);

		Assertions.assertEquals(c.get(Calendar.MONTH), c1.get(Calendar.MONTH));
	}

	/**
	 * Method: plusDays(Date date, long daysToAdd)
	 */
	@Test
	void testPlusDays() {
		Date date = new Date();
		Date date1 = DateUtil.plusDays(date, 1);

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_YEAR, 1);
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);

		Assertions.assertEquals(c.get(Calendar.DAY_OF_YEAR), c1.get(Calendar.DAY_OF_YEAR));
	}

	/**
	 * Method: minusYears(Date date, int years)
	 */
	@Test
	void testMinusYears() {
		Date date = new Date();
		Date date1 = DateUtil.minusYears(date, 1);

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, -1);
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);

		Assertions.assertEquals(c.get(Calendar.YEAR), c1.get(Calendar.YEAR));
	}

	/**
	 * Method: minusMonths(Date date, int months)
	 */
	@Test
	void testMinusMonths() {
		Date date = new Date();
		Date date1 = DateUtil.minusMonths(date, 1);
		DateUtil.minusWeeks(date, 1);

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, -1);
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);

		Assertions.assertEquals(c.get(Calendar.MONTH), c1.get(Calendar.MONTH));
	}

	/**
	 * Method: minusDays(Date date, long days)
	 */
	@Test
	void testMinusDays() {
		Date date = new Date();
		Date date1 = DateUtil.minusDays(date, 1);

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_YEAR, -1);
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);

		Assertions.assertEquals(c.get(Calendar.DAY_OF_YEAR), c1.get(Calendar.DAY_OF_YEAR));
	}

	@Test
	void testDate() {
		Date date = new Date();
		System.out.println(DateUtil.formatDateTime(date));
	}

	@Test
	void testDateParse() {
		Date date = DateUtil.parse("2020-04-23", DatePattern.NORM_DATE_PATTERN);
		System.out.println(date);
		String format = DateUtil.format(date, DatePattern.NORM_DATE_PATTERN);
		System.out.println(format);
	}

	@Test
	void testTimeParse() {
		Date date = DateUtil.parse("16:12:12", DatePattern.NORM_TIME_FORMAT);
		System.out.println(date);
		String format = DateUtil.format(date, DatePattern.NORM_TIME_PATTERN);
		System.out.println(format);
	}

	@Test
	void testDateTimeParse() {
		Date date = DateUtil.parse("2020-04-23 16:12:12", DatePattern.NORM_DATETIME_FORMAT);
		System.out.println(date);
		String format = DateUtil.format(date, DatePattern.NORM_DATETIME_PATTERN);
		System.out.println(format);
	}

}
