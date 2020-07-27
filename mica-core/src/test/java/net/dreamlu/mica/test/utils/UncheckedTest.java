package net.dreamlu.mica.test.utils;

import net.dreamlu.mica.core.utils.Unchecked;
import org.junit.Test;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class UncheckedTest {

	@Test(expected = Exception.class)
	public void test1() {
		Function<Object, Object> function = Unchecked.function((t) -> {
			throw new Exception();
		});
		function.apply(null);
	}

	@Test(expected = Exception.class)
	public void test2() {
		Consumer<Object> consumer = Unchecked.consumer((x) -> {
			throw new Exception();
		});
		consumer.accept(null);
	}

	@Test(expected = Exception.class)
	public void test3() {
		Supplier<Object> supplier = Unchecked.supplier(() -> {
			throw new Exception();
		});
		supplier.get();
	}

	@Test(expected = Exception.class)
	public void test4() {
		Runnable runnable = Unchecked.runnable(() -> {
			throw new Exception();
		});
		runnable.run();
	}

}
