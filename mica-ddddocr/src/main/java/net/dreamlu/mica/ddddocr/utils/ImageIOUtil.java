package net.dreamlu.mica.ddddocr.utils;

import net.dreamlu.mica.ddddocr.exception.OCRException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * 图像I/O和处理工具类
 *
 * @author L.cm
 */
public class ImageIOUtil {

	/**
	 * 获取 base64 图片字节数组
	 *
	 * @param base64Image base64图片字符串
	 * @return 图片字节数组
	 */
	public static BufferedImage loadBase64Image(String base64Image) {
		// 移除base64前缀（如 data:image/png;base64,）
		String base64Data = base64Image;
		int index = base64Image.indexOf(",");
		if (index > 0) {
			base64Data = base64Image.substring(index + 1);
		}
		// 将base64字符串转换为字节数组
		byte[] imageBytes = Base64.getDecoder().decode(base64Data);
		return loadImage(imageBytes);
	}

	/**
	 * 从字节数组加载图像
	 */
	public static BufferedImage loadImage(byte[] imageBytes) {
		try (ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes)) {
			return ImageIO.read(bais);
		} catch (IOException e) {
			throw new OCRException("无法加载图像: " + e.getMessage(), e);
		}
	}

	/**
	 * 从文件路径加载图像
	 */
	public static BufferedImage loadImage(String imagePath) {
		return loadImage(Paths.get(imagePath));
	}

	/**
	 * 从文件路径加载图像（兼容旧版本）
	 */
	public static BufferedImage loadImage(Path imagePath) {
		try {
			return loadImage(Files.readAllBytes(imagePath));
		} catch (IOException e) {
			throw new OCRException("无法从文件加载图像: " + e.getMessage(), e);
		}
	}

	/**
	 * PNG RGBA黑色预处理（处理透明背景）
	 */
	public static BufferedImage pngRgbaBlackPreprocess(BufferedImage image) {
		if (image.getType() != BufferedImage.TYPE_INT_ARGB) {
			return image;
		}

		BufferedImage result = new BufferedImage(
			image.getWidth(),
			image.getHeight(),
			BufferedImage.TYPE_INT_RGB
		);

		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				int argb = image.getRGB(x, y);
				int alpha = (argb >> 24) & 0xFF;

				if (alpha == 0) {
					// 透明区域设为黑色
					result.setRGB(x, y, 0x000000);
				} else {
					// 保留原色
					int rgb = argb & 0x00FFFFFF;
					result.setRGB(x, y, rgb);
				}
			}
		}
		return result;
	}

	/**
	 * 调整图像尺寸
	 */
	public static BufferedImage resizeImage(BufferedImage image, int targetWidth, int targetHeight) {
		BufferedImage resized = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = resized.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(image, 0, 0, targetWidth, targetHeight, null);
		g.dispose();
		return resized;
	}

	/**
	 * 调整图像尺寸（保持宽高比）
	 */
	public static BufferedImage resizeImageKeepAspect(BufferedImage image, int targetHeight) {
		int originalWidth = image.getWidth();
		int originalHeight = image.getHeight();
		int targetWidth = (int) (originalWidth * ((double) targetHeight / originalHeight));
		return resizeImage(image, targetWidth, targetHeight);
	}

	/**
	 * 转换为灰度图
	 */
	public static BufferedImage convertToGrayscale(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage gray = new BufferedImage(
			width,
			height,
			BufferedImage.TYPE_BYTE_GRAY
		);

		// 使用标准的灰度转换公式: gray = 0.299*R + 0.587*G + 0.114*B
		WritableRaster raster = gray.getRaster();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int rgb = image.getRGB(x, y);
				int r = (rgb >> 16) & 0xFF;
				int g = (rgb >> 8) & 0xFF;
				int b = rgb & 0xFF;
				// 计算灰度值
				int grayValue = (int) (0.299 * r + 0.587 * g + 0.114 * b);
				raster.setSample(x, y, 0, grayValue);
			}
		}
		return gray;
	}

	/**
	 * 转换为ONNX输入格式 [1, C, H, W]
	 */
	public static float[][][][] prepareForOnnx(BufferedImage image, int targetHeight) {
		// 调整尺寸
		BufferedImage resized = resizeImageKeepAspect(image, targetHeight);
		// 转换为灰度
		BufferedImage gray = convertToGrayscale(resized);

		// 标准化并转换为[1, C, H, W]格式
		int height = gray.getHeight();
		int width = gray.getWidth();
		float[][][][] result = new float[1][1][height][width];

		// 使用Raster读取像素值，更可靠
		Raster raster = gray.getRaster();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int grayValue;
				if (gray.getType() == BufferedImage.TYPE_BYTE_GRAY) {
					// TYPE_BYTE_GRAY 类型直接读取单个样本
					grayValue = raster.getSample(x, y, 0);
				} else {
					// 其他类型使用RGB方式读取
					int rgb = gray.getRGB(x, y);
					// 取R通道作为灰度值
					grayValue = (rgb >> 16) & 0xFF;
				}
				result[0][0][y][x] = grayValue / 255.0f;
			}
		}
		return result;
	}
}
