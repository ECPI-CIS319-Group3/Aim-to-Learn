package aimtolearn.sprites;

import aimtolearn.Constants;
import aimtolearn.Sound;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.List;

import static aimtolearn.Constants.*;

/**
 * Class representing the controllable ship, along with all its animations and sounds
 */
public class Ship {

	private int x;
	private final int y, initialX;

	/** -1, 0, or 1 **/
	private byte direction;

	// timings for when certain events started - used for animations
	private long directionChangeStart, impactedStart, explosionStart;

	// the current ship's image - used for tilting/moving left/right having different images
	private Image currentImage;

	// different states of the ship
	private boolean shotCharging, dead;

	// constants

	private static final int INVULN_DURATION = 2000, SHIELD_STEP_DURATION = 150,
		HIT_DURATION = 250, EXPLOSION_DURATION = 1000;;
	public static final int SHOT_CHARGE_TIME = 750;

	public static final byte DIR_RIGHT = 1, DIR_NONE = 0, DIR_LEFT = -1;
	private static final List<Byte> DIRECTIONS = Arrays.asList(DIR_RIGHT, DIR_NONE, DIR_LEFT);

	// images and animations

	private static final Image MOVING_LEFT = Constants.getImage("ship_moving_left.png");
	private static final Image MOVE_LEFT = Constants.getImage("ship_left.png");
	private static final Image MOVING_RIGHT = Constants.getImage("ship_moving_right.png");
	private static final Image MOVE_RIGHT = Constants.getImage("ship_right.png");
	private static final AnimatedSprite FIRING_ANIM = new AnimatedSprite("ship_fire", 7, SHOT_CHARGE_TIME);
	private static final AnimatedSprite EXPLOSION_ANIM = new AnimatedSprite("ship_explosion", 4, EXPLOSION_DURATION);
	private static final AnimatedSprite SHIELD_OVERLAY_ANIM =
		new AnimatedSprite("ship_shield", 4, SHIELD_STEP_DURATION, INVULN_DURATION);
	private static final AnimatedSprite HIT_OVERLAY_ANIM = new AnimatedSprite("ship_hit_explosion", 6, HIT_DURATION);
	private static final List<AnimatedSprite> ANIMATIONS =
		Arrays.asList(FIRING_ANIM, EXPLOSION_ANIM, SHIELD_OVERLAY_ANIM, HIT_OVERLAY_ANIM);

	public Ship(int startX) {
		this.initialX = startX;
		this.y = SHIP_Y - SHIP_HEIGHT;
		reset();
	}

	/**
	 * Draws the ship at its current location.
	 * Called by the screen's updateScreen() method
	 */
	public void draw(Graphics g) {

		int x = computeX();
		if (isExploding()) EXPLOSION_ANIM.draw(g, x, y);
		else if (shotCharging) FIRING_ANIM.draw(g, x, y);
		else if (!dead) {
			g.drawImage(currentImage, x, y, null);
			if (HIT_OVERLAY_ANIM.isRunning()) HIT_OVERLAY_ANIM.draw(g, x, y);
			if (SHIELD_OVERLAY_ANIM.isRunning()) SHIELD_OVERLAY_ANIM.draw(g, x, y);
		}
	}

	/**
	 * Update the ship; called every game tick
	 */
	public void tick() {

		for (AnimatedSprite anim : ANIMATIONS) anim.tick();

		if (this.direction != 0) {
			if (System.currentTimeMillis() - directionChangeStart < 100)
				this.currentImage = this.direction == DIR_LEFT ? MOVING_LEFT : MOVING_RIGHT;
			else
				this.currentImage = this.direction == DIR_LEFT ? MOVE_LEFT : MOVE_RIGHT;
		}
		else {
			this.currentImage = SHIP_IMAGE;
		}
	}

	/**
	 * Resets the ship's location back to default
	 */
	public void reset() {
		this.x = initialX;
		this.dead = false;
		this.shotCharging = false;
		this.direction = 0;
		this.currentImage = SHIP_IMAGE;
	}

	/**
	 * Compute the ship's top-left position. this.x is tracked from the center of the ship
	 */
	private int computeX() {
		return this.x - SHIP_WIDTH / 2;
	}

	// getter and setter for x
	public int getX() { return x; }
	public void setX(int x) { this.x = x; }

	/**
	 * Notify the ship that it has been hit by a falling answer
	 */
	public void impacted() {
		this.impactedStart = System.currentTimeMillis();

		HIT_OVERLAY_ANIM.start();
		SHIELD_OVERLAY_ANIM.start();

		Sound.SHIP_HIT.play();
		Sound.SHIELD_ACTIVE.play();
	}

	/**
	 * Explode the ship
	 */
	public void explode() {
		this.explosionStart = System.currentTimeMillis();
		Sound.SHIP_EXPLOSION.play();
		this.dead = true;
		EXPLOSION_ANIM.start();
	}

	/**
	 * Checks if the ship is currently invincible, which is for a moment after being impacted
	 */
	public boolean isInvincible() {
		return System.currentTimeMillis() - impactedStart <= INVULN_DURATION;
	}

	public boolean isExploding() {
		return System.currentTimeMillis() - explosionStart <= EXPLOSION_DURATION;
	}

	public boolean isDead() { return dead; }

	/**
	 * Notify the ship that a shot has started/stopped charging. Starts the animation if needed
	 */
	public void setShotCharging(boolean charging) {
		this.shotCharging = charging;
		if (charging) FIRING_ANIM.start();
	}

	/** Get whether or not a shot is charging */
	public boolean isShotCharging() { return shotCharging; }

	/**
	 * Notify the ship that it has just began a direction change
	 */
	public void setDirection(byte direction) {
		if (!DIRECTIONS.contains(direction))
			throw new IllegalArgumentException("Invalid direction: " + direction);

		if (this.direction != direction)
			this.directionChangeStart = System.currentTimeMillis();

		this.direction = direction;
	}

	/**
	 * Get the ship's current bounding box - used for hit detection
	 */
	public Rectangle getBounds() {
		return new Rectangle(computeX(), y, SHIP_WIDTH, SHIP_HEIGHT);
	}
}
