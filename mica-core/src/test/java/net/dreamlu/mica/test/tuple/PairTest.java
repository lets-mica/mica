package net.dreamlu.mica.test.tuple;

import net.dreamlu.mica.core.tuple.Pair;
import net.dreamlu.mica.core.utils.JsonUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Pair 测试
 *
 * @author L.cm
 */
class PairTest {

	@Test
	void test() {
		String json = JsonUtil.toJson(Pair.create(123, "abc"));
		Assertions.assertNotNull(JsonUtil.readValue(json, Pair.class));
	}

}
