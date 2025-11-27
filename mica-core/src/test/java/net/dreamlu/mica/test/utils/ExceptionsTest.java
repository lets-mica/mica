package net.dreamlu.mica.test.utils;

import net.dreamlu.mica.core.utils.Exceptions;
import net.dreamlu.mica.core.utils.JsonUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tools.jackson.core.JacksonException;

import java.io.IOException;

/**
 * Exceptions 工具测试
 *
 * @author L.cm
 */
class ExceptionsTest {

	@Test
	void testIOException() {
		Assertions.assertThrows(IOException.class, () -> {
			throw Exceptions.unchecked(new IOException());
		});
	}

	@Test
	void testJson() {
		Assertions.assertThrows(JacksonException.class, () -> {
			JsonUtil.readValue("`12123`", Object.class);
		});
	}
}
