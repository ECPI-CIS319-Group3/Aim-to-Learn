package aimtolearn;

import javax.swing.SwingUtilities;

public class Run {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			Game game = new Game();
		});
	}

}
