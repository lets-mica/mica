package net.dreamlu.mica.test.utils;

import net.dreamlu.mica.core.utils.CollectionUtil;
import org.junit.Test;

import java.util.Map;

/**
 * CollectionUtil Tester.
 *
 * @author L.cm
 * @version 1.0
 */
public class CollectionUtilTest {

	/**
	 * Method: isNotEmpty(@Nullable Collection<?> collection)
	 */
	@Test
	public void testIsNotEmptyCollection() throws Exception {
		//TODO: Test goes here...
	}

	/**
	 * Method: isNotEmpty(@Nullable Map<?, ?> map)
	 */
	@Test
	public void testIsNotEmptyMap() throws Exception {
		//TODO: Test goes here...
	}

	/**
	 * Method: contains(@Nullable T[] array, final T element)
	 */
	@Test
	public void testContains() throws Exception {
		//TODO: Test goes here...
	}

	/**
	 * Method: concat(String[] one, String[] other)
	 */
	@Test
	public void testConcatForOneOther() throws Exception {
		//TODO: Test goes here...
	}

	/**
	 * Method: concat(T[] one, T[] other, Class<T> clazz)
	 */
	@Test
	public void testConcatForOneOtherClazz() throws Exception {
		//TODO: Test goes here...
	}

	/**
	 * Method: ofImmutableSet(E... es)
	 */
	@Test
	public void testOfImmutableSet() throws Exception {
		//TODO: Test goes here...
	}

	/**
	 * Method: ofImmutableList(E... es)
	 */
	@Test
	public void testOfImmutableList() throws Exception {
		//TODO: Test goes here...
	}

	/**
	 * Method: toList(Iterable<E> elements)
	 */
	@Test
	public void testToList() throws Exception {
		//TODO: Test goes here...
	}

	/**
	 * Method: toMap(Object... keysValues)
	 */
	@Test
	public void testToMap() throws Exception {
		Map<Object, String> objectMap = CollectionUtil.toMap("1", "2", "3", 4);
		System.out.println(objectMap);
	}

}
