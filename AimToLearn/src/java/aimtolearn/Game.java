package aimtolearn;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import java.awt.*;
import java.awt.geom.Rectangle2D;

import static aimtolearn.Constants.AR;

public class Game extends JFrame {

	private GamePanel activePanel = null;
	private MainMenu mainMenu;
	private GameplayScreen gameplayScreen;

	private int desiredHeight;
	private int desiredWidth;

	private static final Integer[] HEIGHTS = {720, 900, 1080};

	public Game() {
		this.mainMenu = new MainMenu(this);
		this.gameplayScreen = new GameplayScreen(this);

		setDisplayPanel(mainMenu);

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

	public GameplayScreen getGameplayScreen() {
		return gameplayScreen;
	}

	/**
	 * Draw a string centered vertically and horizontally within a given Rectangle
	 * @param text string to draw
	 * @param rect Rectangle to center within
	 * @param g graphics context
	 * @param align one of {@code SwingConstants.BOTTOM}, {@code SwingConstants.CENTER}, {@code SwingConstants.TOP}
	 * @return the position of the centered text
	 */
	public static Point text(String text, Rectangle rect, Graphics g, int align) {
		return text(text, rect, null, g, align);
	}

	/**
	 * Draw a string centered vertically and horizontally within a given Rectangle
	 * @param text string to draw
	 * @param rect Rectangle to center within
	 * @param bgColor text background color
	 * @param g graphics context
	 * @param align one of {@code SwingConstants.BOTTOM}, {@code SwingConstants.CENTER}, {@code SwingConstants.TOP}
	 * @return the position of the centered text
	 */
	public static Point text(String text, Rectangle rect, Color bgColor, Graphics g, int align) {
		FontMetrics metrics = g.getFontMetrics();
		Rectangle2D textBounds = metrics.getStringBounds(text, g);

		int textWidth = (int) textBounds.getWidth();
		int textHeight = metrics.getHeight();
		int x = rect.x + (rect.width - textWidth)/2;

		int y;
		if (align == SwingConstants.CENTER) y = rect.y + (rect.height - textHeight) / 2 + metrics.getAscent();
		else if (align == SwingConstants.BOTTOM)  y = rect.y + rect.height - metrics.getDescent();
		else if (align == SwingConstants.TOP) y = rect.y + textHeight - metrics.getDescent();
		else throw new IllegalArgumentException(
			"Alignment must be SwingConstants.BOTTOM, SwingConstants.CENTER, or SwingConstants.TOP");

		if (bgColor != null) {
			Color oldColor = g.getColor();
			g.setColor(bgColor);
			g.fillRect(x, y - metrics.getAscent(), textWidth, textHeight);
			g.setColor(oldColor);
		}

		g.drawString(text, x, y);

		return new Point(x, y - (int)(metrics.getHeight()/2.0));
	}
}
