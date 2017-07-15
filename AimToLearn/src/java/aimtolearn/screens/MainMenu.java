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
		onSelection(3);
	}

	@Override
	public boolean onSelection(int index) {
		if (index == 0) startChosen();
		else if (index == 1) game.openOptions(this);
		else if (index == 2) game.howToPlay(this);
		else if (index == 3) game.confirmQuit(this);
		else throw new AssertionError("Not possible");
		return true;
	}

	private void startChosen() {
		game.GAMEPLAY_SCREEN.resetScore();

		game.setDisplayPanel(game.SUBJECT_SCREEN);
		game.SUBJECT_SCREEN.init();
	}
}

