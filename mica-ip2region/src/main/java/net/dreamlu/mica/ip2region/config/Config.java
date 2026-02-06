// Copyright 2022 The Ip2Region Authors. All rights reserved.
// Use of this source code is governed by a Apache2.0-style
// license that can be found in the LICENSE file.

package net.dreamlu.mica.ip2region.config;

import net.dreamlu.mica.ip2region.xdb.Header;
import net.dreamlu.mica.ip2region.xdb.LongByteArray;
import net.dreamlu.mica.ip2region.xdb.Version;
import net.dreamlu.mica.ip2region.xdb.XdbException;

import java.io.File;

/**
 * ip2region config class
 *
 * @Author Lion <chenxin619315@gmail.com>
 * @Date 2025/11/20
 */
public class Config {
	// cache policy consts
	public static final int NoCache = 0;
	public static final int VIndexCache = 1;
	public static final int BufferCache = 2;

	// search cache policy
	public final int cachePolicy;
	public final Version ipVersion;

	// xdb file path
	public final File xdbFile;
	public final Header header;

	public final byte[] vIndex;
	public final LongByteArray cBuffer;

	public final int searchers;

	// config builder
	public static ConfigBuilder custom() {
		return new ConfigBuilder();
	}

	protected Config(int cachePolicy, Version ipVersion, File xdbFile,
					 Header header, byte[] vIndex, LongByteArray cBuffer, int searchers) throws XdbException {
		this.cachePolicy = cachePolicy;
		this.ipVersion = ipVersion;

		this.xdbFile = xdbFile;
		this.header = header;
		this.vIndex = vIndex;
		this.cBuffer = cBuffer;

		final Version xVersion = Version.fromHeader(header);
		// verify the ip version (ipVersion and the version of the xdb file should be the same)
		if (header.ipVersion != ipVersion.id) {
			throw new XdbException("ip verison not match: xdb file "
				+ xdbFile.getAbsolutePath() + " (" + xVersion.name + "), as " + ipVersion.name + " expected");
		}

		this.searchers = searchers;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append('{');
		sb.append("cache_policy:").append(cachePolicy).append(',');
		sb.append("version:").append(ipVersion.toString()).append(',');
		sb.append("xdb_path:").append(xdbFile == null ? "null" : xdbFile.getAbsolutePath()).append(',');
		sb.append("header:").append(header.toString()).append(',');
		if (vIndex == null) {
			sb.append("v_index: null, ");
		} else {
			sb.append("v_index: {bytes: ").append(vIndex.length).append("},");
		}
		if (cBuffer == null) {
			sb.append("c_buffer: null, ");
		} else {
			sb.append("c_buffer: {bytes: ").append(cBuffer.length()).append("},");
		}
		sb.append("searchers:").append(searchers);
		sb.append('}');
		return sb.toString();
	}

	public static int cachePolicyFromName(String name) throws InvalidConfigException {
		final String lName = name.toLowerCase();
		return switch (lName) {
			case "file", "nocache" -> NoCache;
			case "vectorindex", "vindex", "vindexcache" -> VIndexCache;
			case "content", "buffercache" -> BufferCache;
			default -> throw new InvalidConfigException("invalid cache policy `" + name + "`");
		};
	}
}
