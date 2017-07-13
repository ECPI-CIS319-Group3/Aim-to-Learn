package aimtolearn.screens;

import aimtolearn.Game;

public class GameOverScreen extends BaseMenu {
	public GameOverScreen(Game game) {
		super(game, new String[]{"Return to menu", "Quit"}, "Game\nOver");
		setTopHeight(400);
	}

	@Override
	public void onSelection(int index) {
		if (index == 0) game.setDisplayPanel(game.MAIN_MENU);
		else System.exit(0);
	}
}
