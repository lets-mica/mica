package net.dreamlu.mica.fonts;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FontsTest {

	public static void main(String[] args) throws IOException, FontFormatException {
		String path = FontsTest.class.getResource("/").getPath().split("build")[0] + "src/main/resources/fonts/";
		String[] fontNames = new String[] {"001.ttf", "002.ttf", "003.ttf", "004.ttf"};
		BufferedImage image = new BufferedImage(1000, 300, BufferedImage.TYPE_INT_RGB);
		// 获取图形上下文
		Graphics2D graphics = image.createGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		// 图形抗锯齿
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// 字体抗锯齿
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, 1000, 300);
		graphics.setColor(Color.BLACK);
		for (int i = 0; i < fontNames.length; i++) {
			String fontName = fontNames[i];
			Font font = Font.createFont(Font.TRUETYPE_FONT, new File(path + fontName));
			graphics.setFont(font.deriveFont(Font.BOLD, 20F));
			graphics.drawString("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+-×=?", 20, 50 * (i + 1));
		}
		ImageIO.write(image, "JPEG", new FileImageOutputStream(new File("C:\\Users\\dream.lu\\Desktop\\test\\1.jpg")));
	}
}
