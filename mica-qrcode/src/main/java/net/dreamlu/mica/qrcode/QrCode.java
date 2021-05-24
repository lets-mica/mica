/*
 * Copyright 2021 [Nutz & https://nutzam.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package net.dreamlu.mica.qrcode;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import net.dreamlu.mica.core.utils.Base64Util;
import net.dreamlu.mica.core.utils.CharPool;
import net.dreamlu.mica.core.utils.Exceptions;
import net.dreamlu.mica.core.utils.ImageUtil;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Hashtable;

/**
 * QRCode 读写，基于 nutz-plugins-qrcode 调整，简化使用
 *
 * @author ywjno(ywjno.dev @ gmail.com)
 * @author L.cm
 */
public final class QrCode {

	/**
	 * 二维码内容
	 */
	private final String content;
	/**
	 * 图片大小
	 */
	private int size;
	/**
	 * 内容编码格式
	 */
	private Charset encode;
	/**
	 * 错误修正等级 (Error Collection Level)
	 */
	private ErrorCorrectionLevel errorCorrectionLevel;
	/**
	 * 错误修正等级的具体值
	 */
	private double errorCorrectionLevelValue;
	/**
	 * 前景色
	 */
	private Color foreGroundColor;
	/**
	 * 背景色
	 */
	private Color backGroundColor;
	/**
	 * 图片的文件格式
	 */
	private String imageFormat;
	/**
	 * 删除图片的外白边
	 */
	private boolean deleteMargin;
	/**
	 * 提供给编码器额外的参数
	 */
	private final Hashtable<EncodeHintType, Object> hints;
	/**
	 * 需要添加的图片
	 */
	private BufferedImage logo;

	/**
	 * 创建一个带有默认值的 QRCode 生成器的格式。默认值如下
	 *
	 * <ul>
	 * <li>图片大小: 512px</li>
	 * <li>内容编码格式: UTF-8</li>
	 * <li>错误修正等级: Level M (有15% 的内容可被修正)</li>
	 * <li>前景色: 黑色</li>
	 * <li>背景色: 白色</li>
	 * <li>输出图片的文件格式: png</li>
	 * <li>图片空白区域大小: 0个单位</li>
	 * </ul>
	 */
	private QrCode(String content) {
		this.content = content;
		this.size = 512;
		this.encode = StandardCharsets.UTF_8;
		this.errorCorrectionLevel = ErrorCorrectionLevel.M;
		this.errorCorrectionLevelValue = 0.15;
		this.foreGroundColor = Color.BLACK;
		this.backGroundColor = Color.WHITE;
		this.imageFormat = "png";
		this.deleteMargin = true;
		this.hints = new Hashtable<>();
	}

	/**
	 * 使用带默认值的「QRCode 生成器格式」来创建一个 QRCode 处理器。
	 *
	 * @param content 所要生成 QRCode 的内容
	 * @return QRCode
	 */
	public static QrCode form(final String content) {
		return new QrCode(content);
	}

	/**
	 * 设置图片的大小。图片的大小等于实际内容与外边距的值（建议设置成偶数值）。
	 *
	 * @param size 图片的大小
	 * @return QRCode
	 */
	public QrCode size(int size) {
		this.size = size;
		return this;
	}

	/**
	 * 设置内容编码格式。
	 *
	 * @param encode 内容编码格式
	 * @return QRCode
	 */
	public QrCode encode(Charset encode) {
		if (null != encode) {
			this.encode = encode;
		}
		return this;
	}

