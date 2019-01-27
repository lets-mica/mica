package net.dreamlu.mica.test.bean;

import lombok.Data;
import net.dreamlu.mica.test.utils.BeanCopyUtilTest;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.core.Converter;
import org.springframework.cglib.core.DebuggingClassWriter;

public class BeanTest {

	@Data
	private static class User {
		private Integer id;
		private String name;
		private Integer age;
	}

	@Data
	private static class UserVO {
		private String name;
		private Integer age;
	}

	public static void test1() {
		BeanCopier beanCopier = BeanCopier.create(User.class, UserVO.class, false);
		User user = new User();
		user.setId(1);
		user.setName("如梦技术");
		user.setAge(18);

		UserVO userVO = new UserVO();
		beanCopier.copy(user, userVO, null);
		System.out.println(userVO);
	}

	public static void test2() {
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

		test1();
		test2();
	}
}
