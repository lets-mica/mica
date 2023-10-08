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

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.URI;
import java.net.URL;

/**
 * image 工具
 *
 * @author L.cm
 */
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
	 * 读取图片，http 或者 file 地址
	 *
	 * @param url 图片链接地址
	 * @return BufferedImage
	 */
	public static BufferedImage read(String url) {
		return StringUtil.isHttpUrl(url) ? readUrl(url) : read(new File(url));
	}

	/**
	 * 读取图片
	 *
	 * @param url 图片链接地址
	 * @return BufferedImage
	 */
	private static BufferedImage readUrl(String url) {
		try {
			return ImageIO.read(URI.create(url).toURL());
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

	/**
	 * 写出图片
	 *
	 * @param im         RenderedImage to be written.
	 * @param formatName a String containing the informal name of the format.
	 * @param output     an ImageOutputStream to be written to.
	 * @return false if no appropriate writer is found.
	 */
	public static boolean write(RenderedImage im,
								String formatName,
								ImageOutputStream output) {
		try {
			return ImageIO.write(im, formatName, output);
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 写出图片
	 *
	 * @param im         RenderedImage to be written.
	 * @param formatName a String containing the informal name of the format.
	 * @param output     an ImageOutputStream to be written to.
	 * @return false if no appropriate writer is found.
	 */
	public static boolean write(RenderedImage im,
								String formatName,
								File output) {
		try {
			return ImageIO.write(im, formatName, output);
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 写出图片
	 *
	 * @param im         RenderedImage to be written.
	 * @param formatName a String containing the informal name of the format.
	 * @param output     an ImageOutputStream to be written to.
	 * @return false if no appropriate writer is found.
	 */
	public static boolean write(RenderedImage im,
								String formatName,
								OutputStream output) {
		try {
			return ImageIO.write(im, formatName, output);
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 写出图片为 byte 数组
	 *
	 * @param im         RenderedImage to be written.
	 * @param formatName a String containing the informal name of the format.
	 * @return byte array.
	 */
	public static byte[] writeAsBytes(RenderedImage im, String formatName) {
		try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
			if (ImageIO.write(im, formatName, output)) {
				return output.toByteArray();
			}
			throw new IllegalArgumentException("ImageWriter formatName " + formatName + " writer is null.");
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 写出图片为 InputStream
	 *
	 * @param im         RenderedImage to be written.
	 * @param formatName a String containing the informal name of the format.
	 * @return byte array input stream.
	 */
	public static ByteArrayInputStream writeAsStream(RenderedImage im, String formatName) {
		return new ByteArrayInputStream(writeAsBytes(im, formatName));
	}

}
