package net.dreamlu.mica.test.utils;

import net.dreamlu.mica.core.utils.DateUtil;
import net.dreamlu.mica.core.utils.Unchecked;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 测试并发异常
 *
 * @author L.cm
 */
public class DateFormatTest {
	private static String dateStr = "2018-06-22T10:00:00";
	private static String pattern = "yyyy-MM-dd'T'HH:mm:ss";
	private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(pattern);

	@Test
	public void test1() throws InterruptedException {
		Set<String> dateSet = new TreeSet<>();
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		Runnable task = Unchecked.runnable(() -> {
			String date = parseDate1(dateStr);
			dateSet.add(date);
		});

		for (int i = 0; i < 100; i++) {
			executorService.submit(task);
		}

		executorService.awaitTermination(100, TimeUnit.MILLISECONDS);
		Assert.assertEquals(1, dateSet.size());
	}

	@Test
	public void test2() throws InterruptedException {
		Set<String> dateSet = new TreeSet<>();
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		Runnable task = Unchecked.runnable(() -> {
			String date = parseDate2(dateStr);
			dateSet.add(date);
		});

		for (int i = 0; i < 100; i++) {
			executorService.submit(task);
		}

		executorService.awaitTermination(100, TimeUnit.MILLISECONDS);
		Assert.assertTrue(dateSet.size() > 0);
	}

	private static String parseDate1(String dateStr) {
		Date date = DateUtil.parse(dateStr, pattern);
		return DateUtil.format(date, pattern);
	}

	private static String parseDate2(String dateStr) throws ParseException {
		Date date = DATE_FORMAT.parse(dateStr);
		return DateUtil.format(date, pattern);
	}
}
