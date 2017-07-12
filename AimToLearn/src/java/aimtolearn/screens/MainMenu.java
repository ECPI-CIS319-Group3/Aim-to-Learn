package aimtolearn.screens;

import aimtolearn.Constants;
import aimtolearn.Game;

import java.awt.Image;

public class MainMenu extends BaseMenu {

	private static final Image LOGO_IMAGE = Constants.getImage("logo.png");

	public MainMenu(Game game) {
		super(game, new String[]{"Start", "Options", "How to Play", "Quit"}, LOGO_IMAGE);
		setTopHeight(300);
	}

	@Override
	protected void onEscape() {
		game.quitTemp();
	}

	@Override
	public void onSelection(int index) {
		if (index == 0) startChosen();
		else if (index == 1) game.optionsTemp();
		else if (index == 2) game.howToPlayTemp();
		else if (index == 3) game.quitTemp();
		else throw new AssertionError("Not possible");
	}

	private void startChosen() {
		game.setDisplayPanel(game.SUBJECT_SCREEN);
		game.SUBJECT_SCREEN.init();
	}
}

