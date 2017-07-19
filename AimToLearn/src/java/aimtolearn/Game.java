package aimtolearn;

import aimtolearn.screens.*;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import static aimtolearn.Constants.*;

/**
 * The main program window
 */
public class Game extends JFrame {

	// the currently-active JPanel
	private BaseScreen activePanel = null;

	// the public screens used by this and other classes
	public final MainMenu MAIN_MENU;
	public final TutorialScreen TUTORIAL_SCREEN;
	public final CreditsScreen CREDITS_SCREEN;
	public final PauseMenu PAUSE_MENU;
	public final GameplayScreen GAMEPLAY_SCREEN;
	public final ContinueShootingMenu CONTINUE_SCREEN;
	public final SubjectShootingMenu SUBJECT_SCREEN;
	public final ConfirmReturnMenu CONFIRM_RETURN_MENU;
	public final GameOverScreen GAME_OVER_SCREEN;
	public final MoveScreen MOVE_SCREEN;

	// private screens used only by this class
	private final ConfirmQuitMenu CONFIRM_QUIT_SCREEN;
	private final HowToPlayScreen HOW_TO_SCREEN;
	private final OptionsMenu OPTIONS_MENU;

	// the current visible width/height - contents are scaled to match this size
	private int desiredHeight;
	private int desiredWidth;

	public Game() {
		// initialize all screens, which all take this Game instance as their parameter
		this.MAIN_MENU = new MainMenu(this);
		this.PAUSE_MENU = new PauseMenu(this);

		this.GAMEPLAY_SCREEN = new GameplayScreen(this);
		this.CONTINUE_SCREEN = new ContinueShootingMenu(this);
		this.SUBJECT_SCREEN = new SubjectShootingMenu(this);

		this.TUTORIAL_SCREEN = new TutorialScreen(this);
		this.CREDITS_SCREEN = new CreditsScreen(this);
		this.HOW_TO_SCREEN = new HowToPlayScreen(this);
		this.OPTIONS_MENU = new OptionsMenu(this);
		this.MOVE_SCREEN = new MoveScreen(this);

		this.CONFIRM_RETURN_MENU = new ConfirmReturnMenu(this);
		this.CONFIRM_QUIT_SCREEN = new ConfirmQuitMenu(this);

		this.GAME_OVER_SCREEN = new GameOverScreen(this);

		// preload sounds
		Sound.init();

		// splash screen is only shown once, so no need to have a field for it
		SplashScreen splashScreen = new SplashScreen(this);
		setDisplayPanel(splashScreen);

		// start the update thread
		GameLoop loop = new GameLoop(this);
		loop.start();

		// set resolution to 1600x900 (the res everything was built for), or
		// to 1280x720 if the screen height is too small
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setResolution(screen.height < HEIGHTS[1] ? HEIGHTS[0] : HEIGHTS[1]);

		// set this frame's options
		this.setTitle("Aim to Learn");
		this.setResizable(false); // disable resizing
		this.setUndecorated(true); // remove border and top bar
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	/**
	 * Sets the currently visible JPanel and gives it focus
	 * @param panel the BaseScreen instance to set visible
	 */
	public void setDisplayPanel(BaseScreen panel) {
		this.activePanel = panel;
		this.setContentPane(activePanel);
		this.revalidate(); // this is needed when changing components on-the-fly
		activePanel.requestFocusInWindow();

		if (panel instanceof SplashScreen || panel instanceof MainMenu) Sound.BG_MUSIC_V1.loop();
		else if (panel instanceof GameplayScreen) Sound.BG_MUSIC_V2.loop();
	}

	/**
	 * Show the "how to play" tutorial. Only called from the main menu
	 */
	public void howToPlay(BaseScreen returnScreen) {
		setDisplayPanel(HOW_TO_SCREEN);
		HOW_TO_SCREEN.setReturnScreen(returnScreen);
	}

	/**
	 * Show the options screen given a screen to return to after closing it
	 * @param returnScreen the BaseScreen to return to
	 */
	public void openOptions(BaseScreen returnScreen) {
		setDisplayPanel(OPTIONS_MENU);
		OPTIONS_MENU.setReturnScreen(returnScreen);
		OPTIONS_MENU.init();
		OPTIONS_MENU.reset();
	}

	/**
	 * Show the quit confirmation screen given a screen to return to when hitting cancel
	 * @param returnScreen the BaseScreen to return to
	 */
	public void confirmQuit(BaseScreen returnScreen) {
		CONFIRM_QUIT_SCREEN.setReturnScreen(returnScreen);
		setDisplayPanel(CONFIRM_QUIT_SCREEN);
	}

	/**
	 * Set the resolution by its height. Width is calculated from that
	 * @param height the height to switch to
	 */
	public void setResolution(int height) {
		this.desiredHeight = height;
		this.desiredWidth = (int) (desiredHeight * AR);
		this.setSize(desiredWidth, desiredHeight);
		this.setLocationRelativeTo(null); // this centers the window in the screen
	}

	/**
	 * Delegated method from the active screen's key listener to handle some debug keybinds
	 */
	public void onKeyDown(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_F10) System.exit(0);
		else if (key == KeyEvent.VK_F9) setDisplayPanel(GAME_OVER_SCREEN);
	}

	// === misc. getters ===

	public int getDesiredHeight() {
		return desiredHeight;
	}

	public int getDesiredWidth() {
		return desiredWidth;
	}

	public BaseScreen getActivePanel() {
		return activePanel;
	}

}
