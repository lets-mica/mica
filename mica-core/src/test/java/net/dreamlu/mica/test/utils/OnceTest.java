package net.dreamlu.mica.test.utils;

import net.dreamlu.mica.core.utils.Once;
import org.junit.Assert;
import org.junit.Test;

public class OnceTest {

	@Test
	public void test() {
		Once once = new Once();
		Assert.assertTrue(once.run());
		Assert.assertFalse(once.run());
		Assert.assertFalse(once.run());
		Assert.assertFalse(once.run());
		Assert.assertFalse(once.run());
		Assert.assertFalse(once.run());
	}
}
