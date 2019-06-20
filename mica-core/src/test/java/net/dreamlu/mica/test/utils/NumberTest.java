package net.dreamlu.mica.test.utils;

import net.dreamlu.mica.core.utils.NumberUtil;
import org.junit.Assert;
import org.junit.Test;

public class NumberTest {

	@Test
	public void testTo62String() {
		long ms = 1551320493447L;
		String string = NumberUtil.to62Str(ms);
		Assert.assertEquals(string, "rjkOH7p");
	}

}
