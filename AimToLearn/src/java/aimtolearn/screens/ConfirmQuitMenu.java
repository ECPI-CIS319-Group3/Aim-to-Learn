package aimtolearn.screens;

import aimtolearn.Game;

public class ConfirmQuitMenu extends ReturnableMenu {

	public ConfirmQuitMenu(Game game) {
		super(game, new String[]{"Yes", "No"}, "Are you sure you\nwant to quit?");
		setTopHeight(400);
		setTitleFontSize(40);
		setOptionFontSize(30);
	}

	@Override
	public boolean onSelection(int index) {
		if (index == 0) // yes
			System.exit(0);
		else if (index == 1) // no
			returnToScreen();

		return true;
	}
}
