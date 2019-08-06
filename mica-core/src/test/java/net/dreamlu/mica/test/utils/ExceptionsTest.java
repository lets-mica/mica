package net.dreamlu.mica.test.utils;

import net.dreamlu.mica.core.utils.Exceptions;
import net.dreamlu.mica.core.utils.JsonUtil;
import org.junit.Test;

import java.io.IOException;

/**
 * Exceptions 工具测试
 *
 * @author L.cm
 */
public class ExceptionsTest {

	@Test(expected = IOException.class)
	public void testIOException() {
		throw Exceptions.unchecked(new IOException());
	}

	@Test(expected = IOException.class)
	public void testJson() {
		JsonUtil.readValue("`12123`", Object.class);
	}
}
