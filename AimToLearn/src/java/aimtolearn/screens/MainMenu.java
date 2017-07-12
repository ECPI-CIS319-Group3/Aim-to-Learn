package aimtolearn.screens;

import aimtolearn.Game;
import aimtolearn.Question;

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
		Question.Subject[] subjects = Question.Subject.values();
		String[] subjectStrings = new String[subjects.length];
		for (int i = 0; i < subjects.length; i++)
			subjectStrings[i] = subjects[i].name().toUpperCase();

		game.shootingOption("Choose subject", subjectStrings, result -> {
			game.setDisplayPanel(game.GAMEPLAY_SCREEN);
			game.GAMEPLAY_SCREEN.start(subjects[result], Question.Difficulty.EASY);
		});
	}
}

