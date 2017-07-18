package aimtolearn.screens;

import aimtolearn.Game;
import aimtolearn.Question;

public class ContinueShootingMenu extends BaseShootingMenu {


	public ContinueShootingMenu(Game game) {
		super(game, "Round Complete!\nHow would you like to continue?", "Change subject", "Increase Difficulty");
		setPromptHeight(300);
	}

	@Override
	protected void onSelection(int index) {

		Question current = game.GAMEPLAY_SCREEN.getQuestion();
		Question.Subject sub = current.getSubject();

		if (index == 0) { // "change subject"
			game.setDisplayPanel(game.SUBJECT_SCREEN);
			game.SUBJECT_SCREEN.init(false);
			game.SUBJECT_SCREEN.setDisabledSubject(sub);
		}
		else if (index == 1) { // "inc difficulty"
			game.setDisplayPanel(game.GAMEPLAY_SCREEN);

			Question.Difficulty[] diffs = Question.Difficulty.values();

			int curIndex = current.getDifficulty().ordinal();
			if (curIndex < diffs.length-1) curIndex++;

			game.GAMEPLAY_SCREEN.start(sub, diffs[curIndex]);
		}
		else {
			throw new AssertionError("Not possible");
		}
	}
}
