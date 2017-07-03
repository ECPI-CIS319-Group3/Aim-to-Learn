package aimtolearn;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

import static aimtolearn.Constants.SHIP_Y;

public class Ship {

	private int x;

	/** -1, 0, or 1 **/
	private byte direction;

	private long directionChangeStart;

	private Image currentImage;

	private static final String SHIP_FILE_NAME = "ship.png";
	private static final String SHIP_MOVE_LEFT_NAME = "moveShipLeft.png";
	private static final String SHIP_MOVE_RIGHT_NAME = "moveShipRight.png";

	public static final byte DIR_RIGHT = 1, DIR_NONE = 0, DIR_LEFT = -1;
	private static final List<Byte> DIRECTIONS = Arrays.asList(DIR_RIGHT, DIR_NONE, DIR_LEFT);

	private static final BufferedImage IMAGE, MOVING_LEFT, MOVE_LEFT, MOVING_RIGHT, MOVE_RIGHT;
	public static final int WIDTH, HEIGHT;

	static {

		IMAGE = Constants.getImage("ship.png");

		if (IMAGE != null) {
			WIDTH = IMAGE.getWidth(null);
			HEIGHT = IMAGE.getHeight(null);
		}
		else {
			WIDTH = -1;
			HEIGHT = -1;
		}

		MOVING_LEFT = Constants.getImage("ship_moving_left.png");
		MOVE_LEFT = Constants.getImage("ship_left.png");
		MOVING_RIGHT = Constants.getImage("ship_moving_right.png");
		MOVE_RIGHT = Constants.getImage("ship_right.png");
	}

	public Ship(int startX) {
		this.x = startX;
		this.direction = 0;
		this.currentImage = IMAGE;
	}

	public void draw(Graphics g) {
		int y = SHIP_Y - HEIGHT;
		int x = this.x - WIDTH / 2;

		g.drawImage(currentImage, x, y, null);
	}

	public void tick() {
		if (this.direction != 0) {
			if (System.currentTimeMillis() - this.directionChangeStart < 100)
				this.currentImage = this.direction == DIR_LEFT ? MOVING_LEFT : MOVING_RIGHT;
			else
				this.currentImage = this.direction == DIR_LEFT ? MOVE_LEFT : MOVE_RIGHT;
		}
		else {
			this.currentImage = IMAGE;
		}
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setDirection(byte direction) {
		if (!DIRECTIONS.contains(direction))
			throw new IllegalArgumentException("Invalid direction: " + direction);

		if (this.direction != direction)
			this.directionChangeStart = System.currentTimeMillis();

		this.direction = direction;
	}
}
