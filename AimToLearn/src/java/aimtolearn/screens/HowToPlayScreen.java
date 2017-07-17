package aimtolearn.screens;

import aimtolearn.Game;
import aimtolearn.Sound;

import java.awt.event.KeyEvent;

public class HowToPlayScreen extends ReturnableMenu {

	public HowToPlayScreen(Game game) {
		super(game, new String[]{
			"Move left       ← or A",
			"Move right      → or D",
			"Shoot           ↑ or W",
			"Pause           Escape",
			"Quit             F10  "
		}, "Controls");

		setMovementEnabled(false);
	}

	@Override
	protected void onKeyDown(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			Sound.MENU_SELECT.play();
			returnToScreen();
		}
		else {
			super.onKeyDown(e);
		}
	}

	@Override
	protected boolean onSelection(int i) { return false; }
}
