package aimtolearn.screens;

import aimtolearn.Constants;
import aimtolearn.Game;
import aimtolearn.Sound;
import aimtolearn.Utils;

import javax.swing.SwingConstants;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static aimtolearn.Constants.HEIGHTS;
import static aimtolearn.Constants.MAIN_STROKE;

public class OptionsMenu extends BaseMenu {

	private BaseScreen returnScreen;
	private int visibleResIndex;

	private static final String[] OPTIONS =
		new String[]{"Master Volume", "Sound FX Volume", "Music Volume", "Resolution", "Move Screen"};
	private static final int MARGIN = 50, LEFT_WIDTH = 600, BAR_RIGHT_MARGIN = 50, BAR_NUM_WIDTH = 100,
		BAR_LENGTH = 600, BAR_HEIGHT = 30, BAR_THICKNESS = 10, RES_PADDING = 20;
	private static final List<Integer> LEFT_KEYS = Arrays.asList(KeyEvent.VK_LEFT, KeyEvent.VK_A);
	private static final List<Integer> RIGHT_KEYS = Arrays.asList(KeyEvent.VK_RIGHT, KeyEvent.VK_D);
	private static final String[] RESOLUTIONS = {"1280x720", "1600x900", "1920x1080"};
	private static final Color VOLUME_WARN = new Color(68, 0, 0);

	public OptionsMenu(Game game) {
		super(game, OPTIONS, "Options");
		setChoiceHeight(100);
	}

	public void init() {
		this.visibleResIndex = Arrays.binarySearch(HEIGHTS, game.getDesiredHeight());
	}

	@Override
	protected Rectangle makeChoiceBounds(int index) {
		Rectangle orig = super.makeChoiceBounds(index);

		if (index < 4) // 3 sound options and resolution option must be moved over
			orig.setBounds(MARGIN, orig.y, LEFT_WIDTH, orig.height);

		return orig;
	}

	@Override
	protected void onKeyDown(KeyEvent e) {

		boolean doBeep = false;

		if (LEFT_KEYS.contains(e.getKeyCode()))
			doBeep = onDirection(-1);
		else if (RIGHT_KEYS.contains(e.getKeyCode()))
			doBeep = onDirection(1);

		if (doBeep) Sound.MENU_MOVE.play();

		super.onKeyDown(e);
	}

	// this isn't the greatest, but optimization in a menu like this isn't essential
	@Override
	protected void updateScreen(Graphics graphics) {
		super.updateScreen(graphics);

		Graphics2D g = ((Graphics2D) graphics);

		Point[] points = getChoicePoints();
		int[] volumes = {Sound.getMasterVolume(), Sound.getFxVolume(), Sound.getMusicVolume()};

		int x = MARGIN + LEFT_WIDTH;
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

		g.setStroke(MAIN_STROKE);
		FontMetrics metrics = g.getFontMetrics();
		int totalWidth = 0;
		List<Integer> widths = new ArrayList<>();
		for (String res : RESOLUTIONS) {
			int w = metrics.stringWidth(res);
			totalWidth += w;
			widths.add(w);
		}

		int spacing = (Constants.MAIN_WIDTH - 2*MARGIN - LEFT_WIDTH - totalWidth) / (RESOLUTIONS.length+1);

		int y = points[3].y;
		for (int i = 0; i < RESOLUTIONS.length; i++) {
			g.setColor(Color.WHITE);

			String res = RESOLUTIONS[i];
			int h = metrics.getHeight() + 2*RES_PADDING;
			Rectangle box = new Rectangle(x, y - h/2, widths.get(i) + 2*RES_PADDING, h);

			if (i == visibleResIndex) g.draw(box);

			if (game.getDesiredHeight() == HEIGHTS[i]) g.setColor(Color.GRAY);
			else g.setColor(Color.WHITE);

			Utils.text(res, box, g, SwingConstants.CENTER);

			x += widths.get(i) + spacing;
		}

	}

	private boolean onDirection(int dir) {

		int index = getSelectedIndex();

		if (index < 3) { // first 3 are volumes
			int step = Sound.STEP_SIZE * dir;
			if (index == 0) Sound.setMasterVolume(Sound.getMasterVolume() + step);
			else if (index == 1) Sound.setFxVolume(Sound.getFxVolume() + step);
			else if (index == 2) Sound.setMusicVolume(Sound.getMusicVolume() + step);
		}
		else if (index == 3) { // 4th is resolution
			this.visibleResIndex += dir;
			if (visibleResIndex < 0) {
				visibleResIndex = 0;
				return false;
			}
			if (visibleResIndex >= HEIGHTS.length) {
				visibleResIndex = HEIGHTS.length - 1;
				return false;
			}
		}

		return true;
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
		if (index < 3) { // disable beep for selecting sound options
			return false;
		}
		else if (index == 3) { // if resolution option was selected
			game.setResolution(HEIGHTS[visibleResIndex]);
			init();
		}
		else if (index == 4) { // move window button
			game.setDisplayPanel(game.MOVE_SCREEN);
			game.MOVE_SCREEN.setReturnScreen(this);
		}
		return true;
	}
}
