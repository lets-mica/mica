// Copyright 2022 The Ip2Region Authors. All rights reserved.
// Use of this source code is governed by a Apache2.0-style
// license that can be found in the LICENSE file.

package net.dreamlu.mica.ip2region.xdb;

// Little Endian basic data type decode and encode.
// @Author Lion <chenxin619315@gmail.com>
// @Date   2025/09/10

public class LittleEndian {

	// get an uint32 from a byte array from the specified offset
	public static long getUint32(final byte[] buff, int offset) {
		return (
			((buff[offset++] & 0x000000FFL)) |
				((buff[offset++] << 8) & 0x0000FF00L) |
				((buff[offset++] << 16) & 0x00FF0000L) |
				((buff[offset] << 24) & 0xFF000000L)
		);
	}

	// get an 2 bytes int from a byte array from the specified offset
	public static int getUint16(final byte[] buff, int offset) {
		return (
			((buff[offset++]) & 0x000000FF) |
				((buff[offset] << 8) & 0x0000FF00)
		);
	}

}
