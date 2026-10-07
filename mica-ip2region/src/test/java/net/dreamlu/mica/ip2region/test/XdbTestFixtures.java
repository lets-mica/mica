package net.dreamlu.mica.ip2region.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import net.dreamlu.mica.ip2region.xdb.Searcher;

final class XdbTestFixtures {

	private XdbTestFixtures() {
	}

	static byte[] emptyDatabase(int ipVersion) {
		byte[] content = new byte[Searcher.HeaderInfoLength
			+ Searcher.VectorIndexRows * Searcher.VectorIndexCols * Searcher.VectorIndexSize];
		content[0] = 3;
		content[16] = (byte) ipVersion;
		content[18] = 4;
		return content;
	}

	static Path writeDatabase(Path directory, int ipVersion) throws IOException {
		return Files.write(directory.resolve("ipv" + ipVersion + ".xdb"), emptyDatabase(ipVersion));
	}
}
