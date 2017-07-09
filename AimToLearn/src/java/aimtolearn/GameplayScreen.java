package aimtolearn;

import javax.swing.SwingConstants;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static aimtolearn.Constants.*;
import static java.awt.event.KeyEvent.*;

public class GameplayScreen extends GamePanel {

	private Ship ship;
	private QuestionSet questionSet;
	private Question currentQuestion;

	private int score, level, round;

	private long lastShotTime = 0;
	private long lastAnswerSpawnTime = 0;

	private Map<Integer, Boolean> activeKeys = new HashMap<>();
	private final List<Rectangle> shots = new CopyOnWriteArrayList<>();
	private final List<AnswerSprite> answers = new CopyOnWriteArrayList<>();

	private final int[] RIGHT_KEYS = {VK_RIGHT, VK_D};
	private final int[] LEFT_KEYS = {VK_LEFT, VK_A};
	private final int[] FIRE_KEYS = {VK_UP, VK_W};

	private static final Dimension SHOT_SIZE = new Dimension(10, 40);

	private static final int
		TOP = 150, TOP_MARGIN = 25,
		BOX_WIDTH = 100, TEXT_MARGIN = 10,
		SMALL_FONT = 16, LARGE_FONT = 50,
		SHIP_SPEED = 5, SHOT_SPEED = 10, ANSWER_SPEED = 2, FIRE_DELAY = 500,
		LEFT_BOUND = Ship.WIDTH / 2, RIGHT_BOUND = MAIN_WIDTH - LEFT_BOUND,
		ANSWER_SPAWN_RATE = 1000;

