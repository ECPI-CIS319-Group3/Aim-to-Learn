package aimtolearn;

/**
 * This class manages the game's update loop, which runs every 10 ms, 100 times per second
 */
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

	/**
	 * Start the update loop
	 */
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

		// track the last tick time so that we can account for inaccuracies that may build over time
		lastStartTime = System.currentTimeMillis();

		while (running) {

			// every tick, update the active panel
			if (game.getActivePanel().isActive())
				game.getActivePanel().tick();

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
