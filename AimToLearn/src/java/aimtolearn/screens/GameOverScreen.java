package aimtolearn.screens;

import aimtolearn.Constants;
import aimtolearn.Game;
import aimtolearn.Utils;

import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

public class GameOverScreen extends BaseMenu {

	private static final int TOP_HEIGHT = 300, SCORE_HEIGHT = 400;
	private static final Rectangle SCORE_BOUNDS = new Rectangle(0, TOP_HEIGHT, Constants.MAIN_WIDTH, SCORE_HEIGHT);
	private static final Font SCORE_FONT = Constants.PIXEL_FONT.deriveFont(120f);
	private static final Font SCORE_TEXT_FONT = Constants.PIXEL_FONT.deriveFont(40f);

	public GameOverScreen(Game game) {
		super(game, new String[]{"Return to menu", "Quit"}, "Game Over");
		setTopHeight(TOP_HEIGHT);
		setTitleFontSize(70);
		setExtraSpacing(SCORE_HEIGHT);
	}

	@Override
	protected void updateScreen(Graphics g) {
		super.updateScreen(g);

		g.setFont(SCORE_TEXT_FONT);
		Utils.text("Total Score", SCORE_BOUNDS, g, SwingConstants.TOP);
		g.setFont(SCORE_FONT);
		Utils.text(""+game.GAMEPLAY_SCREEN.getTotalScore(), SCORE_BOUNDS, g, SwingConstants.CENTER);
	}

	@Override
	public boolean onSelection(int index) {
		if (index == 0) game.setDisplayPanel(game.MAIN_MENU);
		else System.exit(0);
		return true;
	}
}