	/**
	 * 设置错误修正等级。其定义如下
	 *
	 * <ul>
	 * <li>L: 有 7% 的内容可被修正</li>
	 * <li>M: 有15% 的内容可被修正</li>
	 * <li>Q: 有 25% 的内容可被修正</li>
	 * <li>H: 有 30% 的内容可被修正</li>
	 * </ul>
	 *
	 * @param errorCorrectionLevel 错误修正等级
	 * @return QRCode
	 */
	public QrCode errorCorrectionLevel(ErrorCorrectionLevel errorCorrectionLevel) {
		switch (errorCorrectionLevel) {
			case L:
				this.errorCorrectionLevel = errorCorrectionLevel;
				this.errorCorrectionLevelValue = 0.07;
				break;
			case M:
				this.errorCorrectionLevel = errorCorrectionLevel;
				this.errorCorrectionLevelValue = 0.15;
				break;
			case Q:
				this.errorCorrectionLevel = errorCorrectionLevel;
				this.errorCorrectionLevelValue = 0.25;
				break;
			case H:
				this.errorCorrectionLevel = errorCorrectionLevel;
				this.errorCorrectionLevelValue = 0.3;
				break;
			default:
				this.errorCorrectionLevel = ErrorCorrectionLevel.M;
				this.errorCorrectionLevelValue = 0.15;
				break;
		}
		return this;
	}

	/**
	 * 设置前景色。值为十六进制的颜色值（与 CSS 定义颜色的值相同，不支持简写），可以忽略「#」符号。
	 *
	 * @param foreGroundColor 前景色的值
	 * @return QRCode
	 */
	public QrCode foreGroundColor(String foreGroundColor) {
		try {
			this.foreGroundColor = getColor(foreGroundColor);
		} catch (NumberFormatException e) {
			this.foreGroundColor = Color.BLACK;
		}
		return this;
	}

	/**
	 * 设置前景色。
	 *
	 * @param foreGroundColor 前景色的值
	 * @return QRCode
	 */
	public QrCode foreGroundColor(Color foreGroundColor) {
		this.foreGroundColor = foreGroundColor;
		return this;
	}

	/**
	 * 设置背景色。值为十六进制的颜色值（与 CSS 定义颜色的值相同，不支持简写），可以忽略「#」符号。
	 *
	 * @param backGroundColor 前景色的值
	 * @return QRCode
	 */
	public QrCode backGroundColor(String backGroundColor) {
		try {
			this.backGroundColor = getColor(backGroundColor);
		} catch (NumberFormatException e) {
			this.backGroundColor = Color.WHITE;
		}
		return this;
	}

	/**
	 * 设置背景色。
	 *
	 * @param backGroundColor 前景色的值
	 * @return QRCode
	 */
	public QrCode backGroundColor(Color backGroundColor) {
		this.backGroundColor = backGroundColor;
		return this;
	}

	/**
	 * 设置图片的文件格式 。
	 *
	 * @param imageFormat 图片的文件格式
	 * @return QRCode
	 */
	public QrCode imageFormat(String imageFormat) {
		if (imageFormat != null) {
			this.imageFormat = imageFormat.toLowerCase();
		}
		return this;
	}

	/**
	 * 删除白边。
	 *
	 * @param deleteMargin 删除白边
	 * @return QRCode
	 */
	public QrCode deleteMargin(boolean deleteMargin) {
		this.deleteMargin = deleteMargin;
		return this;
	}

	/**
	 * 返回提供给编码器额外的参数。
	 *
	 * @return 提供给编码器额外的参数
	 */
	public Hashtable<EncodeHintType, ?> getHints() {
		hints.clear();
		hints.put(EncodeHintType.ERROR_CORRECTION, this.errorCorrectionLevel);
		hints.put(EncodeHintType.CHARACTER_SET, this.encode);
		hints.put(EncodeHintType.MARGIN, 0);
		return hints;
	}

	/**
	 * 设置添加的图片。
	 *
	 * @param logo 添加的图片
	 * @return QRCode
	 */
	public QrCode logo(BufferedImage logo) {
		this.logo = logo;
		return this;
	}

	/**
	 * 设置添加的图片。
	 *
	 * @param logo 添加的图片
	 * @return QRCode
	 */
	public QrCode logo(File logo) {
		return logo(ImageUtil.read(logo));
	}

