package aimtolearn.screens;

import aimtolearn.Game;
import aimtolearn.Sound;
import aimtolearn.sprites.Ship;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static aimtolearn.Constants.*;
import static java.awt.event.KeyEvent.*;

/**
 * The main screen containing a controllable ship at the bottom and nothing else.
 */
public abstract class ShipScreen extends BaseScreen {

	protected final Ship ship;
	protected final List<Rectangle> shots = new CopyOnWriteArrayList<>();

	private long lastShotTime = 0, shotChargeStart = 0;

	private boolean shootingEnabled, frozen;

	private Map<Integer, Boolean> activeKeys = new HashMap<>();

	protected final List<Integer> RIGHT_KEYS = Arrays.asList(VK_RIGHT, VK_D);
	protected final List<Integer> LEFT_KEYS = Arrays.asList(VK_LEFT, VK_A);
	private final List<Integer> FIRE_KEYS = Arrays.asList(VK_UP, VK_W);

	private static final Dimension SHOT_SIZE = new Dimension(10, 40);

	private static final int SHIP_SPEED = 5, SHOT_SPEED = 15,
		LEFT_BOUND = SHIP_WIDTH / 2, RIGHT_BOUND = MAIN_WIDTH - LEFT_BOUND;

	protected ShipScreen(Game game) {
		super(game);
		this.frozen = false;
		this.shootingEnabled = true;
		this.ship = new Ship(MAIN_WIDTH / 2);
	}

	@Override
	protected void onKeyDown(KeyEvent e) {
		int key = e.getKeyCode();
		activeKeys.put(key, true);

		if (key == VK_ESCAPE) {
			Sound.MENU_SELECT.play();
			game.PAUSE_MENU.setResumeScreen(this);
			game.setDisplayPanel(game.PAUSE_MENU);
			game.PAUSE_MENU.reset();
			setActive(false);
		}

		// TODO this is temporary
		if (key == VK_F8 && !ship.isDead()) {
			ship.explode();
		}
	}

	@Override
	protected void onKeyUp(KeyEvent e) {
		activeKeys.put(e.getKeyCode(), false);
	}

	protected void reset() {
		this.frozen = false;
		activeKeys.clear();
		ship.reset();
	}

	private boolean isKeyDown(List<Integer> keyNumbers) {
		for (int key : keyNumbers)
			if (activeKeys.getOrDefault(key, false)) return true;
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
	@Override
	public void tick() {
		ship.tick();

		// prevent anything but ship updates when in gameover mode
		if (frozen) return;

		// prevent all movement and firing while ship is exploding
		if (!ship.isDead()) {

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

			if (isKeyDown(FIRE_KEYS) && shootingEnabled) {
				if (!ship.isShotCharging() && System.currentTimeMillis() - lastShotTime >= Ship.SHOT_CHARGE_TIME) {
					this.shotChargeStart = System.currentTimeMillis();
					ship.setShotCharging(true);
					Sound.SHOT_CHARGE.play();
				}
			}

			if (ship.isShotCharging() && System.currentTimeMillis() - shotChargeStart >= Ship.SHOT_CHARGE_TIME) {
				fireShot();
				ship.setShotCharging(false);
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

	protected void fireShot() {

		Sound.SHOOT.play();

		int x = (int) (ship.getX() - SHOT_SIZE.getWidth() / 2);
		int y = SHIP_Y - SHIP_WIDTH / 2 - SHOT_SIZE.height;

		shots.add(new Rectangle(new Point(x, y), SHOT_SIZE));
	}

	protected void setFrozen(boolean frozen) { this.frozen = frozen; }
	protected boolean isFrozen() { return frozen; }

	protected void setShootingEnabled(boolean shootingEnabled) { this.shootingEnabled = shootingEnabled; }
}
