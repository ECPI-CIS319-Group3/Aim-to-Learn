package aimtolearn;

import javax.imageio.ImageIO;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

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

	private static final String SHIP_FILE_NAME = "ship.png";
	public static final Image SHIP_IMAGE;
	public static final int SHIP_WIDTH, SHIP_HEIGHT;

	static {

		Font font;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File(FONT_FILE)).deriveFont((float) FONT_SIZE);
		}
		catch (FontFormatException | IOException e) {
			font = null;
			System.err.println("Missing font file \"" + FONT_FILE + "\". Quitting game.");
			System.exit(13);
		}

		PIXEL_FONT = font;

		Image shipImage;
		try {
			shipImage = ImageIO.read(new File(SHIP_FILE_NAME));
		}
		catch (IOException e) {
			shipImage = null;
			System.err.println("Missing image file \"" + SHIP_FILE_NAME + "\". Quitting game.");
			System.exit(666);
		}

		SHIP_IMAGE = shipImage;
		SHIP_WIDTH = shipImage.getWidth(null);
		SHIP_HEIGHT = shipImage.getHeight(null);
	}

	// TODO temporary stuff

	public static final String QUESTION = "How many states are in the US?";

	// question length limit is currently 52
//	public static final String QUESTION = "How many states are in the United states of america?";

}
