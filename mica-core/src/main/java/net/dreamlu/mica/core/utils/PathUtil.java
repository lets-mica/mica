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

import lombok.experimental.UtilityClass;
import org.springframework.lang.Nullable;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.net.URL;

/**
 * 用来获取各种目录
 *
 * @author L.cm
 */
@UtilityClass
public class PathUtil {

	/**
	 * 获取jar包运行时的当前目录
	 * @return {String}
	 */
	@Nullable
	public static String getJarPath() {
		try {
			URL url = PathUtil.class.getResource(StringPool.SLASH).toURI().toURL();
			return PathUtil.toFilePath(url);
		} catch (Exception e) {
			String path = PathUtil.class.getResource(StringPool.EMPTY).getPath();
			return new File(path).getParentFile().getParentFile().getAbsolutePath();
		}
	}

	@Nullable
	public static String toFilePath(@Nullable URL url) {
		if (url == null) { return null; }
		String protocol = url.getProtocol();
		String file = UrlUtil.decode(url.getPath(), Charsets.UTF_8);
		if (ResourceUtils.URL_PROTOCOL_FILE.equals(protocol)) {
			return new File(file).getParentFile().getParentFile().getAbsolutePath();
		} else if (ResourceUtils.URL_PROTOCOL_JAR.equals(protocol)
			|| ResourceUtils.URL_PROTOCOL_ZIP.equals(protocol)) {
			int ipos = file.indexOf(ResourceUtils.JAR_URL_SEPARATOR);
			if (ipos > 0) {
				file = file.substring(0, ipos);
			}
			if (file.startsWith(ResourceUtils.FILE_URL_PREFIX)) {
				file = file.substring(ResourceUtils.FILE_URL_PREFIX.length());
			}
			return new File(file).getParentFile().getAbsolutePath();
		}
		return file;
	}

}
