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

import org.springframework.core.io.*;
import org.springframework.util.Assert;

import java.io.IOException;

/**
 * 资源工具类
 *
 * @author L.cm
 */
public class ResourceUtil extends org.springframework.util.ResourceUtils {
	public static final String FTP_URL_PREFIX = "ftp:";

	/**
	 * 获取资源
	 * <p>
	 * 支持一下协议：
	 * <p>
	 * 1. classpath:
	 * 2. file:
	 * 3. ftp:
	 * 4. http: and https:
	 * 6. C:/dir1/ and /Users/lcm
	 * </p>
	 *
	 * @param resourceLocation 资源路径
	 * @return {Resource}
	 * @throws IOException IOException
	 */
	public static Resource getResource(String resourceLocation) throws IOException {
		Assert.notNull(resourceLocation, "Resource location must not be null");
		if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
			String path = resourceLocation.substring(CLASSPATH_URL_PREFIX.length());
			return new ClassPathResource(path);
		}
		if (resourceLocation.startsWith(FTP_URL_PREFIX)) {
			return new UrlResource(resourceLocation);
		}
		if (StringUtil.isHttpUrl(resourceLocation)) {
			return new UrlResource(resourceLocation);
		}
		if (resourceLocation.startsWith(FILE_URL_PREFIX)) {
			return new FileUrlResource(resourceLocation);
		}
		return new FileSystemResource(resourceLocation);
	}

	/**
	 * 读取资源文件为字符串
	 *
	 * @param resourceLocation 资源文件地址
	 * @return 字符串
	 */
	public static String getAsString(String resourceLocation) {
		try {
			Resource resource = getResource(resourceLocation);
			return IoUtil.readToString(resource.getInputStream());
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

}
