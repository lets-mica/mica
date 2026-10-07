package net.dreamlu.mica.ip2region.test;

import java.io.ByteArrayInputStream;
import java.nio.file.Path;

import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import net.dreamlu.mica.ip2region.config.Config;
import net.dreamlu.mica.ip2region.config.ConfigBuilder;
import net.dreamlu.mica.ip2region.core.SearcherPool;
import net.dreamlu.mica.ip2region.xdb.Searcher;
import net.dreamlu.mica.ip2region.xdb.XdbException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConfigTest {

	@TempDir
	Path directory;

	@ParameterizedTest
	@ValueSource(ints = {4, 6})
	void streamVersionMismatchReportsXdbException(int actualVersion) {
		int expectedVersion = actualVersion == 4 ? 6 : 4;
		XdbException failure = assertThrows(XdbException.class,
			() -> build(streamBuilder(actualVersion), expectedVersion));
		assertTrue(failure.getMessage().contains("input stream"));
		assertTrue(failure.getMessage().contains("(IPv" + actualVersion + ")"));
		assertTrue(failure.getMessage().contains("IPv" + expectedVersion + " expected"));
	}

	@ParameterizedTest
	@ValueSource(ints = {4, 6})
	void fileVersionMismatchPreservesPathAndVersionDiagnostics(int actualVersion) throws Exception {
		Path file = XdbTestFixtures.writeDatabase(directory, actualVersion);
		int expectedVersion = actualVersion == 4 ? 6 : 4;
		XdbException failure = assertThrows(XdbException.class,
			() -> build(Config.custom().setXdbFile(file.toFile()).setCachePolicy(Config.NoCache),
				expectedVersion));
		assertEquals("ip verison not match: xdb file " + file.toFile().getAbsolutePath()
			+ " (IPv" + actualVersion + "), as IPv" + expectedVersion + " expected", failure.getMessage());
	}

	@ParameterizedTest
	@ValueSource(ints = {4, 6})
	void matchingStreamConfigurationRemainsSearchable(int version) throws Exception {
		Config config = build(streamBuilder(version), version);
		assertNull(config.xdbFile);
		assertSearchable(config, version);
	}

	@ParameterizedTest
	@ValueSource(ints = {4, 6})
	void matchingFileConfigurationRemainsSearchable(int version) throws Exception {
		Path file = XdbTestFixtures.writeDatabase(directory, version);
		Config config = build(Config.custom().setXdbFile(file.toFile()).setCachePolicy(Config.NoCache), version);
		assertEquals(file.toFile(), config.xdbFile);
		assertSearchable(config, version);
	}

	private static ConfigBuilder streamBuilder(int version) {
		return Config.custom()
			.setXdbInputStream(new ByteArrayInputStream(XdbTestFixtures.emptyDatabase(version)))
			.setCachePolicy(Config.BufferCache);
	}

	private static Config build(ConfigBuilder builder, int version) throws Exception {
		return version == 4 ? builder.asV4() : builder.asV6();
	}

	private static void assertSearchable(Config config, int version) throws Exception {
		SearcherPool pool = SearcherPool.create(config);
		Searcher searcher = pool.borrowSearcher();
		try {
			assertEquals("", searcher.search(new byte[version == 4 ? 4 : 16]));
		} finally {
			pool.returnSearcher(searcher);
			pool.close();
		}
	}
}
