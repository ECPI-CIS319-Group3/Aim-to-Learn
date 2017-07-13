package aimtolearn.screens;

import aimtolearn.Constants;
import aimtolearn.Game;
import aimtolearn.Sound;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;

public class HowToPlayScreen extends BaseScreen {

	private int imgX, imgY;
	private Image howToPlayImg;
	private BaseScreen returnScreen;

	private static final Image HOW_TO_PLAY = Constants.getImage("how_to_play.png");

	public HowToPlayScreen(Game game) {
		super(game);

		Image i = HOW_TO_PLAY;
		this.imgX = (Constants.MAIN_WIDTH - i.getWidth(null)) / 2;
		this.imgY = (Constants.MAIN_HEIGHT - i.getHeight(null)) / 2;

		howToPlayImg = i; // TODO possibly rescale this image
	}

	public void setReturnScreen(BaseScreen returnScreen) {
		this.returnScreen = returnScreen;
	}

	@Override
	protected void onKeyDown(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE && returnScreen != null) {
			Sound.MENU_SELECT.play();
			game.setDisplayPanel(returnScreen);
			returnScreen.setActive(true);
		}
	}

	@Override
	protected void updateScreen(Graphics g) {
		g.drawImage(howToPlayImg, imgX, imgY, this);
	}

	@Override
	public void tick() {}
}
