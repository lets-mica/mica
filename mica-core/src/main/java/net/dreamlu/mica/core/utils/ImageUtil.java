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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * image 工具
 *
 * @author L.cm
 */
@UtilityClass
public class ImageUtil {

	/**
	 * 读取图片
	 *
	 * @param input 图片文件
	 * @return BufferedImage
	 */
	public static BufferedImage read(File input) {
		try {
			return ImageIO.read(input);
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 读取图片
	 *
	 * @param input 图片文件流
	 * @return BufferedImage
	 */
	public static BufferedImage read(InputStream input) {
		try {
			return ImageIO.read(input);
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 读取图片
	 *
	 * @param url 图片链接地址
	 * @return BufferedImage
	 */
	public static BufferedImage read(String url) {
		try {
			return ImageIO.read(new URL(url));
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 读取图片
	 *
	 * @param url 图片链接地址
	 * @return BufferedImage
	 */
	public static BufferedImage read(URL url) {
		try {
			return ImageIO.read(url);
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

}
