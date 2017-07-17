package aimtolearn;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * Class filled with misc. constant values used throughout the program
 */
public class Constants {

	// resolution constants
	public static final int MAIN_WIDTH = 1600;
	public static final int MAIN_HEIGHT = 900;
	public static final double AR = (double) MAIN_WIDTH / MAIN_HEIGHT;

	/** How many times per second the game will update */
	public static final int TICK_RATE = 100;

	/** Y-coordinate for bottom of the ship */
	public static final int SHIP_Y = 850;

	public static final Stroke MAIN_STROKE = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	public static final int[] HEIGHTS = {720, 900, 1080};
	public static final int MULTILINE_SPACING = 20;
	public static final float MAIN_FONT = 24, SMALL_FONT = 16, LARGE_FONT = 50;
	private static final String FONT_FILE = "PressStart2P-Regular.ttf";
	public static final Image SHIP_IMAGE = Constants.getImage("ship.png");
	public static final int SHIP_WIDTH, SHIP_HEIGHT;
	public static final Font PIXEL_FONT;
	public static final Random RAND = new Random();
	private static final String IMG_DIR = "img/", WAV_DIR = "wav/";

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

	/**
	 * Load an image file from the resource folder
	 */
	public static BufferedImage getImage(String fileName) {
		fileName = IMG_DIR + fileName;
		try {
			return ImageIO.read(Constants.class.getResource(fileName));
		} catch (IOException e) {
			System.err.println("Failed to load image: \"" + fileName + "\". Quitting game.");
			System.exit(14);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Load a sound file from the resouce folder
	 */
	public static Clip getSound(String fileName) {
		fileName = WAV_DIR + fileName;
		try (AudioInputStream in = AudioSystem.getAudioInputStream(Constants.class.getResource(fileName))) {
			Clip clip = AudioSystem.getClip();
			clip.open(in);
			return clip;
		} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
			System.err.println("Failed to load sound file: \"" + fileName + "\". Quitting Game.");
			System.exit(12);
			throw new RuntimeException(e);
		}
	}

}
