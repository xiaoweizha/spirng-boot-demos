package com.example.demo.common.util;

public final class MatrixToImageConfig {
	
	public static final int BLACK = -16777216;
	public static final int WHITE = -1;
	private final int onColor;
	private final int offColor;

	public MatrixToImageConfig() {
		this(-16777216, -1);
	}

	public MatrixToImageConfig(int onColor, int offColor) {
		this.onColor = onColor;
		this.offColor = offColor;
	}

	public int getPixelOnColor() {
		return this.onColor;
	}

	public int getPixelOffColor() {
		return this.offColor;
	}

	int getBufferedImageColorModel() {
		return (this.onColor == -16777216) && (this.offColor == -1) ? 12 : 1;
	}
	
}
