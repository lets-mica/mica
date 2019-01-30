package net.dreamlu.mica.test.bean;

import net.dreamlu.mica.core.utils.BeanUtil;
import net.dreamlu.mica.test.utils.BeanCopyUtilTest;
import net.dreamlu.mica.test.utils.User;
import net.dreamlu.mica.test.utils.User1;
import org.springframework.cglib.core.DebuggingClassWriter;

import java.util.HashMap;
import java.util.Map;

/**
 * 调试 cglib map -> bean，详见{@link net.dreamlu.mica.test.utils.AsmTest}
 *
 * @author L.cm
 */
public class MapToBeanTest {

	public static void main(String[] args) {
		// 设置 cglib 源码生成目录
		String sourcePath = BeanCopyUtilTest.class.getResource("/").getPath().split("mica-core")[0];
		System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, sourcePath + "gen_code");

		Map<String, Object> map = new HashMap<>();
		map.put("id", 1);
		map.put("name", "张三");
		map.put("six", "女");
		map.put("gender", "男");
		map.put("xx", "xx");
		map.put("xInt", 100);
		map.put("xxInt", 101);
		map.put("xLong", 10000L);

		User1 user1 = BeanUtil.copy(map, User1.class);
		System.out.println(user1);
		System.out.println(BeanUtil.toMap(user1));

		User1 userx = new User1();
		BeanUtil.copy(user1, userx);
		System.out.println(userx);

		User1 user2 = BeanUtil.copyWithConvert(map, User1.class);
		System.out.println(user2);

		User user3 = BeanUtil.copy(map, User.class);
		System.out.println(user3);

		User user4 = BeanUtil.copyWithConvert(map, User.class);
		System.out.println(user4);

		User user5 = BeanUtil.copy(user2, User.class);
		System.out.println(user5);
	}
}
