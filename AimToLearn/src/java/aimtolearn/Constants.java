package aimtolearn;

import javax.imageio.ImageIO;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
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

	public static final float MAIN_FONT = 24, SMALL_FONT = 16, LARGE_FONT = 50;
	private static final String FONT_FILE = "PressStart2P-Regular.ttf";

	public static final Image LOGO_IMAGE = Constants.getImage("logo.png");
	public static final Image SHIP_IMAGE = Constants.getImage("ship.png");

	public static final int SHIP_WIDTH, SHIP_HEIGHT;

	public static final Font PIXEL_FONT;

	public static final Random RAND = new Random();

	static {

		SHIP_WIDTH = SHIP_IMAGE.getWidth(null);
		SHIP_HEIGHT = SHIP_IMAGE.getHeight(null);

		Font font;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, Constants.class.getResourceAsStream(FONT_FILE))
				.deriveFont(MAIN_FONT);
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
