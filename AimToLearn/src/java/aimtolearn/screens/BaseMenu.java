package aimtolearn.screens;

import aimtolearn.Constants;
import aimtolearn.Game;
import aimtolearn.Utils;

import javax.swing.SwingConstants;
import java.awt.*;
import java.awt.event.KeyEvent;

import static aimtolearn.Constants.*;

public abstract class BaseMenu extends BaseScreen {

	private String[] choices;
	private int selectedIndex;
	private String titleText;

	private Rectangle titleBounds;
	private Rectangle[] choiceBounds;

	private static final Image LOGO_IMAGE = Constants.getImage("logo.png");

	private static final int CHOICE_HEIGHT = 75, TOP_HEIGHT = 250, TRIANGLE_MARGIN = 10;
	private static final Dimension TRIANGLE_SIZE = new Dimension(30, 20);

	protected BaseMenu(Game game, String[] choices) {
		this(game, choices, null);
	}

	protected BaseMenu(Game game, String[] choices, String titleText) {
		super(game);
		this.choices = choices;
		this.selectedIndex = 0;
		this.titleText = titleText;

		this.titleBounds = new Rectangle(0, 0, MAIN_WIDTH, TOP_HEIGHT);
		this.choiceBounds = new Rectangle[choices.length];

		for (int i = 0; i < choices.length; i++) {
			this.choiceBounds[i] = new Rectangle(0, TOP_HEIGHT + i * CHOICE_HEIGHT, MAIN_WIDTH, CHOICE_HEIGHT);
		}
	}

	@Override
	protected void onKeyDown(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP)
			selectedIndex = selectedIndex == 0 ? choices.length-1 : selectedIndex-1;
		else if (e.getKeyCode() == KeyEvent.VK_DOWN)
			selectedIndex = selectedIndex == choices.length-1 ? 0 : selectedIndex+1;
		else if (e.getKeyCode() == KeyEvent.VK_ENTER)
			onSelection(selectedIndex);

		repaint();
	}

	@Override
	protected void updateScreen(Graphics g) {

		if (titleText == null) {
			int logoX = (MAIN_WIDTH - LOGO_IMAGE.getWidth(null)) / 2;
			g.drawImage(LOGO_IMAGE, logoX, 100, this);
		}
		else {
			g.setFont(PIXEL_FONT.deriveFont(64f));
			Utils.text(titleText, titleBounds, g, SwingConstants.CENTER);
		}

		g.setFont(PIXEL_FONT);

		for (int i = 0; i < choices.length; i++) {

			Point pos = Utils.text(choices[i], choiceBounds[i], g, SwingConstants.BOTTOM);

			if (i == selectedIndex) {
				Polygon triangle = new Polygon(
					new int[]{-TRIANGLE_SIZE.width/2, 0, -TRIANGLE_SIZE.width/2},
					new int[]{-TRIANGLE_SIZE.height/2, 0, TRIANGLE_SIZE.height/2},
					3);
				triangle.translate(pos.x - TRIANGLE_MARGIN, pos.y);
				g.fillPolygon(triangle);
			}

		}
	}

	@Override
	public void tick() {}

	public abstract void onSelection(int index);
}
