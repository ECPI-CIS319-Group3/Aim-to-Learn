package aimtolearn.sprites;

import aimtolearn.Constants;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AnimatedSprite {

	private static final Map<String, Point> OFFSETS = new HashMap<>();

	static {
		try {
		//	List<String> lines = Files.readAllLines(Paths.get(Constants.class.getResource("offsets.txt").toURI()));
			Scanner linesScanner = new Scanner(Constants.class.getResourceAsStream("offsets.txt"));
			while (linesScanner.hasNext()) {
				String line = linesScanner.nextLine();
				String[] parts = line.split(" ");
				String name = parts[0];
				int x = Integer.parseInt(parts[1]);
				int y = Integer.parseInt(parts[2]);
				OFFSETS.put(name, new Point(x, y));
			}
		}
		catch (/*IOException | URISyntaxException | FileSystemNotFoundException |*/ NumberFormatException e) {
			System.err.println("Failed to load or parse animation offset file. Quitting game.");
			System.exit(15);
		}
	}

	private Image[] frames;
	private final Point offset;
	private final int frameCount, frameTime, totalDuration;

	private boolean running;
	private int currentFrame;
	private long startTime, lastUpdateTime = 0;

	// create a one-time animation
	public AnimatedSprite(String imgName, int frameCount, int totalDuration) {
		this(imgName, frameCount, totalDuration / frameCount, totalDuration);
	}

	public AnimatedSprite(String imgName, int frameCount, int frameTime, int totalDuration) {

		BufferedImage spriteSheet = Constants.getImage(imgName + ".png");
		int frameHeight = spriteSheet.getHeight(null);
		int frameWidth = spriteSheet.getWidth(null) / frameCount;
		this.frames = new Image[frameCount];
		for (int i=0; i < frameCount; i++) this.frames[i] = spriteSheet.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);

		this.offset = OFFSETS.getOrDefault(imgName, new Point(0, 0));
		this.frameCount = frameCount;
		this.frameTime = frameTime;
		this.totalDuration = totalDuration;

	}

	public void start() {
		this.startTime = System.currentTimeMillis();
		this.currentFrame = 0;
		this.running = true;
	}

	public boolean isRunning() {
		return running;
	}

	public void tick() {

		long t = System.currentTimeMillis();

		if (t - startTime >= totalDuration) { // we passed the total duration, so stop
			this.running = false;
		}

		if (running && t - lastUpdateTime >= frameTime) { // each frame step
			this.currentFrame = currentFrame >= frameCount-1 ? 0 : currentFrame + 1;
			this.lastUpdateTime = t;
		}
	}

	public void draw(Graphics g, int x, int y) {
		g.drawImage(frames[currentFrame], x + offset.x, y + offset.y, null);
	}

}
