/*
 * Copyright (c) 2019-2029, Dreamlu 卢春梦 (596392912@qq.com & www.dreamlu.net).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.dreamlu.mica.ip2region.core;

/**
 * 基于 zxipdb-java 简化改
 *
 * @author L.cm
 */
public class IpAddress {
    private byte[] mIp;

    public IpAddress(String ip) {
		mIp = textToNumericFormatV6(ip);
        if (mIp == null) {
			mIp = textToNumericFormatV4(ip);
        }
        if (mIp == null) {
            throw new IllegalArgumentException("invalid ip address `" + ip + "`");
        }
    }

    private IpAddress(byte[] mIp) {
        this.mIp = mIp;
    }

    public static IpAddress fromBytesV6(byte[] b) {
        if (b.length == INADDR16SZ) {
            return new IpAddress(b);
        } else if (b.length < INADDR16SZ) {
            byte[] bs = new byte[INADDR16SZ];
            System.arraycopy(b, 0, bs, 0, b.length);
            return new IpAddress(bs);
        } else {
            throw new IllegalArgumentException("非法IP地址");
        }
    }

    public static IpAddress fromBytesV6LE(byte[] b) {
        byte[] be = bsWap(b);
        return fromBytesV6(be);
    }

    public byte[] getBytes() {
        return mIp;
    }

    @Override
    public String toString() {
        if (mIp.length == INADDR4SZ) {
            return numericToTextFormatV4(mIp);
        } else {
            return numericToTextFormatV6(mIp);
        }
    }

    public int compareTo(IpAddress anIp) {
        return compareUnsigned(mIp, anIp.getBytes());
    }

	public static int compareUnsigned(byte[] a, byte[] b) {
		if (a == b) {
			return 0;
		}
		if (a == null || b == null) {
			return a == null ? -1 : 1;
		}
		int i = mismatch(a, b, Math.min(a.length, b.length));
		if (i >= 0) {
			return Byte.toUnsignedInt(a[i]) - Byte.toUnsignedInt(b[i]);
		}
		return a.length - b.length;
	}

	public static int mismatch(byte[] a, byte[] b, int length) {
		int i = 0;
		for (; i < length; i++) {
			if (a[i] != b[i])
				return i;
		}
		return -1;
	}

    private static byte[] bsWap(byte[] b) {
        byte[] rev = new byte[b.length];
        for (int i = 0, j = b.length - 1; j >= 0; i++, j--) {
            rev[j] = b[i];
        }
        return rev;
    }

    private static final int INADDR4SZ = 4;
    private static final int INADDR16SZ = 16;
    private static final int INT16SZ = 2;

    /*
     * Converts IPv4 address in its textual presentation form
     * into its numeric binary form.
     *
     * @param src a String representing an IPv4 address in standard format
     * @return a byte array representing the IPv4 numeric address
     */
    private static byte[] textToNumericFormatV4(String src) {
        int len = src.length();
        if (len == 0 || len > 15) {
            return null;
        }
        if (src.indexOf('.') < 1) {
            return null;
        }
        byte[] res = new byte[INADDR4SZ];
        long tmpValue = 0;
        int currByte = 0;
        boolean newOctet = true;
        for (int i = 0; i < len; i++) {
            char c = src.charAt(i);
            if (c == '.') {
                if (newOctet || tmpValue < 0 || tmpValue > 0xff || currByte == 3) {
                    return null;
                }
                res[currByte++] = (byte) (tmpValue & 0xff);
                tmpValue = 0;
                newOctet = true;
            } else {
                int digit = Character.digit(c, 10);
                if (digit < 0) {
                    return null;
                }
                tmpValue *= 10;
                tmpValue += digit;
                newOctet = false;
            }
        }
        if (newOctet || tmpValue < 0 || tmpValue >= (1L << ((4 - currByte) * 8))) {
            return null;
        }
        switch (currByte) {
            case 0:
                res[0] = (byte) ((tmpValue >> 24) & 0xff);
            case 1:
                res[1] = (byte) ((tmpValue >> 16) & 0xff);
            case 2:
                res[2] = (byte) ((tmpValue >> 8) & 0xff);
            case 3:
                res[3] = (byte) ((tmpValue) & 0xff);
        }
        return res;
    }

