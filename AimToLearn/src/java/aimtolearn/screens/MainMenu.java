package aimtolearn.screens;

import aimtolearn.Constants;
import aimtolearn.Game;

public class MainMenu extends BaseMenu {

	public MainMenu(Game game) {
		super(game, new String[]{"Start", "Options", "How to Play", "Quit"}, Constants.LOGO_IMAGE);
		setTopHeight(300);
	}

	@Override
	protected void onEscape() {
		onSelection(3);
	}

	@Override
	public void onSelection(int index) {
		if (index == 0) startChosen();
		else if (index == 1) game.optionsTemp();
		else if (index == 2) game.howToPlayTemp();
		else if (index == 3) game.confirmQuit(this);
		else throw new AssertionError("Not possible");
	}

	private void startChosen() {
		game.setDisplayPanel(game.SUBJECT_SCREEN);
		game.SUBJECT_SCREEN.init();
	}
}

