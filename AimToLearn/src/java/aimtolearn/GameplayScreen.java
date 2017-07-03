package aimtolearn;

import javax.swing.SwingConstants;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static aimtolearn.Constants.*;
import static java.awt.event.KeyEvent.*;

public class GameplayScreen extends GamePanel {

	private int shipX;

	private int score, level, round;

	private long lastShotTime = 0;

	private Map<Integer, Boolean> activeKeys = new HashMap<>();
	private final List<Point> shotLocations = new CopyOnWriteArrayList<>();

	private final int[] RIGHT_KEYS = {VK_RIGHT, VK_D};
	private final int[] LEFT_KEYS = {VK_LEFT, VK_A};
	private final int[] FIRE_KEYS = {VK_UP, VK_W};

	private static final Dimension SHOT_SIZE = new Dimension(10, 40);

	private static final int TOP = 150, TOP_MARGIN = 25,
		BOX_WIDTH = 100, TEXT_MARGIN = 10,
		SMALL_FONT = 16, LARGE_FONT = 50;

	private static final int SHIP_SPEED = 10, SHOT_SPEED = 10, AUTOFIRE_DELAY = 500;

	private static final int LEFT_BOUND = SHIP_WIDTH / 2,
		RIGHT_BOUND = MAIN_WIDTH - LEFT_BOUND;

	public GameplayScreen(Game game) {
		super(game);

		this.score = 0;
		this.level = 1;
		this.round = 1;

		this.shipX = MAIN_WIDTH / 2;

		this.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				activeKeys.put(e.getKeyCode(), true);

				if (e.getKeyCode() == VK_ESCAPE) game.quit(); // TODO temporary
			}
			public void keyReleased(KeyEvent e) {
				activeKeys.put(e.getKeyCode(), false);
			}
		});

		GameLoop loop = new GameLoop();
		loop.start();
	}

	@Override
	protected void updateScreen(Graphics graphics) {

		Graphics2D g = ((Graphics2D) graphics);

		// draw the top interface

		Rectangle levelBox = new Rectangle(TOP_MARGIN, TOP_MARGIN, BOX_WIDTH, TOP);
		Rectangle roundBox = new Rectangle((int) (levelBox.getMaxX()), TOP_MARGIN, BOX_WIDTH, TOP);
		Rectangle scoreBox = new Rectangle(MAIN_WIDTH - TOP_MARGIN - 2*BOX_WIDTH, TOP_MARGIN, 2*BOX_WIDTH, TOP);
		Rectangle[] boxes = new Rectangle[]{levelBox, roundBox, scoreBox};

		for (Rectangle box : boxes) {
			g.draw(box);
			box.translate(0, TEXT_MARGIN);
		}

		g.setFont(g.getFont().deriveFont((float) SMALL_FONT));
		Game.text("Level", levelBox, g, SwingConstants.TOP);
		Game.text("Round", roundBox, g, SwingConstants.TOP);
		Game.text("Score", scoreBox, g, SwingConstants.TOP);

		for (Rectangle box : boxes) {
			box.translate(0, SMALL_FONT);
			box.setSize((int) box.getWidth(), (int) box.getHeight() - TEXT_MARGIN - SMALL_FONT);
		}

		g.setFont(g.getFont().deriveFont((float) LARGE_FONT));
		Game.text(""+level, levelBox, g, SwingConstants.CENTER);
		Game.text(""+score, scoreBox, g, SwingConstants.CENTER);
		Game.text(""+round, roundBox, g, SwingConstants.CENTER);

		Rectangle questionBox = new Rectangle(
			(int) (roundBox.getMaxX() + TOP_MARGIN),
			TOP_MARGIN,
			MAIN_WIDTH - 4 * TOP_MARGIN - 4 * BOX_WIDTH,
			TOP
		);

		/*
		Stroke orig = g.getStroke();
		float strokeWidth = ((BasicStroke) orig).getLineWidth();
		Stroke dash = new BasicStroke(strokeWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10f, new float[]{10f}, 0f);
		g.setStroke(dash);
		g.draw(questionBox);
		g.setStroke(orig);
		*/
		g.setFont(g.getFont().deriveFont(((float) FONT_SIZE)));
		Game.text(QUESTION, questionBox, g, SwingConstants.CENTER);

		// draw the shots

		for (Point shotLoc : shotLocations)
			g.fill(new Rectangle(shotLoc, SHOT_SIZE));

		// draw the ship

		int y = SHIP_Y - SHIP_HEIGHT;
		int x = shipX - SHIP_WIDTH / 2;

		g.drawImage(SHIP_IMAGE, x, y, null);

	}

	private boolean keyDown(int... keyNumbers) {
		return Arrays.stream(keyNumbers)
			.anyMatch(key -> activeKeys.getOrDefault(key, false));
	}

	/**
	 * Called every game tick to update positions and such
	 */
	private void tick() {

		// prevent both left and right from being held down together
		if (!(keyDown(RIGHT_KEYS) && keyDown(LEFT_KEYS))) {

			if (keyDown(RIGHT_KEYS)) // if right is down, move right
				this.shipX += SHIP_SPEED;
			else if (keyDown(LEFT_KEYS)) // if left is down, move left
				this.shipX -= SHIP_SPEED;

			if (shipX > RIGHT_BOUND) this.shipX = RIGHT_BOUND;
			if (shipX < LEFT_BOUND) this.shipX = LEFT_BOUND;
		}

		if (keyDown(FIRE_KEYS)) {
			if (System.currentTimeMillis() - lastShotTime >= AUTOFIRE_DELAY) {// auto-fire every [x]ms
				fireShot();
				this.lastShotTime = System.currentTimeMillis();
			}
		}
		else {
			this.lastShotTime = 0;
		}

		for (Point shotLoc : shotLocations) {
			if (shotLoc.getY() < 0) {
				shotLocations.remove(shotLoc);
				score++; // TODO temp
			}
			else
				shotLoc.translate(0, -SHOT_SPEED);
		}

		repaint();
	}

	private void fireShot() {
		int x = (int) (shipX - SHOT_SIZE.getWidth() / 2);
		int y = (int) (SHIP_Y /*- SHIP_HEIGHT / 2*/ - SHOT_SIZE.getHeight());

		shotLocations.add(new Point(x, y));
	}

	private class GameLoop implements Runnable {

		private boolean running;
		private Thread thread;

		GameLoop() {
			this.running = false;
			this.thread = new Thread(this);
		}

		public void start() {
			this.running = true;
			thread.start();
		}

		@Override
		public void run() {

			final int delay = 1000 / Constants.TICK_RATE;
			long lastStartTime, offset, sleepTime;

			lastStartTime = System.currentTimeMillis();

			while (running) {
				tick();

				offset = System.currentTimeMillis() - lastStartTime;
				sleepTime = delay - offset;

				if (sleepTime < 0) sleepTime = 2;

				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					throw new RuntimeException("Game loop interrupted.", e);
				}

				lastStartTime = System.currentTimeMillis();
			}

		}
	}
}