    /*
     * Convert IPv6 presentation level address to network order binary form.
     * credit:
     *  Converted from C code from Solaris 8 (inet_pton)
     *
     * Any component of the string following a per-cent % is ignored.
     *
     * @param src a String representing an IPv6 address in textual format
     * @return a byte array representing the IPv6 numeric address
     */
    private byte[] textToNumericFormatV6(String src) {
        // Shortest valid string is "::", hence at least 2 chars
        if (src.length() < 2) {
            return null;
        }
        int colonp;
        char ch;
        boolean sawXdigit;
        int val;
        char[] srcb = src.toCharArray();
        byte[] dst = new byte[INADDR16SZ];

        int srcbLength = srcb.length;
        int pc = src.indexOf('%');
        if (pc == srcbLength - 1) {
            return null;
        }
        if (pc != -1) {
            srcbLength = pc;
        }
        colonp = -1;
        int i = 0;
        int j = 0;
        /* Leading :: requires some special handling. */
        if (srcb[i] == ':' && (srcb[++i] != ':')) {
            return null;
        }
        int curtok = i;
        sawXdigit = false;
        val = 0;
        while (i < srcbLength) {
            ch = srcb[i++];
            int chval = Character.digit(ch, 16);
            if (chval != -1) {
                val <<= 4;
                val |= chval;
                if (val > 0xffff) {
                    return null;
                }
                sawXdigit = true;
                continue;
            }
            if (ch == ':') {
                curtok = i;
                if (!sawXdigit) {
                    if (colonp != -1) {
                        return null;
                    }
                    colonp = j;
                    continue;
                } else if (i == srcbLength) {
                    return null;
                }
                if (j + INT16SZ > INADDR16SZ) {
                    return null;
                }
                dst[j++] = (byte) ((val >> 8) & 0xff);
                dst[j++] = (byte) (val & 0xff);
                sawXdigit = false;
                val = 0;
                continue;
            }
            if (ch == '.' && ((j + INADDR4SZ) <= INADDR16SZ)) {
                String ia4 = src.substring(curtok, srcbLength);
                /* check this IPv4 address has 3 dots, ie. A.B.C.D */
                int dotCount = 0;
                int index = 0;
                while ((index = ia4.indexOf('.', index)) != -1) {
                    dotCount++;
                    index++;
                }
                if (dotCount != 3) {
                    return null;
                }
                byte[] v4Addr = textToNumericFormatV4(ia4);
                if (v4Addr == null) {
                    return null;
                }
                for (int k = 0; k < INADDR4SZ; k++) {
                    dst[j++] = v4Addr[k];
                }
                sawXdigit = false;
                break;
            }
            return null;
        }
        if (sawXdigit) {
            if (j + INT16SZ > INADDR16SZ)
                return null;
            dst[j++] = (byte) ((val >> 8) & 0xff);
            dst[j++] = (byte) (val & 0xff);
        }

        if (colonp != -1) {
            int n = j - colonp;
            if (j == INADDR16SZ) {
				return null;
			}
            for (i = 1; i <= n; i++) {
                dst[INADDR16SZ - i] = dst[colonp + n - i];
                dst[colonp + n - i] = 0;
            }
            j = INADDR16SZ;
        }
        if (j != INADDR16SZ) {
			return null;
		}
        mIp = dst;
        return mIp;
    }

    /**
     * Converts IPv4 binary address into a string suitable for presentation.
     *
     * @param src a byte array representing an IPv4 numeric address
     * @return a String representing the IPv4 address in
     * textual representation format
     */
    private static String numericToTextFormatV4(byte[] src) {
        return (src[0] & 0xff) + "." + (src[1] & 0xff) + "." + (src[2] & 0xff) + "." + (src[3] & 0xff);
    }

    /**
     * Convert IPv6 binary address into presentation (printable) format.
     *
     * @param src a byte array representing the IPv6 numeric address
     * @return a String representing an IPv6 address in
     * textual representation format
     */
    private static String numericToTextFormatV6(byte[] src) {
        StringBuilder sb = new StringBuilder(39);
        for (int i = 0; i < (INADDR16SZ / INT16SZ); i++) {
            sb.append(Integer.toHexString(((src[i << 1] << 8) & 0xff00) | (src[(i << 1) + 1] & 0xff)));
            if (i < (INADDR16SZ / INT16SZ) - 1) {
                sb.append(':');
            }
        }
        return sb.toString();
    }

}
