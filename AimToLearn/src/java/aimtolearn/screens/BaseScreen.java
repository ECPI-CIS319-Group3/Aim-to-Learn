package aimtolearn.screens;

import aimtolearn.Game;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.FilteredImageSource;
import java.awt.image.ReplicateScaleFilter;

import static aimtolearn.Constants.*;
import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;

public abstract class BaseScreen extends JPanel {

	protected final Game game;

	private boolean active;

	protected BaseScreen(Game game) {
		this.game = game;

		this.setFocusable(true);
		this.setOpaque(true);
		this.setBackground(BLACK);

		this.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				game.onKeyDown(e);
				BaseScreen.this.onKeyDown(e);
			}
			public void keyReleased(KeyEvent e) {
				BaseScreen.this.onKeyUp(e);
			}
		});
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	protected void paintComponent(Graphics graphics) {

		Image rescale;
		Graphics2D g;
		if (game.getDesiredHeight() != MAIN_HEIGHT) {
			rescale = createImage(MAIN_WIDTH, MAIN_HEIGHT);
			g = (Graphics2D) rescale.getGraphics();
		}
		else {
			rescale = null;
			g = ((Graphics2D) graphics);
		}

		g.setColor(getBackground());
		g.fillRect(0, 0, MAIN_WIDTH, MAIN_HEIGHT);
		g.setColor(WHITE);
		g.setFont(PIXEL_FONT);
		g.setStroke(MAIN_STROKE);

		updateScreen(g);

		if (rescale != null) {
			ReplicateScaleFilter scaleFilter = new ReplicateScaleFilter(game.getDesiredWidth(), game.getDesiredHeight());
			FilteredImageSource fis = new FilteredImageSource(rescale.getSource(), scaleFilter);
			graphics.drawImage(createImage(fis), 0, 0, null);
		}

		Toolkit.getDefaultToolkit().sync();
	}

	protected abstract void updateScreen(Graphics g);

	public abstract void tick();

	protected void onKeyDown(KeyEvent e) {}
	protected void onKeyUp(KeyEvent e) {}

}
