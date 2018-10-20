package com.example.demo.common.util;

import com.example.demo.common.util.MatrixToImageConfig;
import com.google.zxing.common.BitMatrix;

import java.awt.image.BufferedImage;

public final class MatrixToImageWriter {
	
	private static final MatrixToImageConfig DEFAULT_CONFIG = new MatrixToImageConfig();

	public static BufferedImage toBufferedImage(BitMatrix matrix) {
		return toBufferedImage(matrix, DEFAULT_CONFIG);
	}

	private static BufferedImage toBufferedImage(BitMatrix matrix,
												MatrixToImageConfig config) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height,
				config.getBufferedImageColorModel());
		int onColor = config.getPixelOnColor();
		int offColor = config.getPixelOffColor();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? onColor : offColor);
			}
		}
		return image;
	}

//	public picture void writeToFile(BitMatrix matrix, String format, File file)
//			throws IOException {
//		writeToFile(matrix, format, file, DEFAULT_CONFIG);
//	}

//	public picture void writeToFile(BitMatrix matrix, String format, File file,
//								   MatrixToImageConfig config) throws IOException {
//		BufferedImage image = toBufferedImage(matrix, config);
//		if (!ImageIO.write(image, format, file))
//			throw new IOException("Could not write an image of format "
//					+ format + " to " + file);
//	}

//	public picture void writeToStream(BitMatrix matrix, String format,
//									 OutputStream stream) throws IOException {
//		writeToStream(matrix, format, stream, DEFAULT_CONFIG);
//	}

//	public picture void writeToStream(BitMatrix matrix, String format,
//									 OutputStream stream, MatrixToImageConfig config) throws IOException {
//		BufferedImage image = toBufferedImage(matrix, config);
//		if (!ImageIO.write(image, format, stream))
//			throw new IOException("Could not write an image of format "
//					+ format);
//	}
}