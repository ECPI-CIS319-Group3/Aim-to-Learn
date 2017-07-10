package aimtolearn.screens;

import aimtolearn.Constants;
import aimtolearn.Game;
import aimtolearn.sprites.Ship;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static aimtolearn.Constants.*;
import static java.awt.event.KeyEvent.*;

/**
 * The main screen containing a controllable ship at the bottom and nothing else.
 */
public class MainScreen extends GamePanel {

	protected final Ship ship;
	protected final List<Rectangle> shots = new CopyOnWriteArrayList<>();

	private final GameLoop loop;
	private long lastShotTime = 0;

	private Map<Integer, Boolean> activeKeys = new HashMap<>();

	private final int[] RIGHT_KEYS = {VK_RIGHT, VK_D};
	private final int[] LEFT_KEYS = {VK_LEFT, VK_A};
	private final int[] FIRE_KEYS = {VK_UP, VK_W};

	private static final Dimension SHOT_SIZE = new Dimension(10, 40);

	private static final int SHIP_SPEED = 5, SHOT_SPEED = 10, FIRE_DELAY = 500,
		LEFT_BOUND = Ship.WIDTH / 2, RIGHT_BOUND = MAIN_WIDTH - LEFT_BOUND;

	protected MainScreen(Game game) {
		super(game);

		this.ship = new Ship(MAIN_WIDTH / 2);

		this.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				activeKeys.put(e.getKeyCode(), true);

				if (e.getKeyCode() == VK_ESCAPE)
					game.quit(); // TODO temporary
			}
			public void keyReleased(KeyEvent e) {
				activeKeys.put(e.getKeyCode(), false);
			}
		});

		this.loop = new GameLoop();

	}

	public void startLoop() {
		loop.start();
	}

	public void stopLoop() {
		loop.stop();
	}

	private boolean keyDown(int... keyNumbers) {
		for (int key : keyNumbers) {
			if (activeKeys.getOrDefault(key, false))
				return true;
		}
		return false;
	}

	@Override
	protected void updateScreen(Graphics graphics) {
		Graphics2D g = ((Graphics2D) graphics);
		for (Rectangle shotLoc : shots) g.fill(shotLoc);
		ship.draw(g);
	}

	/**
	 * Called every game tick to update positions and such
	 */
	protected void tick() {
		ship.tick();

		// prevent both left and right from being held down together
		if (!(keyDown(RIGHT_KEYS) && keyDown(LEFT_KEYS))) {

			int shipX = ship.getX();

			if (keyDown(RIGHT_KEYS)) { // if right is down, move right
				shipX += SHIP_SPEED;
				ship.setDirection(Ship.DIR_RIGHT);
			}
			else if (keyDown(LEFT_KEYS)) { // if left is down, move left
				shipX -= SHIP_SPEED;
				ship.setDirection(Ship.DIR_LEFT);
			}
			else {
				ship.setDirection(Ship.DIR_NONE);
			}

			if (shipX > RIGHT_BOUND) shipX = RIGHT_BOUND;
			if (shipX < LEFT_BOUND) shipX = LEFT_BOUND;

			ship.setX(shipX);
		}

		if (keyDown(FIRE_KEYS)) {
			if (System.currentTimeMillis() - lastShotTime >= FIRE_DELAY) {// auto-fire every [x]ms
				fireShot();
				this.lastShotTime = System.currentTimeMillis();
			}
		}

		for (Rectangle shotLoc : shots) {
			if (shotLoc.getY() < 0)
				shots.remove(shotLoc);
			else
				shotLoc.translate(0, -SHOT_SPEED);
		}
	}

	private void fireShot() {
		int x = (int) (ship.getX() - SHOT_SIZE.getWidth() / 2);
		int y = SHIP_Y - Ship.HEIGHT / 2 - SHOT_SIZE.height;

		shots.add(new Rectangle(new Point(x, y), SHOT_SIZE));
	}

	private class GameLoop implements Runnable {

		private boolean running, started;
		private Thread thread;

		GameLoop() {
			this.running = false;
			this.started = false;
			this.thread = new Thread(this,
				this.getClass().getSimpleName() + " for " + MainScreen.this.getClass().getSimpleName());
		}

		public void start() {
			this.running = true;
			if (!started) {
				thread.start();
				this.started = true;
			}
		}

		public void stop() {
			this.running = false;
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