	/**
	 * 设置添加的图片。
	 *
	 * @param url 添加的图片
	 * @return QRCode
	 */
	public QrCode logo(URL url) {
		return logo(ImageUtil.read(url));
	}

	/**
	 * 设置添加的图片。
	 *
	 * @param iconPath 添加的图片
	 * @return QRCode
	 */
	public QrCode logo(String iconPath) {
		return logo(ImageUtil.read(iconPath));
	}

	/**
	 * 设置添加的图片。
	 *
	 * @param logoStream 添加的图片流
	 * @return QRCode
	 */
	public QrCode logo(InputStream logoStream) {
		return logo(ImageUtil.read(logoStream));
	}

	/**
	 * 写出二维码
	 * @param output OutputStream
	 * @return 是否成功
	 */
	public boolean write(OutputStream output) {
		BufferedImage bufferedImage = this.toImage();
		return ImageUtil.write(bufferedImage, this.imageFormat, output);
	}

	/**
	 * 把指定的内容生成为一个 QRCode 的图片，之后保存到指定的文件中。
	 *
	 * @param f 指定的文件
	 * @return 文件
	 */
	public File toFile(String f) {
		return toFile(new File(f));
	}

	/**
	 * 把指定的内容生成为一个 QrCode 的图片，之后保存到指定的文件中。
	 *
	 * @param qrCodeFile 指定的文件
	 * @return 文件
	 */
	public File toFile(File qrCodeFile) {
		if (!qrCodeFile.exists()) {
			qrCodeFile.getParentFile().mkdirs();
		}
		BufferedImage bufferedImage = this.toImage();
		ImageUtil.write(bufferedImage, this.imageFormat, qrCodeFile);
		return qrCodeFile;
	}

	/**
	 * 使用带默认值的「QrCode 生成器格式」，把指定的内容生成为一个 QrCode 的 base64 image。
	 *
	 * @return base64 字符串
	 */
	public String toBase64() {
		return "data:image/png;base64," + Base64Util.encodeToString(toBytes());
	}

	/**
	 * 使用带默认值的「QrCode 生成器格式」，把指定的内容生成为一个 QrCode 的 byte 数组。
	 *
	 * @return byte array
	 */
	public byte[] toBytes() {
		BufferedImage bufferedImage = this.toImage();
		return ImageUtil.writeAsBytes(bufferedImage, this.imageFormat);
	}

	/**
	 * 使用带默认值的「QrCode 生成器格式」，把指定的内容生成为一个 QrCode 的流。
	 *
	 * @return QRCode 的图像流
	 */
	public ByteArrayInputStream toStream() {
		BufferedImage bufferedImage = this.toImage();
		return ImageUtil.writeAsStream(bufferedImage, this.imageFormat);
	}

