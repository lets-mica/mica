package net.dreamlu.mica.test.bean;

import net.dreamlu.mica.core.utils.BeanUtil;

import java.util.ArrayList;
import java.util.List;

public class BeanTest1 {

	public static void main(String[] args) {
		List<Test1> list = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			Test1 test1 = new Test1();
			test1.setId(i);
			test1.setName("name" + i);
			test1.setAge(18 + i);
			list.add(test1);
		}
		Class<?> clazz = Test2.class;
		TestN1 testN1 = new TestN1();
		testN1.setList(list);
		TestN2 testN2 = BeanUtil.copy(testN1, TestN2.class);
		System.out.println(testN2);

//		ResolvableType.forMethodParameter(writeMethod, 0)
//			.getGeneric(0)
//			.resolve()

	}
}
