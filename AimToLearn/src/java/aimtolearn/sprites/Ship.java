package aimtolearn.sprites;

import aimtolearn.Constants;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
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

	private static final int INVULN_DURATION = 1000;

	public static final byte DIR_RIGHT = 1, DIR_NONE = 0, DIR_LEFT = -1;
	private static final List<Byte> DIRECTIONS = Arrays.asList(DIR_RIGHT, DIR_NONE, DIR_LEFT);

	private static final BufferedImage MOVING_LEFT, MOVE_LEFT, MOVING_RIGHT, MOVE_RIGHT;

	static {
		MOVING_LEFT = Constants.getImage("ship_moving_left.png");
		MOVE_LEFT = Constants.getImage("ship_left.png");
		MOVING_RIGHT = Constants.getImage("ship_moving_right.png");
		MOVE_RIGHT = Constants.getImage("ship_right.png");
	}

	public Ship(int startX) {
		this.x = startX;
		this.y = SHIP_Y - SHIP_HEIGHT;
		this.direction = 0;
		this.currentImage = SHIP_IMAGE;
	}

	public void draw(Graphics g) {
		g.drawImage(currentImage, computeX(), y, null);
	}

	public void tick() {
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
	}

	public boolean isInvincible() {
		return System.currentTimeMillis() - impactedStart <= INVULN_DURATION;
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
