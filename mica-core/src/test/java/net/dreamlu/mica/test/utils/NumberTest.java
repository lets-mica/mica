package net.dreamlu.mica.test.utils;

import net.dreamlu.mica.core.utils.NumberUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NumberTest {

	@Test
	void testTo62String() {
		long ms = 1551320493447L;
		String string = NumberUtil.to62Str(ms);
		Assertions.assertEquals("rjkOH7p", string);
	}

}
