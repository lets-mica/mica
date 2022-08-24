package net.dreamlu.mica.test.utils;

import net.dreamlu.mica.core.utils.Unchecked;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

class UncheckedTest {

	@Test
	void test1() {
		Function<Object, Object> function = Unchecked.function((t) -> {
			throw new Exception();
		});
		Assertions.assertThrows(Exception.class, () -> function.apply(null));
	}

	@Test
	void test2() {
		Consumer<Object> consumer = Unchecked.consumer((x) -> {
			throw new Exception();
		});
		Assertions.assertThrows(Exception.class, () -> consumer.accept(null));
	}

	@Test
	void test3() {
		Supplier<Object> supplier = Unchecked.supplier(() -> {
			throw new Exception();
		});
		Assertions.assertThrows(Exception.class, supplier::get);
	}

	@Test
	void test4() {
		Runnable runnable = Unchecked.runnable(() -> {
			throw new Exception();
		});
		Assertions.assertThrows(Exception.class, runnable::run);
	}

}
