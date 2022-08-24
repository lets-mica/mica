package net.dreamlu.mica.test.utils;

import net.dreamlu.mica.core.utils.DatePattern;
import net.dreamlu.mica.core.utils.DateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;

/**
 * DateTimeUtil 测试
 */
class DateTimeUtilTest {

	@Test
	void test() {
		TemporalAccessor temporalAccessor = DateUtil.parseDateTime("2018-11-11 11:11:11", DateUtil.DATETIME_FORMATTER);
		Assertions.assertTrue(temporalAccessor instanceof LocalDateTime);
	}

	@Test
	void test1() {
		DateUtil.parseDateTime("2018-11-11 11:11:11", new String[]{
			DatePattern.UTC_PATTERN,
			DatePattern.NORM_DATETIME_PATTERN
		});
	}
}
