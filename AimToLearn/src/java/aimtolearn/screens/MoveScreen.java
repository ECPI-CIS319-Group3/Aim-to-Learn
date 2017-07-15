package aimtolearn.screens;

import aimtolearn.Game;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MoveScreen extends ReturnableMenu {

	private Point startClick;

	public MoveScreen(Game game) {
		super(game, new String[]{"Back"}, "Click and drag anywhere\nto move the window.\nDouble click to center.");
		setTopHeight(600);
		setTitleFontSize(35);

		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2)
					game.setLocationRelativeTo(null); // re-center
				else
					startClick = e.getPoint();
			}
		});

		this.addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent e) {

				if (startClick == null) return;

				// TODO this needs work

				// get location of Window
				int thisX = game.getLocation().x;
				int thisY = game.getLocation().y;

				// Determine how much the mouse moved since the initial click
				int xMoved = (thisX + e.getX()) - (thisX + startClick.x);
				int yMoved = (thisY + e.getY()) - (thisY + startClick.y);

				// Move window to this position
				int X = thisX + xMoved;
				int Y = thisY + yMoved;
				game.setLocation(X, Y);
			}
		});

	}

	@Override
	public boolean onSelection(int index) {
		if (index == 0) returnToScreen();
		return true;
	}
}
