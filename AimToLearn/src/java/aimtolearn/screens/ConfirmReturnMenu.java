package aimtolearn.screens;

import aimtolearn.Game;

public class ConfirmReturnMenu extends BaseMenu {

	private BaseScreen returnScreen;

	public ConfirmReturnMenu(Game game) {
		super(game, new String[]{"Yes", "No"}, "Are you sure you want to\nreturn to the main menu?");
		setTopHeight(500);
		setTitleFontSize(40);
		setOptionFontSize(30);
	}

	public void setReturnScreen(BaseScreen returnScreen) {
		this.returnScreen = returnScreen;
		reset();
	}

	@Override
	public boolean onSelection(int index) {
		if (index == 0) { // yes
			game.setDisplayPanel(game.MAIN_MENU);
		}
		else if (index == 1 && returnScreen != null) { // no
			game.setDisplayPanel(returnScreen);
			returnScreen.setActive(true);
		}
		return true;
	}
}
