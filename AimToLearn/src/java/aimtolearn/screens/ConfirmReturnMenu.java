package aimtolearn.screens;

import aimtolearn.Game;

public class ConfirmReturnMenu extends ReturnableMenu {

	public ConfirmReturnMenu(Game game) {
		super(game, new String[]{"Yes", "No"}, "Are you sure you want to\nreturn to the main menu?");
		setTopHeight(500);
		setTitleFontSize(40);
		setOptionFontSize(30);
	}

	@Override
	public boolean onSelection(int index) {
		if (index == 0) // yes
			game.setDisplayPanel(game.MAIN_MENU);
		else if (index == 1) // no
			returnToScreen();

		return true;
	}
}
