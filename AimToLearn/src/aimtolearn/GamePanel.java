package aimtolearn;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.FilteredImageSource;
import java.awt.image.ReplicateScaleFilter;

import static aimtolearn.Constants.*;
import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;

public abstract class GamePanel extends JPanel {

	protected final Game game;

	protected GamePanel(Game game) {
		this.game = game;

		this.setFocusable(true);
		this.setBackground(BLACK);
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
		g.fillRect(0, 0, Constants.MAIN_WIDTH, Constants.MAIN_HEIGHT);
		g.setColor(WHITE);
		g.setFont(Constants.PIXEL_FONT);

		updateScreen(g);

		if (game.getDesiredHeight() != MAIN_HEIGHT && rescale != null) {
			ReplicateScaleFilter scaleFilter = new ReplicateScaleFilter(game.getDesiredWidth(), game.getDesiredHeight());
			FilteredImageSource fis = new FilteredImageSource(rescale.getSource(), scaleFilter);
			graphics.drawImage(createImage(fis), 0, 0, null);
		}
	}

	protected abstract void updateScreen(Graphics g);

}
