package aimtolearn.screens;

import aimtolearn.Constants;
import aimtolearn.Game;
import aimtolearn.Sound;
import aimtolearn.Utils;

import javax.swing.SwingConstants;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.FilteredImageSource;
import java.awt.image.ReplicateScaleFilter;

import static aimtolearn.Constants.*;

public class SplashScreen extends BaseScreen {

	private Image largeShip;

	private final Point logoPos, shipPos;
	private final Rectangle textBounds;

	private static final double LARGE_SHIP_RATIO = 2.5;
	private static final Image LOGO_IMAGE = Constants.getImage("splash_logo.png");

	public SplashScreen(Game game) {
		super(game);

		int w = (int) (SHIP_WIDTH * LARGE_SHIP_RATIO);
		int h = (int) (SHIP_HEIGHT * LARGE_SHIP_RATIO);

		ReplicateScaleFilter scaleFilter = new ReplicateScaleFilter(w, h);
		FilteredImageSource fis = new FilteredImageSource(SHIP_IMAGE.getSource(), scaleFilter);

		this.largeShip = createImage(fis);
		this.logoPos = new Point((MAIN_WIDTH - LOGO_IMAGE.getWidth(null)) / 2, 100);
		this.shipPos = new Point((MAIN_WIDTH - largeShip.getWidth(null)) / 2, 350);
		this.textBounds = new Rectangle(0, shipPos.y + h + 100, MAIN_WIDTH, 100);
	}

	@Override
	protected void onKeyDown(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			game.confirmQuit(this);
		else
			game.setDisplayPanel(game.MAIN_MENU);

		Sound.MENU_SELECT.play();
	}

	@Override
	protected void updateScreen(Graphics g) {
		g.drawImage(LOGO_IMAGE, logoPos.x, logoPos.y, this);

		g.drawImage(largeShip, shipPos.x, shipPos.y, this);

		g.setFont(g.getFont().deriveFont(40f));

		Utils.text("Press any key to begin...", textBounds, g, SwingConstants.CENTER);
	}

	@Override
	public void tick() {}
}
