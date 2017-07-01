package aimtolearn;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import static aimtolearn.Constants.AR;

public class Game extends JFrame {

	private GamePanel activePanel = null;
	private MainMenu mainMenu;
	private GameplayScreen gameplayScreen;

	private int desiredHeight;
	private int desiredWidth;

	public Game() {
		this.mainMenu = new MainMenu(this);
		this.gameplayScreen = new GameplayScreen(this);

		setDisplayPanel(mainMenu);
		setRes(720);

		this.setResizable(false);
		this.setUndecorated(true);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		this.setVisible(true);

	}

	public void setDisplayPanel(GamePanel panel) {
		this.activePanel = panel;
		this.setContentPane(activePanel);
		this.revalidate();
	}

	public void changeRes() {
		Integer[] ops = {/*96, 144,*/ 240, 360, 480, 720, 900};
		int op = JOptionPane.showOptionDialog(this,
			"Choose resolution", "Resolution",
			JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
			ops, ops[0]);

		int height = ops[op];

		if (height != JOptionPane.CLOSED_OPTION)
			setRes(height);
	}

	private void setRes(int h) {
		this.desiredHeight = h;
		this.desiredWidth = (int) (desiredHeight * AR);
		this.setSize(desiredWidth, desiredHeight);
		this.setLocationRelativeTo(null);
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
		FontMetrics metrics = g.getFontMetrics();
		Rectangle2D textBounds = metrics.getStringBounds(text, g);

		int x = rect.x + (int)(rect.width - textBounds.getWidth())/2;

		int y;
		if (align == SwingConstants.CENTER) y = rect.y + (rect.height - metrics.getHeight()) / 2 + metrics.getAscent();
		else if (align == SwingConstants.BOTTOM)  y = rect.y + rect.height - metrics.getDescent();
		else if (align == SwingConstants.TOP) y = rect.y + metrics.getHeight() - metrics.getDescent();
		else throw new IllegalArgumentException(
			"Alignment must be SwingConstants.BOTTOM, SwingConstants.CENTER, or SwingConstants.TOP");

		g.drawString(text, x, y);

		return new Point(x, y - (int)(metrics.getHeight()/2.0));
	}
}
