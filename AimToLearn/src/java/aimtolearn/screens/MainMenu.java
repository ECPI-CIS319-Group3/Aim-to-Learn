package aimtolearn.screens;

import aimtolearn.Game;

import javax.swing.JOptionPane;

public class MainMenu extends MenuScreen {

	public MainMenu(Game game) {
		super(game, new String[]{"Start", "Options", "How to Play", "Quit"});
	}

	@Override
	public void onSelection(int index) {
		if (index == 0) startChosen();
		else if (index == 1) game.changeRes();
		else if (index == 2) JOptionPane.showMessageDialog(game, "TODO: How to Play menu");
		else if (index == 3) game.quit();
		else throw new AssertionError("Not possible");
	}

	private void startChosen() {
		game.setDisplayPanel(game.SUBJECT_SCREEN);
		game.SUBJECT_SCREEN.init();
	}
}

