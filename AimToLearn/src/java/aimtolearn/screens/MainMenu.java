package aimtolearn.screens;

import aimtolearn.Game;
import aimtolearn.Question;
import aimtolearn.Utils;

import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static aimtolearn.Constants.*;

public class MainMenu extends GamePanel {
	// used to create errors when using these; shouldn't use them
	@SuppressWarnings("unused")
	private Integer WIDTH = null, HEIGHT = null;

	private static final String[] ITEMS = {"Start", "Options", "How to Play", "Quit"};

	private int selectedIndex = 0;

	public MainMenu(Game game) {

		super(game);

		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_UP)
					selectedIndex = selectedIndex == 0 ? ITEMS.length-1 : selectedIndex-1;
				else if (e.getKeyCode() == KeyEvent.VK_DOWN)
					selectedIndex = selectedIndex == ITEMS.length-1 ? 0 : selectedIndex+1;
				else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					switch (selectedIndex) {
						case 0: // Start
							startChosen();
							break;
						case 1: // Options
							game.changeRes();
							break;
						case 2: // How to play
							JOptionPane.showMessageDialog(game, "TODO: How to Play menu");
							break;
						case 3: // Quit
							game.quit();
							break;
						default:
							throw new AssertionError("Not possible");
					}
				}

			//	if (selectedIndex == -1) selectedIndex = ITEMS.length-1;
			//	if (selectedIndex == ITEMS.length) selectedIndex = 0;

				repaint();

			}
		});
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

	@Override
	protected void updateScreen(Graphics g) {
		final int height = 75, topHeight = 250;
		final int triangleHeight = 20, triangleWidth = 30, triangleMargin = 10;

		g.setFont(PIXEL_FONT.deriveFont(64f));
		Utils.text("Aim to Learn", new Rectangle(0, 0, MAIN_WIDTH, topHeight), g, SwingConstants.CENTER);

		g.setFont(PIXEL_FONT);

		Rectangle bounds = new Rectangle(0, topHeight, MAIN_WIDTH, height);

		Polygon triangle = new Polygon();
		triangle.addPoint(-triangleWidth/2, -triangleHeight/2);
		triangle.addPoint(0, 0);
		triangle.addPoint(-triangleWidth/2, triangleHeight/2);

		for (int i = 0; i < ITEMS.length; i++) {

			Point pos = Utils.text(ITEMS[i], bounds, g, SwingConstants.BOTTOM);

			if (i == selectedIndex) {
				triangle.translate(pos.x - triangleMargin, pos.y);
				g.fillPolygon(triangle);
			}

			bounds.translate(0, height);
		}

	}
}

