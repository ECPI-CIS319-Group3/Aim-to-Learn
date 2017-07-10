package aimtolearn;

import aimtolearn.screens.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.function.IntConsumer;

import static aimtolearn.Constants.AR;

public class Game extends JFrame {

	private GamePanel activePanel = null;

	public final MainMenu MAIN_MENU;
	public final GameplayScreen GAMEPLAY_SCREEN;
	public final ShootingPromptScreen SHOOTING_PROMPT_SCREEN;

	private int desiredHeight;
	private int desiredWidth;

	private static final Integer[] HEIGHTS = {720, 900, 1080};

	public Game() {
		this.MAIN_MENU = new MainMenu(this);
		this.GAMEPLAY_SCREEN = new GameplayScreen(this);
		this.SHOOTING_PROMPT_SCREEN = new ShootingPromptScreen(this);

		setDisplayPanel(MAIN_MENU);

		int res = -1;
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		if (screen.width > screen.height) {
			for (int h : HEIGHTS) {
				if (h > screen.height) break;
				res = h;
			}
		}
		else throw new UnsupportedOperationException("Not implemented yet"); // TODO implement this

		setRes(res);

		this.setTitle("Aim to Learn");
		this.setResizable(false);
		this.setUndecorated(true);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		this.setVisible(true);

	}

	public void setDisplayPanel(GamePanel panel) {
		this.activePanel = panel;
		this.setContentPane(activePanel);
		this.revalidate();
		activePanel.requestFocusInWindow();
	}

	public void shootingOption(String prompt, String[] options, IntConsumer onSelection) {
		shootingOption(prompt, options, null, onSelection);
	}

	public void shootingOption(String prompt, String[] options, int[] disabledIndexes, IntConsumer onSelection) {
		setDisplayPanel(SHOOTING_PROMPT_SCREEN);
		SHOOTING_PROMPT_SCREEN.setup(prompt, options, disabledIndexes, onSelection);
	}

	public void changeRes() {
		int option = JOptionPane.showOptionDialog(this,
			"Choose resolution", "Resolution",
			JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
			HEIGHTS, HEIGHTS[0]);

		if (option != JOptionPane.CLOSED_OPTION)
			setRes(HEIGHTS[option]);
	}

	private void setRes(int h) {
		this.desiredHeight = h;
		this.desiredWidth = (int) (desiredHeight * AR);
		this.setSize(desiredWidth, desiredHeight);
		this.setLocationRelativeTo(null);
	}

	public void quit() {
		int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?", "Confirm Quit",
			JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

		if (confirm == 0) System.exit(0);
	}

	public int getDesiredHeight() {
		return desiredHeight;
	}

	public int getDesiredWidth() {
		return desiredWidth;
	}

}