	public GameplayScreen(Game game) {
		super(game);

		this.score = 0;
		this.level = 1;
		this.round = 1;

		this.ship = new Ship(MAIN_WIDTH / 2);
		this.questionSet = new QuestionSet();

		this.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				activeKeys.put(e.getKeyCode(), true);

				if (e.getKeyCode() == VK_ESCAPE) game.quit(); // TODO temporary
			}
			public void keyReleased(KeyEvent e) {
				activeKeys.put(e.getKeyCode(), false);
			}
		});

		this.currentQuestion = questionSet.getQuestion(Question.Subject.MATH, Question.Difficulty.EASY);

		GameLoop loop = new GameLoop();
		loop.start();
	}

	@Override
	protected void updateScreen(Graphics graphics) {

		Graphics2D g = ((Graphics2D) graphics);

		if (System.currentTimeMillis() - lastAnswerSpawnTime >= ANSWER_SPAWN_RATE) {
			this.lastAnswerSpawnTime = System.currentTimeMillis();
			String randomAnswer = currentQuestion.randomAnswer();
			AnswerSprite sprite = new AnswerSprite(randomAnswer, g);
			this.answers.add(sprite);
		}

		for (AnswerSprite answer : answers) {
			answer.draw(g);
			g.setColor(Color.GRAY);
			g.draw(answer.getBounds());
			g.setColor(Color.WHITE);
		}

		// draw the top interface

		// TODO top interface needs solid backgrounds behind boxes/text
		Rectangle levelBox = new Rectangle(TOP_MARGIN, TOP_MARGIN, BOX_WIDTH, TOP);
		Rectangle roundBox = new Rectangle((int) (levelBox.getMaxX()), TOP_MARGIN, BOX_WIDTH, TOP);
		Rectangle scoreBox = new Rectangle(MAIN_WIDTH - TOP_MARGIN - 2*BOX_WIDTH, TOP_MARGIN, 2*BOX_WIDTH, TOP);
		Rectangle[] boxes = new Rectangle[]{levelBox, roundBox, scoreBox};

		for (Rectangle box : boxes) {
			g.draw(box);
			box.translate(0, TEXT_MARGIN);
		}

		g.setFont(g.getFont().deriveFont((float) SMALL_FONT));
		Game.text("Level", levelBox, g, SwingConstants.TOP);
		Game.text("Round", roundBox, g, SwingConstants.TOP);
		Game.text("Score", scoreBox, g, SwingConstants.TOP);

		for (Rectangle box : boxes) {
			box.translate(0, SMALL_FONT);
			box.setSize((int) box.getWidth(), (int) box.getHeight() - TEXT_MARGIN - SMALL_FONT);
		}

		g.setFont(g.getFont().deriveFont((float) LARGE_FONT));
		Game.text(""+level, levelBox, g, SwingConstants.CENTER);
		Game.text(""+score, scoreBox, g, SwingConstants.CENTER);
		Game.text(""+round, roundBox, g, SwingConstants.CENTER);

		Rectangle questionBox = new Rectangle(
			(int) (roundBox.getMaxX() + TOP_MARGIN),
			TOP_MARGIN,
			MAIN_WIDTH - 4 * TOP_MARGIN - 4 * BOX_WIDTH,
			TOP
		);

		g.setFont(g.getFont().deriveFont(((float) FONT_SIZE)));
		Game.text(currentQuestion.questionPrompt, questionBox, g, SwingConstants.CENTER);

		// draw the shots

		for (Rectangle shotLoc : shots)
			g.fill(shotLoc);

		// draw the ship

		ship.draw(g);

	}

	private boolean keyDown(int... keyNumbers) {
		return Arrays.stream(keyNumbers)
			.anyMatch(key -> activeKeys.getOrDefault(key, false));
	}

	/**
	 * Called every game tick to update positions and such
	 */
	private void tick() {

		ship.tick();

		// prevent both left and right from being held down together
		if (!(keyDown(RIGHT_KEYS) && keyDown(LEFT_KEYS))) {

			int shipX = ship.getX();

			if (keyDown(RIGHT_KEYS)) { // if right is down, move right
				shipX += SHIP_SPEED;
				ship.setDirection(Ship.DIR_RIGHT);
			}
			else if (keyDown(LEFT_KEYS)) { // if left is down, move left
				shipX -= SHIP_SPEED;
				ship.setDirection(Ship.DIR_LEFT);
			}
			else {
				ship.setDirection(Ship.DIR_NONE);
			}

			if (shipX > RIGHT_BOUND) shipX = RIGHT_BOUND;
			if (shipX < LEFT_BOUND) shipX = LEFT_BOUND;

			ship.setX(shipX);
		}

		if (keyDown(FIRE_KEYS)) {
			if (System.currentTimeMillis() - lastShotTime >= FIRE_DELAY) {// auto-fire every [x]ms
				fireShot();
				this.lastShotTime = System.currentTimeMillis();
			}
		}

		for (Rectangle shotLoc : shots) {
			if (shotLoc.getY() < 0) {
				shots.remove(shotLoc);
			}
			else
				shotLoc.translate(0, -SHOT_SPEED);
		}

		for (AnswerSprite answer : answers) {
			if (answer.getBounds().getY() > MAIN_HEIGHT - answer.getBounds().getHeight()) {
				answers.remove(answer);
			}
			else {
				boolean collided = false;

				for (Rectangle shot : shots) {
					if (answer.getBounds().intersects(shot)) {
						shots.remove(shot);
						onAnswerHit(answer);
						collided = true;
						break;
					}
				}

				if (!collided) answer.moveDown(ANSWER_SPEED);
			}
		}

		repaint();
	}

	private void onAnswerHit(AnswerSprite answer) {
		if (currentQuestion.isCorrect(answer.getText())) {
			answers.clear();
			shots.clear();
			this.currentQuestion = questionSet.getQuestion(Question.Subject.MATH, Question.Difficulty.EASY);
			this.score++;
		}
		else {
			answers.remove(answer);
			if (score > 0) this.score--;
		}
	}

	private void fireShot() {
		int x = (int) (ship.getX() - SHOT_SIZE.getWidth() / 2);
		int y = (int) (SHIP_Y - Ship.HEIGHT / 2 - SHOT_SIZE.getHeight());

		shots.add(new Rectangle(new Point(x, y), SHOT_SIZE));
	}

	private class GameLoop implements Runnable {

		private boolean running;
		private Thread thread;

		GameLoop() {
			this.running = false;
			this.thread = new Thread(this);
		}

		public void start() {
			this.running = true;
			thread.start();
		}

		@Override
		public void run() {

			final int delay = 1000 / Constants.TICK_RATE;
			long lastStartTime, offset, sleepTime;

			lastStartTime = System.currentTimeMillis();

			while (running) {
				tick();

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
}
