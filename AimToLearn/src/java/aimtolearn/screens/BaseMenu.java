package aimtolearn.screens;

import aimtolearn.Game;
import aimtolearn.Sound;
import aimtolearn.Utils;

import javax.swing.SwingConstants;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static aimtolearn.Constants.*;

public abstract class BaseMenu extends BaseScreen {

	private String[] choices;

	private int selectedIndex;

	private String titleText;
	private Image titleImage;
	private List<Integer> disabledIndexes;

	private Rectangle titleBounds;

	private Rectangle[] choiceBounds;

	private static final List<Integer> UP_KEYS = Arrays.asList(KeyEvent.VK_UP, KeyEvent.VK_W);
	private static final List<Integer> DOWN_KEYS = Arrays.asList(KeyEvent.VK_DOWN, KeyEvent.VK_S);
	private static final List<Integer> SELECT_KEYS = Arrays.asList(KeyEvent.VK_ENTER, KeyEvent.VK_SPACE);

	private static final int CHOICE_HEIGHT = 75, TRIANGLE_MARGIN = 10;

	private static final Dimension TRIANGLE_SIZE = new Dimension(30, 20);

	protected BaseMenu(Game game, String[] choices, Image titleImage) {
		this(game, choices, null, titleImage);
	}

	protected BaseMenu(Game game, String[] choices, String titleText) {
		this(game, choices, titleText, null);
	}

	private BaseMenu(Game game, String[] choices, String titleText, Image titleImage) {
		super(game);
		this.choices = choices;
		this.selectedIndex = 0;
		this.titleText = titleText;
		this.titleImage = titleImage;
		this.disabledIndexes = new ArrayList<>();
		setTopHeight(250);
	}

	public void reset() {
		this.selectedIndex = 0;
		repaint();
	}

	protected void setDisabledIndexes(Integer... disabled) {
		this.disabledIndexes = Arrays.asList(disabled);
	}

	protected void setTopHeight(int topHeight) {

		this.titleBounds = new Rectangle(0, 0, MAIN_WIDTH, topHeight);
		this.choiceBounds = new Rectangle[choices.length];

		for (int i = 0; i < choices.length; i++) {
			this.choiceBounds[i] = new Rectangle(0, topHeight + i * CHOICE_HEIGHT, MAIN_WIDTH, CHOICE_HEIGHT);
		}
	}

	@Override
	protected void onKeyDown(KeyEvent e) {
		boolean moved = false, selected = false;

		if (UP_KEYS.contains(e.getKeyCode())) {
			selectedIndex = selectedIndex == 0 ? choices.length - 1 : selectedIndex - 1;
			moved = true;
		}
		else if (DOWN_KEYS.contains(e.getKeyCode())) {
			selectedIndex = selectedIndex == choices.length - 1 ? 0 : selectedIndex + 1;
			moved = true;
		}
		else if (SELECT_KEYS.contains(e.getKeyCode()) && !disabledIndexes.contains(selectedIndex)) {
			onSelection(selectedIndex);
			selected = true;
		}
		else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			onEscape();
			selected = true;
		}

		if (moved) Sound.MENU_MOVE.play();
		if (selected) Sound.MENU_SELECT.play();

		repaint();
	}

	protected void onEscape() {}

	@Override
	protected void updateScreen(Graphics g) {

		if (titleText == null) {
			int logoX = (MAIN_WIDTH - titleImage.getWidth(null)) / 2;
			g.drawImage(titleImage, logoX, 100, this);
		}
		else {
			g.setFont(PIXEL_FONT.deriveFont(64f));
			Utils.text(titleText, titleBounds, g, SwingConstants.CENTER);
		}

		g.setFont(PIXEL_FONT);

		for (int i = 0; i < choices.length; i++) {

			if (disabledIndexes.contains(i)) g.setColor(Color.GRAY);
			else g.setColor(Color.WHITE);

			Point pos = Utils.text(choices[i], choiceBounds[i], g, SwingConstants.BOTTOM);

			if (i == selectedIndex) {
				Polygon triangle = new Polygon(
					new int[]{-TRIANGLE_SIZE.width/2, 0, -TRIANGLE_SIZE.width/2},
					new int[]{-TRIANGLE_SIZE.height/2, 0, TRIANGLE_SIZE.height/2},
					3);
				triangle.translate(pos.x - TRIANGLE_MARGIN, pos.y);
				g.fillPolygon(triangle);
			}

		}
	}

	@Override
	public void tick() {}

	public abstract void onSelection(int index);
}
