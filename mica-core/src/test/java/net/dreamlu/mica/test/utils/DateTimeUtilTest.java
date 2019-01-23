package net.dreamlu.mica.test.utils;

import net.dreamlu.mica.core.utils.DateTimeUtil;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;

/**
 * DateTimeUtil 测试
 */
public class DateTimeUtilTest {

	@Test
	public void test() {
		TemporalAccessor temporalAccessor = DateTimeUtil.parseDateTime("2018-11-11 11:11:11", DateTimeUtil.DATETIME_FORMAT);
		Assert.assertTrue(temporalAccessor instanceof LocalDateTime);
	}
}
