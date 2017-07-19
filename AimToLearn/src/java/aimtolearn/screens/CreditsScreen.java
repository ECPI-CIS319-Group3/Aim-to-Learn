package aimtolearn.screens;

import aimtolearn.Constants;
import aimtolearn.Game;
import aimtolearn.Utils;

import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import static aimtolearn.Constants.LOGO_IMAGE;
import static aimtolearn.Constants.MAIN_WIDTH;

public class CreditsScreen extends BaseScreen {

	private int y;
	private int bottomY;

	private static final int SCROLL_SPEED = 1, OFFSCREEN = -300,
		LOGO_X = (MAIN_WIDTH - LOGO_IMAGE.getWidth(null)) / 2;

	private static final String J = "Justin Mariner",
		E = "Emir Kaynak", R = "Rebekah Marsh";

	private static final String[][] CREDITS = {
		{"By", J, R, E},
		{"Group Leader", J},
		{"Lead Programmer", J},
		{"Graphics Designer", E},
		{"Animation Artist", E},
		{"Sound Designer", R},
		{"Content Manager", R},
		{"Testers", "CIS319 Students", "Friends of group members"},
		{"Music", "Megaman Cirno Fortress Stage 2"},
		{"Thanks for playing!"}
	};

	public CreditsScreen(Game game) {
		super(game);
	}

	@Override
	protected void onKeyDown(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) returnToMenu();
	}

	public void init() {
		this.y = Constants.MAIN_HEIGHT + 100;
		setActive(true);
	}

	@Override
	protected void updateScreen(Graphics g) {

		int lineH = 40;
		int titleH = 70;
		Font titleFont = g.getFont().deriveFont(35f);
		int curY = y;

		g.drawImage(LOGO_IMAGE, LOGO_X, curY, null);

		curY += LOGO_IMAGE.getHeight(null) + 100;

		for (String[] part : CREDITS) {

			boolean isTitle = true;
			g.setFont(titleFont);

			for (String line : part) {
				int h = isTitle ? titleH : lineH;
				Rectangle bounds = new Rectangle(0, curY, Constants.MAIN_WIDTH, h);
				Utils.text(line, bounds, g, SwingConstants.TOP);
				if (isTitle) {
					isTitle = false;
					g.setFont(Constants.PIXEL_FONT);
				}
				curY += h;
			}

			curY += 75;

		}

		this.bottomY = curY;

	}

	@Override
	public void tick() {
		y -= SCROLL_SPEED;
		if (bottomY < OFFSCREEN) returnToMenu();
		repaint();
	}

	private void returnToMenu() {
		game.setDisplayPanel(game.MAIN_MENU);
	}
}
