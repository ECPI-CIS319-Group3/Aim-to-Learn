package aimtolearn.screens;

import aimtolearn.Constants;
import aimtolearn.Game;
import aimtolearn.Sound;

import java.awt.Image;

public class MainMenu extends BaseMenu {

	private static final Image LOGO_IMAGE = Constants.getImage("logo.png");
	private static final int QUIT = 4;

	public MainMenu(Game game) {
		super(game, new String[]{"Start", "Options", "How to Play", "Credits", "Quit"}, LOGO_IMAGE);
		setTopHeight(300);
	}

	@Override
	protected void onEscape() {
		onSelection(QUIT);
	}

	@Override
	public boolean onSelection(int index) {
		if (index == 0) {
			game.GAMEPLAY_SCREEN.resetScore();
			game.setDisplayPanel(game.SUBJECT_SCREEN);
			game.SUBJECT_SCREEN.init();
		}
		else if (index == 1) {
			game.openOptions(this);
		}
		else if (index == 2) {
			game.setDisplayPanel(game.TUTORIAL_SCREEN);
			game.TUTORIAL_SCREEN.init();
		}
		else if (index == 3) {
			game.setDisplayPanel(game.CREDITS_SCREEN);
			game.CREDITS_SCREEN.init();
			Sound.BG_MUSIC_V1.stop();
			Sound.BG_MUSIC_V1.loop();
		}
		else if (index == QUIT) {
			game.confirmQuit(this);
		}
		else {
			throw new AssertionError("Not possible");
		}
		return true;
	}
}

