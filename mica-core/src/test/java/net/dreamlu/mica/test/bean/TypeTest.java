package net.dreamlu.mica.test.bean;

import net.dreamlu.mica.core.utils.ClassUtil;

import java.util.Arrays;
import java.util.stream.Stream;

public class TypeTest {
	public static void main(String[] args) {
		Object a = new int[]{123123};
		System.out.println(a instanceof int[]);
		System.out.println(int[].class.getName());
		System.out.println(Integer[].class.getName());
		System.out.println(ClassUtil.isAssignableValue(Integer[].class, a));
//		System.out.println((Integer[])a);

		Stream<Integer> boxed = Arrays.stream((int[]) a).boxed();
		System.out.println(boxed.toArray(Integer[]::new));

		Object aa = new Integer[]{123123};
		Stream<Integer> stream = Arrays.stream((Integer[]) aa);
		Object[] objects = stream.unordered().toArray();
		System.out.println((int[])aa);
	}
}
