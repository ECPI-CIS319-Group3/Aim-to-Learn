package aimtolearn;

import javax.swing.SwingConstants;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Utils {

	/**
	 * Get a value from a given enum type
	 */
	public static <T extends Enum<T>> T getEnum(String name, Class<T> type) throws IllegalArgumentException {
		return Enum.valueOf(type, name.toUpperCase());
	}

	/**
	 * Draw a string centered vertically and horizontally within a given Rectangle
	 * @param text string to draw
	 * @param rect Rectangle to center within
	 * @param g graphics context
	 * @param verticalAlign one of {@code SwingConstants.BOTTOM}, {@code SwingConstants.CENTER}, {@code SwingConstants.TOP}
	 * @return the position of the centered text or {@code null} for a multi-line string
	 */
	public static Point text(String text, Rectangle rect, Graphics g, int verticalAlign) {
		return text(text, rect, null, g, verticalAlign);
	}

	/**
	 * Draw a string centered vertically and horizontally within a given Rectangle
	 * @param text string to draw
	 * @param rect Rectangle to center within
	 * @param bgColor text background color
	 * @param g graphics context
	 * @param verticalAlign one of {@code SwingConstants.BOTTOM}, {@code SwingConstants.CENTER}, {@code SwingConstants.TOP}
	 * @return the position of the centered text or {@code null} for a multi-line string
	 */
	public static Point text(String text, Rectangle rect, Color bgColor, Graphics g, int verticalAlign) {
		FontMetrics metrics = g.getFontMetrics();

		if (text.contains("\n")) {
			String[] lines = text.split("\n");
			int lineCount = lines.length;
			int translateHeight = metrics.getHeight() + Constants.MULTILINE_SPACING;
			rect = new Rectangle(rect);
			rect.translate(0, translateHeight * (lineCount-1) / -2);

			for (int i = 0; i < lineCount; i++) {
				String line = lines[i];
				Rectangle next = new Rectangle(rect);
				next.translate(0, i * translateHeight);
				text(line, next, bgColor, g, verticalAlign);
			}

			return null;
		}

		Rectangle2D textBounds = metrics.getStringBounds(text, g);

		int textWidth = (int) textBounds.getWidth();
		int textHeight = metrics.getHeight();
		int x = rect.x + (rect.width - textWidth)/2;

		int y;
		if (verticalAlign == SwingConstants.CENTER) y = rect.y + (rect.height - textHeight) / 2 + metrics.getAscent();
		else if (verticalAlign == SwingConstants.BOTTOM)  y = rect.y + rect.height - metrics.getDescent();
		else if (verticalAlign == SwingConstants.TOP) y = rect.y + textHeight - metrics.getDescent();
		else throw new IllegalArgumentException(
			"Alignment must be SwingConstants.BOTTOM, SwingConstants.CENTER, or SwingConstants.TOP");

		if (bgColor != null) {
			Color oldColor = g.getColor();
			g.setColor(bgColor);
			g.fillRect(x, y - metrics.getAscent(), textWidth, textHeight);
			g.setColor(oldColor);
		}

		g.drawString(text, x, y);

		return new Point(x, y - metrics.getHeight()/2);
	}
}
