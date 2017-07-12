package aimtolearn.screens;

import aimtolearn.Game;
import aimtolearn.Question;

public class SubjectShootingMenu extends BaseShootingMenu {

	public SubjectShootingMenu(Game game) {
		super(game, "Choose a subject", Question.Subject.items());
	}

	public void setDisabledSubject(Question.Subject sub) {
		setDisabledIndexes(sub.ordinal());
	}

	@Override
	protected void onSelection(int index) {
		game.setDisplayPanel(game.GAMEPLAY_SCREEN);
		game.GAMEPLAY_SCREEN.start(Question.Subject.values()[index], Question.Difficulty.EASY);
	}
}
