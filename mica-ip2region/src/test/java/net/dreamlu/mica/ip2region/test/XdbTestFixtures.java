package net.dreamlu.mica.ip2region.test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

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

	static byte[] databaseWithRegion(int ipVersion, String region) {
		byte[] empty = emptyDatabase(ipVersion);
		byte[] data = region.getBytes(StandardCharsets.UTF_8);
		int addressBytes = ipVersion == 4 ? 4 : 16;
		int segmentBytes = addressBytes * 2 + 6;
		int segmentOffset = empty.length;
		int dataOffset = segmentOffset + segmentBytes;
		byte[] content = Arrays.copyOf(empty, dataOffset + data.length);
		ByteBuffer buffer = ByteBuffer.wrap(content).order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt(8, segmentOffset);
		buffer.putInt(12, segmentOffset);
		for (int offset = Searcher.HeaderInfoLength; offset < segmentOffset; offset += 8) {
			buffer.putInt(offset, segmentOffset);
			buffer.putInt(offset + 4, segmentOffset);
		}
		Arrays.fill(content, segmentOffset + addressBytes, segmentOffset + addressBytes * 2, (byte) 0xFF);
		buffer.putShort(segmentOffset + addressBytes * 2, (short) data.length);
		buffer.putInt(segmentOffset + addressBytes * 2 + 2, dataOffset);
		System.arraycopy(data, 0, content, dataOffset, data.length);
		return content;
	}
}
