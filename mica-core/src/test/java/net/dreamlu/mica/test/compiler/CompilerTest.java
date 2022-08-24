package net.dreamlu.mica.test.compiler;

import net.dreamlu.mica.core.compiler.ByteCodeLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * compiler test
 *
 * @author L.cm
 */
class CompilerTest {

	@Test
	void test() throws ClassNotFoundException {
		String className = "Foo";
		String sourceCode = "public class " + className + " {" +
			"    public void bar() {" +
			"        System.out.println(\"Hello from bar !\");" +
			"    }" +
			"}";
		Class<?> clazz = ByteCodeLoader.load(className, sourceCode);
		Assertions.assertEquals(className, clazz.getName());
	}

}
