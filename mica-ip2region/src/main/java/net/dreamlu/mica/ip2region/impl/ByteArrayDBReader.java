package net.dreamlu.mica.ip2region.impl;

import net.dreamlu.mica.ip2region.core.DBReader;

import java.io.IOException;

public class ByteArrayDBReader implements DBReader {
	protected byte[] buf;

	protected long pos;

	public ByteArrayDBReader(byte[] buf) {
		this.buf = buf;
	}

	public byte[] full() throws IOException {
		return buf;
	}

	public void readFully(long pos, byte[] buf, int offset, int length) throws IOException {
		System.arraycopy(this.buf, (int) pos, buf, offset, length);
	}

	public void close() throws IOException {
		// nop
	}

}
