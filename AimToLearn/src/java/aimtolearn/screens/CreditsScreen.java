package aimtolearn.screens;

import aimtolearn.Game;
import aimtolearn.Utils;

import javax.swing.SwingConstants;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import static aimtolearn.Constants.*;

public class CreditsScreen extends BaseScreen {

	private Rectangle topBounds;

	private static final int SCROLL_SPEED = 1, OFFSCREEN = -300;

	private static final String J = "Justin Mariner",
		E = "Emir Kaynak", R = "Rebekah Marsh";

	private static final String CREDITS = String.join("\n",
		title("Aim to Learn"), J, R, E, "",
		title("Group Leader"), J, "",
		title("Lead Programmer"), J, "",
		title("Graphics Designer"), E, "",
		title("Animation Artist"), E, "",
		title("Sound Designer"), R, "",
		title("Content Manager"), R, "",
		title("Testers"), "CIS319 Students", "Friends of group members", "", "",
		"Thanks for playing!"
	);
	// len * fontsize + (len-1) * space
	// len * fontsize + len * space - space
	// len * (fontsize + space) - space

	private static final int TEXT_HEIGHT =
		CREDITS.split("\n").length * ((int) MAIN_FONT + MULTILINE_SPACING) - MULTILINE_SPACING;

	private static String title(String s) { return String.format("---%s---", s); }

	public CreditsScreen(Game game) {
		super(game);
	}

	@Override
	protected void onKeyDown(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) returnToMenu();
	}

	public void init() {
		this.topBounds = new Rectangle(0, MAIN_HEIGHT, MAIN_WIDTH, TEXT_HEIGHT);
		setActive(true);
	}

	@Override
	protected void updateScreen(Graphics g) {
		Utils.text(CREDITS, topBounds, g, SwingConstants.CENTER);
	}

	@Override
	public void tick() {
		topBounds.translate(0, -SCROLL_SPEED);
		repaint();

		if (topBounds.getMaxY() < OFFSCREEN) returnToMenu();
	}

	private void returnToMenu() {
		game.setDisplayPanel(game.MAIN_MENU);
	}
}
