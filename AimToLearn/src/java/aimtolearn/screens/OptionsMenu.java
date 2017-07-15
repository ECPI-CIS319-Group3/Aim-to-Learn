package aimtolearn.screens;

import aimtolearn.Game;
import aimtolearn.Sound;
import aimtolearn.Utils;

import javax.swing.SwingConstants;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;

public class OptionsMenu extends BaseMenu {

	private BaseScreen returnScreen;

	private static final String[] OPTIONS =
		new String[]{"Master Volume", "Sound FX Volume", "Music Volume", "Resolution", "Move Screen"};
	private static final int LEFT_MARGIN = 50, LEFT_WIDTH = 600, BAR_RIGHT_MARGIN = 50, BAR_NUM_WIDTH = 100,
		BAR_LENGTH = 600, BAR_HEIGHT = 30, BAR_THICKNESS = 10;

	private static final List<Integer> LEFT_KEYS = Arrays.asList(KeyEvent.VK_LEFT, KeyEvent.VK_A);
	private static final List<Integer> RIGHT_KEYS = Arrays.asList(KeyEvent.VK_RIGHT, KeyEvent.VK_D);

	private static final Color VOLUME_WARN = new Color(68, 0, 0);

	public OptionsMenu(Game game) {
		super(game, OPTIONS, "Options");
		setChoiceHeight(150);
	}

	@Override
	protected Rectangle makeChoiceBounds(int index) {
		Rectangle orig = super.makeChoiceBounds(index);
		orig.setBounds(LEFT_MARGIN, orig.y, LEFT_WIDTH, orig.height);
		return orig;
	}

	@Override
	protected void onKeyDown(KeyEvent e) {

		if (LEFT_KEYS.contains(e.getKeyCode())) {
			onDirection(-1);
			Sound.MENU_MOVE.play();
		}
		else if (RIGHT_KEYS.contains(e.getKeyCode())) {
			onDirection(1);
			Sound.MENU_MOVE.play();
		}

		super.onKeyDown(e);
	}

	// this isn't the greatest, but optimization in a menu like this isn't essential
	@Override
	protected void updateScreen(Graphics graphics) {
		super.updateScreen(graphics);

		Graphics2D g = ((Graphics2D) graphics);

		Point[] points = getChoicePoints();
		int[] volumes = {Sound.getMasterVolume(), Sound.getFxVolume(), Sound.getMusicVolume()};

		int x = LEFT_MARGIN + LEFT_WIDTH;
		int numX = x + BAR_LENGTH + BAR_RIGHT_MARGIN;

		g.setStroke(new BasicStroke(BAR_THICKNESS));

		for (int i = 0; i < 3; i++) {
			int y = points[i].y;
			int vol = volumes[i];

			g.drawLine(x, y, x + BAR_LENGTH, y);

			int barX = x + (int) (BAR_LENGTH * vol / 100.0);
			g.setColor(vol > 100 ? VOLUME_WARN : Color.GRAY);
			g.drawLine(barX, y - BAR_HEIGHT/2, barX, y + BAR_HEIGHT/2);

			g.setColor(Color.WHITE);
			Utils.text(""+vol,
				new Rectangle(numX, y - BAR_HEIGHT/2, BAR_NUM_WIDTH, BAR_HEIGHT),
				g, SwingConstants.CENTER);
		}

	}

	private void onDirection(int dir) {

		int index = getSelectedIndex();

		if (index < 3) { // first 3 are volumes
			int step = Sound.STEP_SIZE * dir;
			if (index == 0) Sound.setMasterVolume(Sound.getMasterVolume() + step);
			else if (index == 1) Sound.setFxVolume(Sound.getFxVolume() + step);
			else if (index == 2) Sound.setMusicVolume(Sound.getMusicVolume() + step);
		}
		else if (index == 3) { // 4th is resolution

		}
	}

	public void setReturnScreen(BaseScreen returnScreen) {
		this.returnScreen = returnScreen;
	}

	@Override
	protected void onEscape() {
		if (returnScreen != null) {
			game.setDisplayPanel(returnScreen);
			returnScreen.setActive(true);
		}
	}

	@Override
	public boolean onSelection(int index) {
		return index >= 4; // only beep if not on volume/resolution items
	}
}
