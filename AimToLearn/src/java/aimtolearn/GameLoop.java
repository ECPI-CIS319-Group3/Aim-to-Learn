package aimtolearn;

public class GameLoop implements Runnable {

	private static GameLoop instance;
	private Game game;

	private boolean running, started;
	private Thread thread;

	public GameLoop(Game game) {
		if (instance != null)
			throw new IllegalStateException("Cannot instantiate GameLoop again.");

		this.game = game;
		this.running = false;
		this.started = false;
		this.thread = new Thread(this, this.getClass().getSimpleName());
		instance = this;
	}

	public void start() {

		this.running = true;

		if (!started) {
			thread.start();
			this.started = true;
		}
		else {
			throw new IllegalStateException("Cannot start twice.");
		}
	}

	@Override
	public void run() {

		final int delay = 1000 / Constants.TICK_RATE;
		long lastStartTime, offset, sleepTime;

		lastStartTime = System.currentTimeMillis();

		while (running) {
			if (game.getActivePanel().isActive())
				game.getActivePanel().tick();

			game.getOverlay().repaint();

			offset = System.currentTimeMillis() - lastStartTime;
			sleepTime = delay - offset;

			if (sleepTime < 0) sleepTime = 2;

			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				throw new RuntimeException("Game loop interrupted.", e);
			}

			lastStartTime = System.currentTimeMillis();
		}

	}
}
