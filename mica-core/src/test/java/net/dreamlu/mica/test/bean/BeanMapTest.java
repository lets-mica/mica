package net.dreamlu.mica.test.bean;

import net.dreamlu.mica.core.utils.BeanUtil;
import net.dreamlu.mica.test.utils.User1;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

class BeanMapTest {

	@Test
	void test1() {
		User1 user = new User1();
		user.setId("123");
		user.setName("张三");
		Map<String, Object> map = BeanUtil.toMap(user);
		Assertions.assertNotNull(map);
		Assertions.assertEquals("123", map.get("id"));
		Assertions.assertEquals("张三", map.get("name"));
	}

}
