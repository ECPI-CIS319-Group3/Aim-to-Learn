package aimtolearn.screens;

import aimtolearn.Constants;
import aimtolearn.Game;
import aimtolearn.sprites.Ship;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class AnimationOverlay extends JPanel {

	private final Game game;

	public AnimationOverlay(Game game) {
		this.game = game;
		this.setOpaque(false);
		this.setBounds(0, 0, Constants.MAIN_WIDTH, Constants.MAIN_HEIGHT);
	}

	public void tick() {

	}

	@Override
	protected void paintComponent(Graphics graphics) {

		Graphics2D g = ((Graphics2D) graphics);

		// TODO temporary

		g.setColor(Color.RED);

		if (game.getActivePanel() instanceof MainScreen) {
			Ship ship = ((MainScreen) game.getActivePanel()).getShip();
			g.draw(ship.getBounds());
		}

	}
}
