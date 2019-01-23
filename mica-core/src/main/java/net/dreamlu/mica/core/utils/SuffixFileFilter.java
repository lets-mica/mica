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

package net.dreamlu.mica.core.utils;

import org.springframework.util.Assert;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;

/**
 * 文件后缀过滤器
 *
 * @author L.cm
 */
public class SuffixFileFilter implements FileFilter, Serializable {

	private static final long serialVersionUID = -3389157631240246157L;

	private final String[] suffixes;

	public SuffixFileFilter(final String suffix) {
		Assert.notNull(suffix, "The suffix must not be null");
		this.suffixes = new String[]{suffix};
	}

	public SuffixFileFilter(final String[] suffixes) {
		Assert.notNull(suffixes, "The suffix must not be null");
		this.suffixes = new String[suffixes.length];
		System.arraycopy(suffixes, 0, this.suffixes, 0, suffixes.length);
	}

	@Override
	public boolean accept(File pathname) {
		final String name = pathname.getName();
		for (final String suffix : this.suffixes) {
			if (checkEndsWith(name, suffix)) {
				return true;
			}
		}
		return false;
	}

	private boolean checkEndsWith(final String str, final String end) {
		final int endLen = end.length();
		return str.regionMatches(true, str.length() - endLen, end, 0, endLen);
	}
}
