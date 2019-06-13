package net.dreamlu.mica.test.utils;

import net.dreamlu.mica.core.beans.MicaBeanCopier;
import net.dreamlu.mica.core.convert.MicaConverter;
import net.dreamlu.mica.core.utils.BeanUtil;
import org.springframework.cglib.core.DebuggingClassWriter;

import java.util.HashMap;
import java.util.Map;

public class BeanCopyUtilTest {

	public static <T> T copy(Object source, Class<T> targetClazz) {
		MicaBeanCopier copier = MicaBeanCopier.create(source.getClass(), targetClazz, false, false);
		T to = BeanUtil.newInstance(targetClazz);
		copier.copy(source, to, null);
		return to;
	}

	public static <T> T copyWithConvert(Object source, Class<T> targetClazz) {
		Class<?> sourceClass = source.getClass();
		MicaBeanCopier copier = MicaBeanCopier.create(source.getClass(), targetClazz, true, false);
		T to = BeanUtil.newInstance(targetClazz);
		copier.copy(source, to, new MicaConverter(sourceClass, targetClazz));
		return to;
	}

	public static void main(String[] args) {
		// 设置 cglib 源码生成目录
		String sourcePath = BeanCopyUtilTest.class.getResource("/").getPath().split("mica-core")[0];
		System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, sourcePath + "gen_code");

		User1 user1 = new User1();
		user1.setId("1");
		user1.setName("张三");

		User user = new User();
		user.setXx("123123");
		user.setPhoto("www.dreamlu.net/img/1");

		copy(user1, User.class);
		System.out.println(user);

		User userx = copyWithConvert(user1, User.class);
		System.out.println(userx);

		UserChain userChain = copy(user, UserChain.class);
		System.out.println(userChain);

		Map<String, Object> data = new HashMap<>();
		data.put("id", 1);
		UserChain userChainx = copy(data, UserChain.class);
		System.out.println(userChainx);

		Map<String, Object> data1 = new HashMap<>();
		data1.put("id", 1);
		data1.put("name", 1);
		data1.put("photo", 1);
		data1.put("xx", 1);
		UserChain userChainx1 = copyWithConvert(data1, UserChain.class);
		System.out.println(userChainx1);

		Map<String, Object> data2 = new HashMap<>();
		data2.put("id", 1);
		data2.put("name", "1");
		data2.put("photo", "1");
		data2.put("xx", "1");
		data2.put("a", new int[]{1,2,3});
		data2.put("allowNull", null);

		UserChain userChainxxxx = copy(data2, UserChain.class);
		System.out.println(userChainxxxx);
		Map<String, Object> dataxxxx = BeanUtil.toMap(userChainxxxx);
		System.out.println(dataxxxx);
	}
}
