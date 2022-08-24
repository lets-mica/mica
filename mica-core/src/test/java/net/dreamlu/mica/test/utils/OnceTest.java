package net.dreamlu.mica.test.utils;

import net.dreamlu.mica.core.utils.Once;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OnceTest {

	@Test
	void test() {
		Once once = new Once();
		Assertions.assertTrue(once.canRun());
		Assertions.assertFalse(once.canRun());
		Assertions.assertFalse(once.canRun());
		Assertions.assertFalse(once.canRun());
		Assertions.assertFalse(once.canRun());
		Assertions.assertFalse(once.canRun());
	}
}
