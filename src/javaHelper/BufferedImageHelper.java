package javaHelper;
import java.awt.image.BufferedImage;


public class BufferedImageHelper {
	public static BufferedImage newBufferdImage(int width, int height) {
		String fileName = Thread.currentThread().getStackTrace()[2].getFileName();
		int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();
		BufferedImage ret = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		System.out.printf("new %d x %d BufferImage on line %d from file %s%n", width, height, lineNumber, fileName);
		return ret;
	}
}
