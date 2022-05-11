package net.dreamlu.mica.test.tuple;

import net.dreamlu.mica.core.tuple.Pair;
import net.dreamlu.mica.core.utils.JsonUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * Pair 测试
 *
 * @author L.cm
 */
public class PairTest {

	@Test
	public void test() {
		String json = JsonUtil.toJson(Pair.create(123, "abc"));
		Assert.assertNotNull(JsonUtil.readValue(json, Pair.class));
	}

}
