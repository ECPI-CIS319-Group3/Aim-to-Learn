package aimtolearn.screens;

import aimtolearn.Game;
import aimtolearn.sprites.Ship;

import java.awt.*;
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
public class MainScreen extends BaseScreen {

	protected final Ship ship;
	protected final List<Rectangle> shots = new CopyOnWriteArrayList<>();

	private long lastShotTime = 0, shotChargeStart = 0;

	private Map<Integer, Boolean> activeKeys = new HashMap<>();

	private final int[] RIGHT_KEYS = {VK_RIGHT, VK_D};
	private final int[] LEFT_KEYS = {VK_LEFT, VK_A};
	private final int[] FIRE_KEYS = {VK_UP, VK_W};

	private static final Dimension SHOT_SIZE = new Dimension(10, 40);

	private static final int SHIP_SPEED = 5, SHOT_SPEED = 15, // FIRE_DELAY = 500, replaced with Ship.SHOT_CHARGE_TIME
		LEFT_BOUND = SHIP_WIDTH / 2, RIGHT_BOUND = MAIN_WIDTH - LEFT_BOUND;

	protected MainScreen(Game game) {
		super(game);
		this.ship = new Ship(MAIN_WIDTH / 2);
	}

	@Override
	protected void onKeyDown(KeyEvent e) {
		activeKeys.put(e.getKeyCode(), true);

		if (e.getKeyCode() == VK_ESCAPE) {
			game.PAUSE_MENU.setResumeScreen(this);
			game.setDisplayPanel(game.PAUSE_MENU);
			game.PAUSE_MENU.reset();
			setActive(false);
		}
	}

	@Override
	protected void onKeyUp(KeyEvent e) {
		activeKeys.put(e.getKeyCode(), false);
	}

	protected void resetKeys() {
		activeKeys.clear();
	}

	private boolean isKeyDown(int... keyNumbers) {
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
	public void tick() {
		ship.tick();

		// prevent both left and right from being held down together
		if (!(isKeyDown(RIGHT_KEYS) && isKeyDown(LEFT_KEYS))) {

			int shipX = ship.getX();

			if (isKeyDown(RIGHT_KEYS)) { // if right is down, move right
				shipX += SHIP_SPEED;
				ship.setDirection(Ship.DIR_RIGHT);
			}
			else if (isKeyDown(LEFT_KEYS)) { // if left is down, move left
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

		if (isKeyDown(FIRE_KEYS)) {
			if (System.currentTimeMillis() - lastShotTime >= Ship.SHOT_CHARGE_TIME) { // auto-fire every [x]ms
				this.shotChargeStart = System.currentTimeMillis();
				ship.setShotCharging(true);
			}
		}

		if (ship.isShotCharging() && System.currentTimeMillis() - shotChargeStart >= Ship.SHOT_CHARGE_TIME) {
			fireShot();
			ship.setShotCharging(false);
			this.lastShotTime = System.currentTimeMillis();
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
		int y = SHIP_Y - SHIP_WIDTH / 2 - SHOT_SIZE.height;

		shots.add(new Rectangle(new Point(x, y), SHOT_SIZE));
	}

	public Ship getShip() {
		return ship;
	}
}