	/**
	 * 使用带默认值的「QrCode 生成器格式」，把指定的内容生成为一个 QrCode 的图像对象。
	 *
	 * @return QRCode 的图像对象
	 */
	public BufferedImage toImage() {
		String text = new String(content.getBytes(this.encode));
		BitMatrix matrix;
		try {
			matrix = new QRCodeWriter().encode(text,
				BarcodeFormat.QR_CODE,
				this.size,
				this.size,
				this.getHints());
		} catch (WriterException e) {
			throw Exceptions.unchecked(e);
		}
		if (this.deleteMargin) {
			matrix = deleteWhite(matrix);
		}
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		int fgColor = this.foreGroundColor.getRGB();
		int bgColor = this.backGroundColor.getRGB();
		BufferedImage image = new BufferedImage(width, height, ColorSpace.TYPE_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? fgColor : bgColor);
			}
		}
		if (null != this.logo) {
			addLogo(image, this.logo, this);
		}
		return image;
	}

	/**
	 * 从指定的 QRCode 图片文件中解析出其内容。
	 *
	 * @param qrCodeFile QRCode 文件
	 * @return QRCode 中的内容
	 */
	public static String read(String qrCodeFile) {
		return read(ImageUtil.read(qrCodeFile));
	}

	/**
	 * 从指定的 QRCode 图片文件中解析出其内容。
	 *
	 * @param qrCodeFile QRCode 图片文件
	 * @return QRCode 中的内容
	 */
	public static String read(File qrCodeFile) {
		return read(ImageUtil.read(qrCodeFile));
	}

	/**
	 * 从指定的 QRCode 图片链接中解析出其内容。
	 *
	 * @param qrCodeUrl QRCode 图片链接
	 * @return QRCode 中的内容
	 */
	public static String read(URL qrCodeUrl) {
		return read(ImageUtil.read(qrCodeUrl));
	}

	/**
	 * 从指定的 QRCode 图像对象中解析出其内容。
	 *
	 * @param qrCodeImage QRCode 图像对象
	 * @return QRCode 中的内容
	 */
	public static String read(BufferedImage qrCodeImage) {
		LuminanceSource source = new BufferedImageLuminanceSource(qrCodeImage);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
		try {
			Result result = new QRCodeReader().decode(bitmap);
			return result.getText();
		} catch (NotFoundException | ChecksumException | FormatException e) {
			throw Exceptions.unchecked(e);
		} finally {
			qrCodeImage.getGraphics().dispose();
		}
	}

	private static void addLogo(BufferedImage qrCodeImage, BufferedImage logoImage, QrCode qrCode) {
		int baseWidth = qrCodeImage.getWidth();
		int baseHeight = qrCodeImage.getHeight();
		// 计算 icon 的最大边长
		// 公式为 二维码面积*错误修正等级*0.4 的开方
		int maxWidth = (int) Math.sqrt(baseWidth
			* baseHeight
			* qrCode.errorCorrectionLevelValue
			* 0.4);
		// 获取 icon 的实际边长
		int logoRectWidth = Math.min(maxWidth, logoImage.getWidth());
		int logoRectHeight = Math.min(maxWidth, logoImage.getHeight());
		// 圆角矩形
		BufferedImage logoRect = new BufferedImage(logoRectWidth,
			logoRectHeight,
			BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = logoRect.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// 画 logo 区域
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, logoRectWidth, logoRectHeight);
		g2.setComposite(AlphaComposite.SrcAtop);
		// 画 灰色 框框 2 px
		g2.setColor(Color.LIGHT_GRAY);
		g2.fillRect(2, 2, logoRectWidth - 4, logoRectHeight - 4);
		g2.setComposite(AlphaComposite.SrcAtop);
		// 画 logo 图
		g2.drawImage(logoImage, 4, 4, logoRectWidth - 8, logoRectHeight - 8, null);
		logoImage.getGraphics().dispose();
		g2.dispose();
		// 将 logo 添加到 二维码上
		Graphics2D gc = (Graphics2D) qrCodeImage.getGraphics();
		gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		gc.setColor(qrCode.backGroundColor);
		gc.drawImage(logoRect,
			(baseWidth - logoRectWidth) / 2,
			(baseHeight - logoRectHeight) / 2,
			null);
		gc.dispose();
	}

	private static Color getColor(String hexString) {
		if (CharPool.HASH == hexString.charAt(0)) {
			return new Color(Long.decode(hexString).intValue());
		} else {
			return new Color(Long.decode("0xFF" + hexString).intValue());
		}
	}

	private static BitMatrix deleteWhite(BitMatrix matrix) {
		int[] rec = matrix.getEnclosingRectangle();
		int resWidth = rec[2] + 1;
		int resHeight = rec[3] + 1;
		BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
		resMatrix.clear();
		for (int i = 0; i < resWidth; i++) {
			for (int j = 0; j < resHeight; j++) {
				if (matrix.get(i + rec[0], j + rec[1])) {
					resMatrix.set(i, j);
				}
			}
		}
		return resMatrix;
	}

}
