package net.dreamlu.mica.test.utils;

import net.dreamlu.mica.core.utils.ULID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * ULID test
 *
 * @author L.cm
 */
class ULIDTest {

	@Test
	void test() {
		ULID ulid = new ULID();
		String nextULID = ulid.nextULID();
		Assertions.assertNotNull(nextULID);
	}

}
