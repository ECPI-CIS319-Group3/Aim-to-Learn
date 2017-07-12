package aimtolearn.screens;

import aimtolearn.Game;

public class PauseMenu extends BaseMenu {

	public PauseMenu(Game game) {
		super(game, new String[]{"Resume", "Options", "How to play", "Return to menu"}, "PAUSED");
		setDisabledIndexes(1);
	}

	@Override
	protected void onEscape() {
		onSelection(0);
	}

	@Override
	public void onSelection(int index) {
		if (index == 0) {
			game.setDisplayPanel(game.GAMEPLAY_SCREEN);
			game.GAMEPLAY_SCREEN.setActive(true);
		}
		else if (index == 1) {
			game.changeRes();
		}
		else if (index == 2) {
			game.howToPlay();
		}
		else if (index == 3) {
			game.setDisplayPanel(game.MAIN_MENU);
		}
	}
}
