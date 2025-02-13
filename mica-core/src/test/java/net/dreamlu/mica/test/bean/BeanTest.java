package net.dreamlu.mica.test.bean;

import lombok.Data;
import net.dreamlu.mica.core.utils.BeanProperty;
import net.dreamlu.mica.core.utils.BeanUtil;
import net.dreamlu.mica.test.utils.BeanCopyUtilTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.core.Converter;
import org.springframework.cglib.core.DebuggingClassWriter;
import org.springframework.cglib.core.SpringNamingPolicy;

import java.util.Map;

public class BeanTest {

	@Data
	public static class User {
		private Integer id;
		private String name;
		private Integer age;
	}

	@Data
	public static class UserVO {
		private String name;
		private Integer age;
	}

	@Test
	void test() {
		User user = new User();
		user.setId(1);
		user.setName("如梦技术");
		user.setAge(18);
		UserVO userVO = BeanUtil.copy(user, UserVO.class);
		Assertions.assertNotNull(userVO);
		Assertions.assertEquals("如梦技术", userVO.getName());
		Assertions.assertEquals(18, userVO.getAge());
	}

	@Test
	void testGen() {
		Object genBean = BeanUtil.generator(UserVO.class, new BeanProperty("id", Integer.class));

		User user = new User();
		user.setId(1);
		user.setName("如梦技术");
		user.setAge(18);
		BeanUtil.copy(user, genBean);

		Map<String, Object> map = BeanUtil.toMap(genBean);
		Assertions.assertNotNull(genBean);
		Assertions.assertEquals("如梦技术", map.get("name"));
		Assertions.assertEquals(18, map.get("age"));
		Assertions.assertEquals(1, map.get("id"));
	}

	public static void test0() {
		// 注意：升级到 java17 之后这样是有问题的，需要采用 test1 的方式
		BeanCopier beanCopier = BeanCopier.create(User.class, UserVO.class, false);
		User user = new User();
		user.setId(1);
		user.setName("如梦技术");
		user.setAge(18);

		UserVO userVO = new UserVO();
		beanCopier.copy(user, userVO, null);
		System.out.println(userVO);
	}

	public static void test1() {
		BeanCopier.Generator gen = new BeanCopier.Generator();
		gen.setSource(User.class);
		gen.setTarget(UserVO.class);
		gen.setUseConverter(false);
		gen.setContextClass(BeanCopier.class);
		gen.setNamingPolicy(SpringNamingPolicy.INSTANCE);
		gen.setClassLoader(UserVO.class.getClassLoader());
		BeanCopier beanCopier = gen.create();

		User user = new User();
		user.setId(1);
		user.setName("如梦技术");
		user.setAge(18);

		UserVO userVO = new UserVO();
		beanCopier.copy(user, userVO, null);
		System.out.println(userVO);
	}

	public static void test2() {
		// 升级到 java 17 之后 User、UserVO 不能为 private 的内部内，否者会报错：in unnamed module of loader 'app'
		BeanCopier beanCopier = BeanCopier.create(User.class, UserVO.class, true);
		User user = new User();
		user.setId(1);
		user.setName("如梦技术");
		user.setAge(18);

		UserVO userVO = new UserVO();
		// 此处 Converter 可使用 lambda 简化。
		beanCopier.copy(user, userVO, new Converter() {
			@Override
			public Object convert(Object o, Class aClass, Object o1) {
				return null;
			}
		});
		System.out.println(userVO);
	}

	public static void main(String[] args) {
		// 设置 cglib 源码生成目录
		String sourcePath = BeanCopyUtilTest.class.getResource("/").getPath().split("mica-core")[0];
		System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, sourcePath + "gen_code");

		test0();
		test1();
		test2();
	}
}
