package aimtolearn.sprites;

import aimtolearn.Utils;

import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import static aimtolearn.Constants.LARGE_FONT;
import static aimtolearn.Constants.SMALL_FONT;

public class NumberBox {

	private String title;
	private int value;
	private Rectangle mainBounds, titleBounds, textBounds;

	private static final int TEXT_MARGIN = 10;

	public NumberBox(String title, int x, int y, int width, int height) {
		this.title = title;
		this.mainBounds = new Rectangle(x, y, width, height);

		this.titleBounds = new Rectangle(mainBounds);
		titleBounds.translate(0, TEXT_MARGIN);

		this.textBounds = new Rectangle(titleBounds);
		titleBounds.translate(0, (int) SMALL_FONT);
		titleBounds.setSize(titleBounds.width, titleBounds.height - TEXT_MARGIN - (int) SMALL_FONT);
	}

	public void update(int value) {
		this.value = value;
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fill(mainBounds);
		g.setColor(Color.WHITE);
		g.draw(mainBounds);

		Font oldFont = g.getFont();

		g.setFont(g.getFont().deriveFont(SMALL_FONT));
		Utils.text(title, titleBounds, g, SwingConstants.TOP);

		g.setFont(g.getFont().deriveFont(LARGE_FONT));

		Utils.text(""+value, textBounds, g, SwingConstants.CENTER);

		g.setFont(oldFont);
	}

	public Rectangle getBounds() {
		return mainBounds;
	}

}
