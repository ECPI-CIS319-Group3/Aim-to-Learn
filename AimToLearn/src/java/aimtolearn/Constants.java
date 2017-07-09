package aimtolearn;

import javax.imageio.ImageIO;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class Constants {

	public static final int MAIN_WIDTH = 1600;
	public static final int MAIN_HEIGHT = 900;
	public static final double AR = (double) MAIN_WIDTH / MAIN_HEIGHT;

	/** How many times per second the game will update */
	public static final int TICK_RATE = 100;

	/** Y-coordinate for bottom of the ship */
	public static final int SHIP_Y = 850;

	public static final int FONT_SIZE = 24;
	private static final String FONT_FILE = "PressStart2P-Regular.ttf";

	public static final Font PIXEL_FONT;

	public static final Random RAND = new Random();

	static {

		Font font;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, Constants.class.getResourceAsStream(FONT_FILE)).deriveFont((float) FONT_SIZE);
		}
		catch (FontFormatException | IOException e) {
			font = null;
			System.err.println("Missing font file \"" + FONT_FILE + "\". Quitting game.");
			System.exit(13);
		}

		PIXEL_FONT = font;
	}

	public static BufferedImage getImage(String fileName) {
		try {
			return ImageIO.read(Constants.class.getResource(fileName));
		} catch (IOException e) {
			System.err.println("Failed to load image: \"" + fileName + "\". Quitting game.");
			System.exit(14);
			return null;
		}
	}

	// question length limit is currently 52
//	public static final String QUESTION = "How many states are in the United states of america?";

}
