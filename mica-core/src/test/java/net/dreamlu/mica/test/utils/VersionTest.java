package net.dreamlu.mica.test.utils;

import net.dreamlu.mica.core.utils.Version;
import org.junit.jupiter.api.Test;

class VersionTest {

	@Test
	void test() {
		Version.of(null).incomplete().eq(null);
	}

	public static void main(String[] args) {
		System.out.println();
	}
}
