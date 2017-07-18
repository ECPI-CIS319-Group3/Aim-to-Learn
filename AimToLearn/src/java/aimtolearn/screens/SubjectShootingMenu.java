package aimtolearn.screens;

import aimtolearn.Game;
import aimtolearn.Question;

public class SubjectShootingMenu extends BaseShootingMenu {

	private static final String PROMPT_1 = "Choose a subject";
	private static final String PROMPT_2 = "Difficulty complete.\nChoose another subject";

	public SubjectShootingMenu(Game game) {
		super(game, PROMPT_1, Question.Subject.items());
	}

	public void init(boolean diffComplete) {
		setPrompt(diffComplete ? PROMPT_2 : PROMPT_1);
		setPromptHeight(diffComplete ? 300 : 200);
		super.init();
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
