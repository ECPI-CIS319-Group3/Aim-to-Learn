package aimtolearn.screens;

import aimtolearn.Game;

public class MainMenu extends BaseMenu {

	public MainMenu(Game game) {
		super(game, new String[]{"Start", "Options", "How to Play", "Quit"});
		setTopHeight(300);
	}

	@Override
	protected void onEscape() {
		game.quit();
	}

	@Override
	public void onSelection(int index) {
		if (index == 0) startChosen();
		else if (index == 1) game.changeRes();
		else if (index == 2) game.howToPlay();
		else if (index == 3) game.quit();
		else throw new AssertionError("Not possible");
	}

	private void startChosen() {
		game.setDisplayPanel(game.SUBJECT_SCREEN);
		game.SUBJECT_SCREEN.init();
	}
}

