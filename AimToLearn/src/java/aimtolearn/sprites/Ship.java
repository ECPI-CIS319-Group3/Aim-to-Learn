package aimtolearn.sprites;

import aimtolearn.Constants;
import aimtolearn.Sound;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.List;

import static aimtolearn.Constants.*;

public class Ship {

	private int x;
	private final int y;

	/** -1, 0, or 1 **/
	private byte direction;
	private long directionChangeStart, impactedStart;
	private Image currentImage;
	private boolean shotCharging;

	private static final int INVULN_DURATION = 2000;
	public static final int SHOT_CHARGE_TIME = 750;

	public static final byte DIR_RIGHT = 1, DIR_NONE = 0, DIR_LEFT = -1;
	private static final List<Byte> DIRECTIONS = Arrays.asList(DIR_RIGHT, DIR_NONE, DIR_LEFT);

	private static final Image MOVING_LEFT = Constants.getImage("ship_moving_left.png");
	private static final Image MOVE_LEFT = Constants.getImage("ship_left.png");
	private static final Image MOVING_RIGHT = Constants.getImage("ship_moving_right.png");
	private static final Image MOVE_RIGHT = Constants.getImage("ship_right.png");
	private static final AnimatedSprite FIRING_ANIM = new AnimatedSprite("ship_fire", 9, SHOT_CHARGE_TIME);
	private static final AnimatedSprite EXPLOSION_ANIM = new AnimatedSprite("ship_explosion", 3, 1);
	private static final AnimatedSprite SHIELD_OVERLAY_ANIM = new AnimatedSprite("ship_shield", 4, 250, INVULN_DURATION);
	private static final AnimatedSprite HIT_OVERLAY_ANIM = new AnimatedSprite("ship_hit_explosion", 6, 250);
	private static final List<AnimatedSprite> ANIMATIONS =
		Arrays.asList(FIRING_ANIM, EXPLOSION_ANIM, SHIELD_OVERLAY_ANIM, HIT_OVERLAY_ANIM);

	public Ship(int startX) {
		this.x = startX;
		this.y = SHIP_Y - SHIP_HEIGHT;
		this.direction = 0;
		this.currentImage = SHIP_IMAGE;
	}

	public void draw(Graphics g) {

		int x = computeX();
		if (shotCharging) FIRING_ANIM.draw(g, x, y);
		else g.drawImage(currentImage, x, y, null);

		if (HIT_OVERLAY_ANIM.isRunning()) HIT_OVERLAY_ANIM.draw(g, x, y);
		if (SHIELD_OVERLAY_ANIM.isRunning()) SHIELD_OVERLAY_ANIM.draw(g, x, y);
	}

	public void tick() {

		for (AnimatedSprite anim : ANIMATIONS) anim.tick();

		if (this.direction != 0) {
			if (System.currentTimeMillis() - this.directionChangeStart < 100)
				this.currentImage = this.direction == DIR_LEFT ? MOVING_LEFT : MOVING_RIGHT;
			else
				this.currentImage = this.direction == DIR_LEFT ? MOVE_LEFT : MOVE_RIGHT;
		}
		else {
			this.currentImage = SHIP_IMAGE;
		}
	}

	private int computeX() {
		return this.x - SHIP_WIDTH / 2;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void impacted() {
		this.impactedStart = System.currentTimeMillis();
		HIT_OVERLAY_ANIM.start();
		SHIELD_OVERLAY_ANIM.start();

		Sound.SHIP_HIT.play();

		Sound.SHIELD_ACTIVE.play();
	}

	public boolean isInvincible() {
		return System.currentTimeMillis() - impactedStart <= INVULN_DURATION;
	}

	public void setShotCharging(boolean charging) {
		this.shotCharging = charging;
		FIRING_ANIM.start();
	}

	public boolean isShotCharging() {
		return shotCharging;
	}

	public void setDirection(byte direction) {
		if (!DIRECTIONS.contains(direction))
			throw new IllegalArgumentException("Invalid direction: " + direction);

		if (this.direction != direction)
			this.directionChangeStart = System.currentTimeMillis();

		this.direction = direction;
	}

	public Rectangle getBounds() {
		return new Rectangle(computeX(), y, SHIP_WIDTH, SHIP_HEIGHT);
	}
}
