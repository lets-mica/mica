package net.dreamlu.mica.test.utils;

import net.dreamlu.mica.core.utils.BeanProperty;
import net.dreamlu.mica.core.utils.BeanUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.cglib.core.DebuggingClassWriter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanUtilTest {

	@Test
	public void test1() {
		Map<String, Object> map = new HashMap<>();
		map.put("id", 1);
		map.put("name", "张三");
		map.put("six", "女");

		User user = BeanUtil.toBean(map, User.class);
		System.out.println(user);

		Object userx = BeanUtil.generator(User.class, new BeanProperty("xxxx", Boolean.class));
		System.out.println(userx);

		BeanUtil.setProperty(user, "xx", "xx");
		System.out.println(user);

		Object name = BeanUtil.getProperty(user, "name");
		Assert.assertEquals(name, "张三");
	}

	@Test
	public void test2() {
		Map<String, Object> map = new HashMap<>();
		map.put("id", 1);
		map.put("name", "张三");
		map.put("data", "1,2,3");
		map.put("gender", "女");

		User1 user = BeanUtil.copyWithConvert(map, User1.class);

		System.out.println(user);
		Assert.assertEquals(user.getId(), "1");
	}

	@Test
	public void test3() {
		Map<String, String> map = new HashMap<>();
		map.put("id", "1");
		map.put("idInt", "123");
		map.put("name", "张三");
		map.put("data", "1,2,3");
		map.put("gender", "男");

		User1 user = BeanUtil.copyWithConvert(map, User1.class);

		System.out.println(user);
		Assert.assertEquals(user.getIdInt().intValue(), 123);
	}

	@Test
	public void test4() {
		User1 user1 = new User1();
		user1.setId("1");
		user1.setName("张三");
		user1.setSix("男");

		User userx = BeanUtil.copyWithConvert(user1, User.class);
		System.out.println(userx);

		User user = new User();
		user.setXx("123123");
		user.setPhoto("www.dreamlu.net/img/1");
		BeanUtil.copy(user1, user);
		System.out.println(user);

		Assert.assertNull(user.getId());
		Assert.assertEquals("张三", user.getName());
	}

	@Test
	public void test5() {
		List<User1> list = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			User1 user1 = new User1();
			user1.setId(i + "");
			user1.setName("张三" + i);
			list.add(user1);
		}
		List<User> copy1 = BeanUtil.copy(list, User.class);
		copy1.forEach(System.out::println);
		List<User> copy2 = BeanUtil.copyWithConvert(list, User.class);
		copy2.forEach(System.out::println);
	}

	@Test
	public void test6() {
		User user = new User();
		user.setXx("123123");
		user.setPhoto("www.dreamlu.net/img/1");
		user.setBirthday(LocalDateTime.now());
		User1 user1 = BeanUtil.copyWithConvert(user, User1.class);
		System.out.println(user1);
	}

	public static void main(String[] args) {
		// 设置 cglib 源码生成目录
		String sourcePath = BeanUtilTest.class.getResource("/").getPath().split("mica-core")[0];
		System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, sourcePath + "gen_code");

		User1 user1 = new User1();
		user1.setId("1");
		user1.setId1(11);
		user1.setIds(new Integer[]{1, 2, 3});
		user1.setIdss(new int[]{1, 2, 3, 4, 5, 6});
		user1.setIdx(new long[]{1, 2, 3, 4, 5, 6});
		user1.setName("张三");

		BeanUtil.toMap(user1);

		User user = new User();
		user.setXx("123123");
		user.setPhoto("www.dreamlu.net/img/1");

		BeanUtil.copy(user1, user);
		System.out.println(user);

		User userx = BeanUtil.copyWithConvert(user1, User.class);
		System.out.println(userx);

		User userxx = new User();
		userxx.setXx("123123");
		userxx.setPhoto("www.dreamlu.net/img/1");
		BeanUtil.copyNonNull(user1, userxx);
		System.out.println(userxx);

		User userxxx = BeanUtil.copyWithConvert(user1, User.class);
		System.out.println(userxxx);

		UserChain userChain = BeanUtil.copy(user, UserChain.class);
		System.out.println(userChain);

		Map<String, Object> data = new HashMap<>();
		data.put("id", 1);
		UserChain userChainx = BeanUtil.copy(data, UserChain.class);
		System.out.println(userChainx);

		Map<String, Object> data1 = new HashMap<>();
		data1.put("id", 1);
		data1.put("name", 1);
		data1.put("photo", 1);
		data1.put("xx", 1);
		UserChain userChainx1 = BeanUtil.copyWithConvert(data1, UserChain.class);
		System.out.println(userChainx1);

		Map<String, Object> data2 = new HashMap<>();
		data2.put("id", 1);
		data2.put("name", "1");
		data2.put("photo", "1");
		data2.put("xx", "1");
		data2.put("a", new int[]{1, 2, 3});
		data2.put("allowNull", null);

		UserChain userChainxxxx = BeanUtil.toBean(data2, UserChain.class);
		System.out.println(userChainxxxx);
		Map<String, Object> dataxxxx = BeanUtil.toMap(userChainxxxx);
		System.out.println(dataxxxx);

		UserChain userChainNull = new UserChain();
		userChainNull.setAllowNull(10000);
		BeanUtil.copyNonNull(data2, userChainNull);
		System.out.println(userChainNull);

		UserChain userChainNonNull = new UserChain();
		userChainNonNull.setAllowNull(10000);
		BeanUtil.copy(data2, userChainNonNull);
		System.out.println(userChainNonNull);
	}
}
