package net.dreamlu.mica.ip2region.test;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import net.dreamlu.mica.ip2region.config.Config;
import net.dreamlu.mica.ip2region.config.ConfigBuilder;
import net.dreamlu.mica.ip2region.core.Ip2Region;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Timeout(10)
class Ip2RegionLifecycleTest {

	@TempDir
	Path directory;

	@ParameterizedTest
	@ValueSource(ints = {Config.NoCache, Config.VIndexCache, Config.BufferCache})
	void fileDatabasesSupportDualStackLookupAndShutdown(int cachePolicy) throws Exception {
		Path v4 = Files.write(directory.resolve("v4.xdb"),
			XdbTestFixtures.databaseWithRegion(4, "IPv4 region"));
		Path v6 = Files.write(directory.resolve("v6.xdb"),
			XdbTestFixtures.databaseWithRegion(6, "IPv6 region"));
		Config v4Config = Config.custom().setXdbFile(v4.toFile()).setCachePolicy(cachePolicy).asV4();
		Config v6Config = Config.custom().setXdbFile(v6.toFile()).setCachePolicy(cachePolicy).asV6();
		assertDualStackLookupAndShutdown(v4Config, v6Config);
	}

	@Test
	void streamDatabasesSupportDualStackLookupAndShutdown() throws Exception {
		Config v4Config = streamBuilder(4, "IPv4 region").asV4();
		Config v6Config = streamBuilder(6, "IPv6 region").asV6();
		assertDualStackLookupAndShutdown(v4Config, v6Config);
	}

	private static ConfigBuilder streamBuilder(int version, String region) {
		return Config.custom().setCachePolicy(Config.BufferCache)
			.setXdbInputStream(new ByteArrayInputStream(XdbTestFixtures.databaseWithRegion(version, region)));
	}

	private static void assertDualStackLookupAndShutdown(Config v4, Config v6) throws Exception {
		Ip2Region region = Ip2Region.create(v4, v6);
		try {
			assertEquals("IPv4 region", region.search("1.2.3.4"));
			assertEquals("IPv6 region", region.search("2001:db8::1"));
			assertEquals("IPv4 region", region.search("255.255.255.255"));
			assertEquals("IPv6 region", region.search("::1"));
		} finally {
			region.close(0);
		}
	}
}
