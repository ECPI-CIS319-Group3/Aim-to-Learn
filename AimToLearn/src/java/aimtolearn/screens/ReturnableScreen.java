package aimtolearn.screens;

import aimtolearn.Game;

public abstract class ReturnableScreen extends BaseMenu {

	private BaseScreen returnScreen;

	protected ReturnableScreen(Game game, String[] choices, String titleText) {
		super(game, choices, titleText);
	}

	protected void returnToScreen() {
		if (returnScreen != null) {
			game.setDisplayPanel(returnScreen);
			returnScreen.setActive(true);
		}
	}

	public void setReturnScreen(BaseScreen returnScreen) {
		this.returnScreen = returnScreen;
		reset();
	}

	@Override
	protected void onEscape() {
		returnToScreen();
	}
}
