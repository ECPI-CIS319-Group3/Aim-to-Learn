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
		Question.Subject[] subs = Question.Subject.values();

		if (index < subs.length) {
			game.GAMEPLAY_SCREEN.start(subs[index], Question.Difficulty.EASY);
			game.setDisplayPanel(game.GAMEPLAY_SCREEN);
		}
		else {
			throw new AssertionError("Not possible");
		}
	}
}
