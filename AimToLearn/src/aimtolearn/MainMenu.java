package aimtolearn;

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

	protected MainMenu(Game game) {

		super(game);

		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_UP)
					selectedIndex = selectedIndex == 0 ? ITEMS.length-1 : selectedIndex-1;
				else if (e.getKeyCode() == KeyEvent.VK_DOWN)
					selectedIndex++;
				else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					switch (selectedIndex) {
						case 0: // Start
							game.setDisplayPanel(game.getGameplayScreen());
							break;
						case 1: // Options
							game.changeRes();
							break;
						case 2: // How to play
							JOptionPane.showMessageDialog(game, "TODO: How to Play menu");
							break;
						case 3: // Quit
							int confirm = JOptionPane.showConfirmDialog(game, "Are you sure you want to quit?", "Confirm Quit",
								JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
							if (confirm == 0) System.exit(0);
							break;
						default:
							throw new AssertionError("Not possible");
					}
				}

				if (selectedIndex == -1) selectedIndex = ITEMS.length-1;
				if (selectedIndex == ITEMS.length) selectedIndex = 0;

				repaint();

			}
		});
	}

	@Override
	protected void updateScreen(Graphics g) {
		final int height = 75, topHeight = 250;
		final int triangleHeight = 20, triangleWidth = 30, triangleMargin = 10;

		g.setFont(PIXEL_FONT.deriveFont(64f));
		Game.text("Aim to Learn", new Rectangle(0, 0, MAIN_WIDTH, topHeight), g, SwingConstants.CENTER);

		g.setFont(PIXEL_FONT);

		Rectangle bounds = new Rectangle(0, topHeight, MAIN_WIDTH, height);

		Polygon triangle = new Polygon();
		triangle.addPoint(-triangleWidth/2, -triangleHeight/2);
		triangle.addPoint(0, 0);
		triangle.addPoint(-triangleWidth/2, triangleHeight/2);

		for (int i = 0; i < ITEMS.length; i++) {

			Point pos = Game.text(ITEMS[i], bounds, g, SwingConstants.BOTTOM);

			if (i == selectedIndex) {
				triangle.translate(pos.x - triangleMargin, pos.y);
				g.fillPolygon(triangle);
			}

			bounds.translate(0, height);
		}

	}
}

