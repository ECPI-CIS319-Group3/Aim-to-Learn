package aimtolearn.sprites;

import aimtolearn.Constants;
import aimtolearn.Utils;

import javax.swing.SwingConstants;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

public class AnswerSprite {

	private String text;
	private final Rectangle bounds;

	private static final int PADDING = 5;

	public AnswerSprite(String text, Graphics g) {
		this.text = text;

		FontMetrics metrics = g.getFontMetrics();
		int width = metrics.stringWidth(text) + 2*PADDING;
		int height = metrics.getHeight() + 2*PADDING;

		int randX = Constants.RAND.nextInt(Constants.MAIN_WIDTH - width);
		this.bounds = new Rectangle(randX, 0, width, height);
	}

	public void draw(Graphics g) {
		Utils.text(text, bounds, g, SwingConstants.CENTER);
	}

	public void moveDown(int dy) {
		this.bounds.translate(0, dy);
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public String getText() {
		return text;
	}
}
