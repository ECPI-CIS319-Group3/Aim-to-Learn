package aimtolearn.screens;

import aimtolearn.Constants;
import aimtolearn.Game;
import aimtolearn.Utils;

import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BaseShootingMenu extends ShipScreen {

	private String prompt;
	private String[] optionStrings;
	private boolean initiated;
	private List<Integer> disabledIndexes;

	private Rectangle[] options;

	private static final int PROMPT_HEIGHT = 200;
	private static final Rectangle PROMPT_BOUNDS = new Rectangle(0, 0, Constants.MAIN_WIDTH, PROMPT_HEIGHT);
	private static final float FONT_SIZE = 40;

	public BaseShootingMenu(Game game, String prompt, String... optionStrings) {
		super(game);
		this.prompt = prompt;
		this.optionStrings = optionStrings;
		this.options = new Rectangle[optionStrings.length];
		this.initiated = false;
		this.disabledIndexes = new ArrayList<>();
	}

	protected void setDisabledIndexes(Integer... disabled) {
		this.disabledIndexes = Arrays.asList(disabled);
	}

	public void init() {
		resetKeys();
		setActive(true);
	}

	@Override
	protected void updateScreen(Graphics g) {

		if (!isActive()) return;

		super.updateScreen(g);

		g.setFont(g.getFont().deriveFont(FONT_SIZE));

		if (!initiated) {
			FontMetrics metrics = g.getFontMetrics();
			List<Integer> optionWidths = new ArrayList<>();

			int optionsWidth = 0;
			int height = metrics.getHeight();

			for (String option : optionStrings) {
				int w = (int) metrics.getStringBounds(option, g).getWidth();
				optionsWidth += w;
				optionWidths.add(w);
			}

			int spacing = (Constants.MAIN_WIDTH - optionsWidth) / (optionStrings.length+1);
			int lastX = 0;

			for (int i = 0; i < options.length; i++) {
				Rectangle bounds = new Rectangle(lastX + spacing, PROMPT_HEIGHT, optionWidths.get(i), height);
				this.options[i] = bounds;
				lastX = (int) bounds.getMaxX();
			}

			this.initiated = true;
		}

		Utils.text(prompt, PROMPT_BOUNDS, g, SwingConstants.CENTER);

		for (int i = 0; i < options.length; i++) {
			if (disabledIndexes.contains(i))
				g.setColor(Color.GRAY);
			else
				g.setColor(Color.WHITE);

			Utils.text(optionStrings[i], options[i], g, SwingConstants.CENTER);
		}

	}

	@Override
	public void tick() {
		super.tick();

		for (int i = 0; i < options.length; i++) {

			for (Rectangle shot : shots) {

				if (shot.intersects(options[i])) {

					shots.remove(shot);

					if (!disabledIndexes.contains(i)) {
						this.initiated = false;
						setActive(false);
						onSelection(i);
					}
				}
			}

		}

		repaint();
	}

	protected abstract void onSelection(int index);


}
