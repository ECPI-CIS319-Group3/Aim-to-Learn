package aimtolearn;

import aimtolearn.screens.*;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static aimtolearn.Constants.AR;

public class Game extends JFrame {

	private BaseScreen activePanel = null;
	private GameLoop loop;
//	private AnimationOverlay animationOverlay;

	public final MainMenu MAIN_MENU;
	public final PauseMenu PAUSE_MENU;
	public final GameplayScreen GAMEPLAY_SCREEN;
	public final ContinueShootingMenu CONTINUE_SCREEN;
	public final SubjectShootingMenu SUBJECT_SCREEN;
	public final ConfirmReturnMenu CONFIRM_RETURN_MENU;
	public final GameOverScreen GAME_OVER_SCREEN;

	private final ConfirmQuitMenu CONFIRM_QUIT_SCREEN;
	private final HowToPlayScreen HOW_TO_SCREEN;
	private final OptionsMenu OPTIONS_MENU;

	private int desiredHeight;
	private int desiredWidth;
	private boolean windowMovementEnabled = false;

	private static final Integer[] HEIGHTS = {720, 900, 1080};

	public Game() {
		this.MAIN_MENU = new MainMenu(this);
		this.PAUSE_MENU = new PauseMenu(this);

		this.GAMEPLAY_SCREEN = new GameplayScreen(this);
		this.CONTINUE_SCREEN = new ContinueShootingMenu(this);
		this.SUBJECT_SCREEN = new SubjectShootingMenu(this);

		this.HOW_TO_SCREEN = new HowToPlayScreen(this);
		this.OPTIONS_MENU = new OptionsMenu(this);

		this.CONFIRM_RETURN_MENU = new ConfirmReturnMenu(this);
		this.CONFIRM_QUIT_SCREEN = new ConfirmQuitMenu(this);

		this.GAME_OVER_SCREEN = new GameOverScreen(this);

		SplashScreen splashScreen = new SplashScreen(this);
		setDisplayPanel(splashScreen);

		DragGlassPane glass = new DragGlassPane();
		this.setGlassPane(glass);
		glass.setVisible(true);

		Sound.init();

	//	this.animationOverlay = new AnimationOverlay(this);
	//	this.getLayeredPane().add(animationOverlay, new Integer(100));
	//	this.revalidate();

		this.loop = new GameLoop(this);
		loop.start();

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setRes(screen.width < HEIGHTS[1] ? HEIGHTS[0] : HEIGHTS[1]);

		this.setTitle("Aim to Learn");
		this.setResizable(false);
		this.setUndecorated(true);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public void setDisplayPanel(BaseScreen panel) {
		this.activePanel = panel;
		this.setContentPane(activePanel);
		this.revalidate();
		activePanel.requestFocusInWindow();
	}

	public void howToPlay(BaseScreen returnScreen) {
		setDisplayPanel(HOW_TO_SCREEN);
		HOW_TO_SCREEN.setReturnScreen(returnScreen);
	}

	public void optionsTemp() {

		setDisplayPanel(OPTIONS_MENU);
		OPTIONS_MENU.setReturnScreen(MAIN_MENU);
	/*	int option = JOptionPane.showOptionDialog(this,
			"Choose resolution", "Resolution",
			JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
			HEIGHTS, HEIGHTS[0]);

		if (option != JOptionPane.CLOSED_OPTION)
			setRes(HEIGHTS[option]);

		String volume = JOptionPane.showInputDialog(this, String.format("Input Volume (current: %d%%)", Sound.getVolume()),
			"Volume", JOptionPane.QUESTION_MESSAGE);
		Sound.setVolume(Integer.parseInt(volume));
		*/
	}

	private void setRes(int h) {
		this.desiredHeight = h;
		this.desiredWidth = (int) (desiredHeight * AR);
		this.setSize(desiredWidth, desiredHeight);
		this.setLocationRelativeTo(null);
	}

	public void confirmQuit(BaseScreen returnScreen) {
		CONFIRM_QUIT_SCREEN.setReturnScreen(returnScreen);
		setDisplayPanel(CONFIRM_QUIT_SCREEN);
	}

	public void onKeyDown(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_F10) System.exit(0);
	}

	public int getDesiredHeight() {
		return desiredHeight;
	}

	public int getDesiredWidth() {
		return desiredWidth;
	}

	public BaseScreen getActivePanel() {
		return activePanel;
	}

//	public AnimationOverlay getOverlay() {
//		return animationOverlay;
//	}

	private class DragGlassPane extends JComponent {

		private Point startClick;

		DragGlassPane() {

			this.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					startClick = e.getPoint();
					getComponentAt(startClick);
				}
			});

			this.addMouseMotionListener(new MouseAdapter() {
				public void mouseDragged(MouseEvent e) {

					if (!windowMovementEnabled || startClick == null) return;

					// get location of Window
					int thisX = Game.this.getLocation().x;
					int thisY = Game.this.getLocation().y;

					// Determine how much the mouse moved since the initial click
					int xMoved = (thisX + e.getX()) - (thisX + startClick.x);
					int yMoved = (thisY + e.getY()) - (thisY + startClick.y);

					// Move window to this position
					int X = thisX + xMoved;
					int Y = thisY + yMoved;
					Game.this.setLocation(X, Y);
				}
			});

		}

	}
}
