package aimtolearn.screens;

import aimtolearn.Game;
import aimtolearn.Question;
import aimtolearn.Sound;
import aimtolearn.Utils;
import aimtolearn.sprites.AnimatedSprite;
import aimtolearn.sprites.AnswerSprite;
import aimtolearn.sprites.NumberBox;

import javax.swing.SwingConstants;
import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static aimtolearn.Constants.*;

public abstract class BaseGameplayScreen extends ShipScreen {

	private int score, level, round;
	private boolean ready, showInterface;
	private String centerBoxText;
	protected final List<AnswerSprite> answers = new CopyOnWriteArrayList<>();
	private Question currentQuestion;
	private int answerSpawnRate;

	private final NumberBox levelBox, roundBox, scoreBox;
	private final NumberBox[] numberBoxes;
	private final Rectangle centerBox;
	private Point answerExplosionLoc;
	private long lastAnswerSpawnTime = 0;

	protected static final int DEFAULT_ANSWER_SPAWN_RATE = 1000;
	private static final int TOP = 150;
	private static final int TOP_MARGIN = 25;
	private static final int BOX_WIDTH = 100;
	private static final int ANSWER_SPEED = 2;

	private static final AnimatedSprite ANSWER_HIT_OVERLAY_ANIM = new AnimatedSprite("answer_hit_explosion", 8, 400);

	protected BaseGameplayScreen(Game game) {
		super(game);

		this.score = 0;
		this.level = 1;
		this.round = 1;
		this.ready = false;
		this.showInterface = true;
		this.answerSpawnRate = DEFAULT_ANSWER_SPAWN_RATE;

		this.levelBox = new NumberBox("Level", TOP_MARGIN, TOP_MARGIN, BOX_WIDTH, TOP);
		this.roundBox = new NumberBox("Round", (int) (levelBox.getBounds().getMaxX()), TOP_MARGIN, BOX_WIDTH, TOP);
		this.scoreBox = new NumberBox("Score", MAIN_WIDTH - TOP_MARGIN - 2*BOX_WIDTH, TOP_MARGIN, 2*BOX_WIDTH, TOP);
		this.numberBoxes = new NumberBox[]{levelBox, roundBox, scoreBox};

		this.centerBox = new Rectangle(
			(int) (roundBox.getBounds().getMaxX() + TOP_MARGIN),
			TOP_MARGIN,
			MAIN_WIDTH - 4 * TOP_MARGIN - 4 * BOX_WIDTH,
			TOP
		);
	}

	public void init() {
		answers.clear();
		shots.clear();
		this.ready = true;
		this.score = 0;
		this.round = 1;
		this.level = 1;
		reset();
		setActive(true);
	}

	@Override
	protected void updateScreen(Graphics g) {

		super.updateScreen(g);
		
		if (currentQuestion != null && System.currentTimeMillis() - lastAnswerSpawnTime >= answerSpawnRate) {
			this.lastAnswerSpawnTime = System.currentTimeMillis();
			String randomAnswer = currentQuestion.randomAnswer();
			AnswerSprite sprite = new AnswerSprite(randomAnswer, g);
			this.answers.add(sprite);
		}

		for (AnswerSprite answer : answers) answer.draw(g);

		if (answerExplosionLoc != null && ANSWER_HIT_OVERLAY_ANIM.isRunning())
			ANSWER_HIT_OVERLAY_ANIM.draw(g, answerExplosionLoc.x, answerExplosionLoc.y);

		// draw the top interface
		if (showInterface) {
			Graphics2D g2 = ((Graphics2D) g);
			for (NumberBox box : numberBoxes) box.draw(g2);
		}

		g.setFont(g.getFont().deriveFont(MAIN_FONT));
		Utils.text(centerBoxText, centerBox, Color.BLACK, g, SwingConstants.CENTER);

	}

	@Override
	public void tick() {
		super.tick();

		ANSWER_HIT_OVERLAY_ANIM.tick();

		if (isFrozen()) return;

		for (AnswerSprite answer : answers) {

			Rectangle ansBounds = answer.getBounds();
			boolean remove = false;

			if (ansBounds.getY() > MAIN_HEIGHT - ansBounds.getHeight()) {
				remove = true;
			}
			else {

				for (Rectangle shot : shots) {
					if (ansBounds.intersects(shot)) {
						shots.remove(shot);
						onAnswerHit(answer, shot);
						remove = true;
						break;
					}
				}

				if (ship.getBounds().intersects(ansBounds)) {
					onShipHit(answer);
					remove = true;
				}

			}

			if (remove) answers.remove(answer);
			else answer.moveDown(ANSWER_SPEED);
		}

		roundBox.update(round);
		levelBox.update(level);
		scoreBox.update(score);
	}

	protected void onAnswerHit(AnswerSprite answer, Rectangle shotBounds) {
		Rectangle bounds = answer.getBounds();
		int explosionY = bounds.y + bounds.height;
		answerExplosionLoc = new Point(shotBounds.x + shotBounds.width/2, explosionY);
		ANSWER_HIT_OVERLAY_ANIM.start();

		Sound.ANSWER_EXPLOSION.play();
	}

	protected boolean isReady() { return ready; }

	protected int getScore() { return score; }
	protected void setScore(int score) { this.score = score; }
	protected int getLevel() { return level; }
	protected void setLevel(int level) { this.level = level; }
	protected int getRound() { return round; }
	protected void setRound(int round) { this.round = round; }
	public Question getQuestion() { return currentQuestion; }

	protected void setQuestion(Question currentQuestion) { this.currentQuestion = currentQuestion; }
	protected void setShowInterface(boolean showInterface) { this.showInterface = showInterface; }
	protected void setAnswerSpawnRate(int answerSpawnRate) { this.answerSpawnRate = answerSpawnRate; }

	protected void setCenterBoxText(String centerBoxText) { this.centerBoxText = centerBoxText; }

	protected abstract void onShipHit(AnswerSprite answer);
}
