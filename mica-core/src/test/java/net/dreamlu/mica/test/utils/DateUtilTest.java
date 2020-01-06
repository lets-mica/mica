package net.dreamlu.mica.test.utils;

import net.dreamlu.mica.core.utils.DateUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * DateUtil Tester.
 *
 * @author L.cm
 * @version 1.0
 * @since <pre>Apr 29, 2019</pre>
 */
public class DateUtilTest {

	/**
	 * Method: plusYears(Date date, int yearsToAdd)
	 */
	@Test
	public void testPlusYears() throws Exception {
		Date date = new Date();
		Date date1 = DateUtil.plusYears(date, 1);

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, 1);
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);

		Assert.assertEquals(c.get(Calendar.YEAR), c1.get(Calendar.YEAR));
	}

	/**
	 * Method: plusMonths(Date date, int monthsToAdd)
	 */
	@Test
	public void testPlusMonths() throws Exception {
		Date date = new Date();
		Date date1 = DateUtil.plusMonths(date, 1);

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, 1);
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);

		Assert.assertEquals(c.get(Calendar.MONTH), c1.get(Calendar.MONTH));
	}

	/**
	 * Method: plusDays(Date date, long daysToAdd)
	 */
	@Test
	public void testPlusDays() throws Exception {
		Date date = new Date();
		Date date1 = DateUtil.plusDays(date, 1);

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_YEAR, 1);
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);

		Assert.assertEquals(c.get(Calendar.DAY_OF_YEAR), c1.get(Calendar.DAY_OF_YEAR));
	}

	/**
	 * Method: minusYears(Date date, int years)
	 */
	@Test
	public void testMinusYears() throws Exception {
		Date date = new Date();
		Date date1 = DateUtil.minusYears(date, 1);

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, -1);
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);

		Assert.assertEquals(c.get(Calendar.YEAR), c1.get(Calendar.YEAR));
	}

	/**
	 * Method: minusMonths(Date date, int months)
	 */
	@Test
	public void testMinusMonths() throws Exception {
		Date date = new Date();
		Date date1 = DateUtil.minusMonths(date, 1);

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, -1);
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);

		Assert.assertEquals(c.get(Calendar.MONTH), c1.get(Calendar.MONTH));
	}

	/**
	 * Method: minusDays(Date date, long days)
	 */
	@Test
	public void testMinusDays() throws Exception {
		Date date = new Date();
		Date date1 = DateUtil.minusDays(date, 1);

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_YEAR, -1);
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);

		Assert.assertEquals(c.get(Calendar.DAY_OF_YEAR), c1.get(Calendar.DAY_OF_YEAR));
	}

	@Test
	public void testDate() throws Exception {
		Date date = new Date();
		System.out.println(DateUtil.formatDateTime(date));
	}
}
