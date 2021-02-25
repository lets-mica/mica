package net.dreamlu.mica.test.lombok;

import net.dreamlu.mica.core.utils.BeanUtil;
import net.dreamlu.mica.core.utils.JsonUtil;

import java.util.Map;

public class LombokTest {

	public static void bug1() {
		Map<String, Object> map = BeanUtil.toMap(new TestBean1("123", "hh"));
		// {"CMc":"hh","name":"123"}  正常应该是 cMc
		System.out.println(JsonUtil.toJson(map));
	}

	public static void bug2() {
		Map<String, Object> map = BeanUtil.toMap(new TestBean2("123", "hh"));
		// {"username":"123"}  正常应该还有个驼峰的 userName
		System.out.println(JsonUtil.toJson(map));
	}

	public static void main(String[] args) {
		bug1();
		bug2();
	}
}
