package aimtolearn.screens;

import aimtolearn.Constants;
import aimtolearn.Game;

import java.awt.Image;

public class PauseMenu extends BaseMenu {

	private BaseScreen resumeScreen;

	private static final Image PAUSED_LOGO = Constants.getImage("pause-logo.png");

	public PauseMenu(Game game) {
		super(game, new String[]{"Resume", "Options", "Controls", "Return to menu"}, PAUSED_LOGO);
		this.resumeScreen = null;
	}

	@Override
	protected void onEscape() {
		onSelection(0);
	}

	public void setResumeScreen(BaseScreen screen) {
		this.resumeScreen = screen;
	}

	@Override
	public boolean onSelection(int index) {

		BaseScreen resume = resumeScreen == null ? game.GAMEPLAY_SCREEN : resumeScreen;

		if (index == 0) {
			game.setDisplayPanel(resume);

			if (resume instanceof ShipScreen)
				((ShipScreen) resume).reset();

			resume.setActive(true);
		}
		else if (index == 1) {
			game.openOptions(this);
		}
		else if (index == 2) {
			game.howToPlay(this);
		}
		else if (index == 3) {
			game.setDisplayPanel(game.CONFIRM_RETURN_MENU);
			game.CONFIRM_RETURN_MENU.setReturnScreen(this);
		}

		return true;
	}
}
