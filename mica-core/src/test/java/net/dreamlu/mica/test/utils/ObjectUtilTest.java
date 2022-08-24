package net.dreamlu.mica.test.utils;

import net.dreamlu.mica.core.utils.ObjectUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

/**
 * ObjectUtil Tester.
 *
 * @author L.cm
 * @version 1.0
 */
class ObjectUtilTest {

	/**
	 * Method: isNull(@Nullable Object object)
	 */
	@Test
	void testIsNull() throws Exception {
		Assertions.assertTrue(ObjectUtil.isNull(null));
	}

	/**
	 * Method: isNotNull(@Nullable Object object)
	 */
	@Test
	void testIsNotNull() throws Exception {
		Assertions.assertFalse(ObjectUtil.isNotNull(null));
	}

	/**
	 * Method: isTrue(@Nullable Boolean object)
	 */
	@Test
	void testIsTrue() throws Exception {
		Assertions.assertTrue(ObjectUtil.isTrue(true));
		Assertions.assertTrue(ObjectUtil.isTrue(Boolean.TRUE));
		Assertions.assertFalse(ObjectUtil.isTrue(null));
		Assertions.assertFalse(ObjectUtil.isTrue(false));
		Assertions.assertFalse(ObjectUtil.isTrue(Boolean.FALSE));
	}

	/**
	 * Method: isFalse(@Nullable Boolean object)
	 */
	@Test
	void testIsFalse() throws Exception {
		Assertions.assertTrue(ObjectUtil.isFalse(null));
		Assertions.assertTrue(ObjectUtil.isFalse(false));
		Assertions.assertTrue(ObjectUtil.isFalse(Boolean.FALSE));
		Assertions.assertFalse(ObjectUtil.isFalse(true));
		Assertions.assertFalse(ObjectUtil.isFalse(Boolean.TRUE));
	}

	/**
	 * Method: isNotEmpty(@Nullable Object[] array)
	 */
	@Test
	void testIsNotEmpty() throws Exception {
		Assertions.assertFalse(ObjectUtil.isNotEmpty(null));
		Assertions.assertFalse(ObjectUtil.isNotEmpty(new Object[0]));
		Assertions.assertFalse(ObjectUtil.isNotEmpty(Collections.emptyList()));
	}

	/**
	 * Method: isNotEmpty(@Nullable Object obj)
	 */
	@Test
	void testIsEmptyObj() throws Exception {
		Assertions.assertTrue(ObjectUtil.isEmpty(null));
		Assertions.assertTrue(ObjectUtil.isEmpty(new Object[0]));
		Assertions.assertTrue(ObjectUtil.isEmpty(Collections.emptyList()));
	}

	/**
	 * Method: toBoolean(@Nullable Object obj)
	 */
	@Test
	void testToBoolean() throws Exception {
		Assertions.assertNull(ObjectUtil.toBoolean(null));
		Assertions.assertNull(ObjectUtil.toBoolean("a"));
		Assertions.assertTrue(ObjectUtil.toBoolean("1"));
		Assertions.assertTrue(ObjectUtil.toBoolean(null, true));
		Assertions.assertTrue(ObjectUtil.toBoolean("a", true));
		Assertions.assertTrue(ObjectUtil.toBoolean("1", true));
	}

}
