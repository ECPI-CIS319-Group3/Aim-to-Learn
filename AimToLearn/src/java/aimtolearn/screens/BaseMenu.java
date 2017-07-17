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
	private boolean movementEnabled;

	private String titleText;
	private Image titleImage;
	private List<Integer> disabledIndexes;
	private Font titleFont, optionFont;

	private Rectangle titleBounds;
	private int topHeight, choiceHeight, extraSpacing;

	private Rectangle[] choiceBounds;
	private Point[] choicePoints;

	private static final List<Integer> UP_KEYS = Arrays.asList(KeyEvent.VK_UP, KeyEvent.VK_W);
	private static final List<Integer> DOWN_KEYS = Arrays.asList(KeyEvent.VK_DOWN, KeyEvent.VK_S);
	private static final List<Integer> SELECT_KEYS = Arrays.asList(KeyEvent.VK_ENTER, KeyEvent.VK_SPACE);

	private static final int TRIANGLE_MARGIN = 10;
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
		this.movementEnabled = true;
		setChoiceHeight(75);
		setTopHeight(250);
		setTitleFontSize(64);
		setExtraSpacing(0);
		setOptionFontSize((int) MAIN_FONT);
	}

	public void reset() {
		this.selectedIndex = movementEnabled ? 0 : -1;
		repaint();
	}

	protected void setDisabledIndexes(Integer... disabled) {
		this.disabledIndexes = Arrays.asList(disabled);
	}

	public void setMovementEnabled(boolean movementEnabled) {
		this.movementEnabled = movementEnabled;
		reset();
	}

	protected void setTitleFontSize(int size) {
		this.titleFont = PIXEL_FONT.deriveFont((float) size);
	}

	protected void setOptionFontSize(int size) {
		this.optionFont = PIXEL_FONT.deriveFont((float) size);
	}

	protected void setTopHeight(int topHeight) {
		this.topHeight = topHeight;
		this.titleBounds = new Rectangle(0, 0, MAIN_WIDTH, topHeight);
		updateBounds();
	}

	protected void setChoiceHeight(int choiceHeight) {
		this.choiceHeight = choiceHeight;
		updateBounds();
	}

	protected void setExtraSpacing(int extraSpacing) {
		this.extraSpacing = extraSpacing;
		updateBounds();
	}

	private void updateBounds() {
		this.choiceBounds = new Rectangle[choices.length];
		this.choicePoints = new Point[choices.length];

		for (int i = 0; i < choices.length; i++)
			this.choiceBounds[i] = makeChoiceBounds(i);
	}

	@Override
	protected void onKeyDown(KeyEvent e) {

		if (!movementEnabled) return;

		int key = e.getKeyCode();
		if (UP_KEYS.contains(key)) {
			selectedIndex = selectedIndex == 0 ? choices.length - 1 : selectedIndex - 1;
			Sound.MENU_MOVE.play();
		}
		else if (DOWN_KEYS.contains(key)) {
			selectedIndex = selectedIndex == choices.length - 1 ? 0 : selectedIndex + 1;
			Sound.MENU_MOVE.play();
		}
		else if (SELECT_KEYS.contains(key) && !disabledIndexes.contains(selectedIndex)) {
			boolean doBeep = onSelection(selectedIndex);
			if (doBeep) Sound.MENU_SELECT.play();
		}
		else if (key == KeyEvent.VK_ESCAPE) {
			onEscape();
			Sound.MENU_SELECT.play();
		}

		repaint();
	}

	protected Rectangle makeChoiceBounds(int index) {
		return new Rectangle(0, topHeight + extraSpacing + index * choiceHeight, MAIN_WIDTH, choiceHeight);
	}

	protected Point[] getChoicePoints() {
		return choicePoints;
	}

	protected int getSelectedIndex() {
		return selectedIndex;
	}

	protected void onEscape() {}

	@Override
	protected void updateScreen(Graphics g) {

		if (titleText == null) {
			int logoX = (MAIN_WIDTH - titleImage.getWidth(null)) / 2;
			g.drawImage(titleImage, logoX, 100, this);
		}
		else {
			g.setFont(titleFont);
			Utils.text(titleText, titleBounds, g, SwingConstants.CENTER);
		}

		g.setFont(optionFont);

		for (int i = 0; i < choices.length; i++) {

			if (disabledIndexes.contains(i)) g.setColor(Color.GRAY);
			else g.setColor(Color.WHITE);

			Point pos = Utils.text(choices[i], choiceBounds[i], g, SwingConstants.CENTER); //BOTTOM);

			this.choicePoints[i] = pos;

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

	protected abstract boolean onSelection(int index);
}
