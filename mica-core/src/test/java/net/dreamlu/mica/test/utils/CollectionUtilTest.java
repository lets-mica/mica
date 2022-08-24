package net.dreamlu.mica.test.utils;

import net.dreamlu.mica.core.utils.CollectionUtil;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * CollectionUtil Tester.
 *
 * @author L.cm
 * @version 1.0
 */
class CollectionUtilTest {

	/**
	 * Method: isNotEmpty(@Nullable Collection<?> collection)
	 */
	@Test
	void testIsNotEmptyCollection() throws Exception {
		//TODO: Test goes here...
	}

	/**
	 * Method: isNotEmpty(@Nullable Map<?, ?> map)
	 */
	@Test
	void testIsNotEmptyMap() throws Exception {
		//TODO: Test goes here...
	}

	/**
	 * Method: contains(@Nullable T[] array, final T element)
	 */
	@Test
	void testContains() throws Exception {
		//TODO: Test goes here...
	}

	/**
	 * Method: concat(String[] one, String[] other)
	 */
	@Test
	void testConcatForOneOther() throws Exception {
		//TODO: Test goes here...
	}

	/**
	 * Method: concat(T[] one, T[] other, Class<T> clazz)
	 */
	@Test
	void testConcatForOneOtherClazz() throws Exception {
		//TODO: Test goes here...
	}

	/**
	 * Method: ofImmutableSet(E... es)
	 */
	@Test
	void testOfImmutableSet() throws Exception {
		//TODO: Test goes here...
	}

	/**
	 * Method: ofImmutableList(E... es)
	 */
	@Test
	void testOfImmutableList() throws Exception {
		//TODO: Test goes here...
	}

	/**
	 * Method: toList(Iterable<E> elements)
	 */
	@Test
	void testToList() throws Exception {
		//TODO: Test goes here...
	}

	/**
	 * Method: toMap(Object... keysValues)
	 */
	@Test
	void testToMap() throws Exception {
		Map<Object, String> objectMap = CollectionUtil.toMap("1", "2", "3", 4);
		System.out.println(objectMap);
	}

}
