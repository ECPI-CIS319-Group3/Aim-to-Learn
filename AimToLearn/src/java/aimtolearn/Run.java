package aimtolearn;

import javax.swing.SwingUtilities;

public class Run {

	// main method that starts the JFrame
	public static void main(String[] args) {

		// start the application on the Swing event thread, the way it should be started
		SwingUtilities.invokeLater(() -> {
			Game game = new Game();
			game.setVisible(true);
		});
	}

}
