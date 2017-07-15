package aimtolearn.screens;

import aimtolearn.Game;

public class ConfirmQuitMenu extends BaseMenu {

	private BaseScreen returnScreen;

	public ConfirmQuitMenu(Game game) {
		super(game, new String[]{"Yes", "No"}, "Are you sure you\nwant to quit?");
		setTopHeight(400);
		setTitleFontSize(40);
		setOptionFontSize(30);
	}

	public void setReturnScreen(BaseScreen returnScreen) {
		this.returnScreen = returnScreen;
		reset();
	}

	@Override
	public boolean onSelection(int index) {
		if (index == 0) { // yes
			System.exit(0);
		}
		else if (index == 1 && returnScreen != null) { // no
			game.setDisplayPanel(returnScreen);
			returnScreen.setActive(true);
		}
		return true;
	}
}
