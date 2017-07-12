package aimtolearn.screens;

import aimtolearn.Game;

public class PauseMenu extends BaseMenu {

	protected PauseMenu(Game game) {
		super(game, new String[]{"Resume", "Options", "How to play", "Return to menu"}, "PAUSED");
	}

	@Override
	public void onSelection(int index) {

	}
}
