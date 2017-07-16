package aimtolearn.screens;

import aimtolearn.*;
import aimtolearn.sprites.AnimatedSprite;
import aimtolearn.sprites.AnswerSprite;
import aimtolearn.sprites.NumberBox;

import javax.swing.SwingConstants;
import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static aimtolearn.Constants.*;

public class GameplayScreen extends ShipScreen {

	private QuestionSet questionSet;
	private Question currentQuestion;

	private int score, level, round;
	private int totalScore;
	private boolean ready;

	private long lastAnswerSpawnTime = 0;

	private final List<AnswerSprite> answers = new CopyOnWriteArrayList<>();

	private final NumberBox levelBox, roundBox, scoreBox;
	private final NumberBox[] numberBoxes;
	private final Rectangle questionBox;
	private Point answerExplosionLoc;

	private static final int TOP = 150;
	private static final int TOP_MARGIN = 25;
	private static final int BOX_WIDTH = 100;
	private static final int ANSWER_SPEED = 2;
	private static final int ANSWER_SPAWN_RATE = 1000;
	private static final int PASSING_SCORE = 7;

	private static final AnimatedSprite ANSWER_HIT_OVERLAY_ANIM = new AnimatedSprite("answer_hit_explosion", 8, 400);

	public GameplayScreen(Game game) {
		super(game);

		this.score = 0;
		this.totalScore = 0;
		this.level = 1;
		this.round = 1;
		this.ready = false;

		this.questionSet = new QuestionSet();

		this.levelBox = new NumberBox("Level", TOP_MARGIN, TOP_MARGIN, BOX_WIDTH, TOP);
		this.roundBox = new NumberBox("Round", (int) (levelBox.getBounds().getMaxX()), TOP_MARGIN, BOX_WIDTH, TOP);
		this.scoreBox = new NumberBox("Score", MAIN_WIDTH - TOP_MARGIN - 2*BOX_WIDTH, TOP_MARGIN, 2*BOX_WIDTH, TOP);
		this.numberBoxes = new NumberBox[]{levelBox, roundBox, scoreBox};

		this.questionBox = new Rectangle(
			(int) (roundBox.getBounds().getMaxX() + TOP_MARGIN),
			TOP_MARGIN,
			MAIN_WIDTH - 4 * TOP_MARGIN - 4 * BOX_WIDTH,
			TOP
		);

	}

	public void start(Question.Subject subject, Question.Difficulty difficulty) {
		answers.clear();
		shots.clear();
		questionSet.resetQuestions();
		this.currentQuestion = questionSet.getQuestion(subject, difficulty);
		this.ready = true;
		this.score = 0;
		this.round = 1;
		this.level = difficulty.ordinal() + 1;
		resetKeys();
		setActive(true);
	}

	public void resetScore() {
		this.totalScore = 0;
	}

	@Override
	protected void updateScreen(Graphics graphics) {

		if (!ready) return;

		Graphics2D g = ((Graphics2D) graphics);

		super.updateScreen(g);

		if (System.currentTimeMillis() - lastAnswerSpawnTime >= ANSWER_SPAWN_RATE) {
			this.lastAnswerSpawnTime = System.currentTimeMillis();
			String randomAnswer = currentQuestion.randomAnswer();
			AnswerSprite sprite = new AnswerSprite(randomAnswer, g);
			this.answers.add(sprite);
		}

		for (AnswerSprite answer : answers) answer.draw(g);

		if (ANSWER_HIT_OVERLAY_ANIM.isRunning())
			ANSWER_HIT_OVERLAY_ANIM.draw(g, answerExplosionLoc.x, answerExplosionLoc.y);

		// draw the top interface

		for (NumberBox box : numberBoxes) box.draw(g);

		g.setFont(g.getFont().deriveFont(MAIN_FONT));
		Utils.text(currentQuestion.getQuestionPrompt(), questionBox, Color.BLACK, g, SwingConstants.CENTER);

	}

	@Override
	public void tick() {

		super.tick();

		ANSWER_HIT_OVERLAY_ANIM.tick();

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
					if (!ship.isInvincible()) {
						ship.impacted();
						decrementScore();
					}
					else {
						Sound.SHIELD_HIT.play();
					}
					remove = true;
				}

			}

			if (remove) answers.remove(answer);
			else answer.moveDown(ANSWER_SPEED);
		}

		roundBox.update(round);
		levelBox.update(level);
		scoreBox.update(score);

		repaint();
	}

	private void onAnswerHit(AnswerSprite answer, Rectangle shotBounds) {

		Rectangle bounds = answer.getBounds();
		int explosionY = bounds.y + bounds.height;
		answerExplosionLoc = new Point(shotBounds.x + shotBounds.width/2, explosionY);
		ANSWER_HIT_OVERLAY_ANIM.start();

		Sound.ANSWER_EXPLOSION.play();

		if (currentQuestion.isCorrect(answer.getText())) {

			answers.clear();
			shots.clear();
			this.score++;
			this.totalScore++;

			if (questionSet.outOfQuestions()) {

				boolean passed = score >= PASSING_SCORE;

				/*
				String msg = "You have scored %d out of %d possible questions. ";
				if (passed)	msg += "You have passed this round.";
				else msg += "You have failed this round and must try again for a score of " + PASSING_SCORE + ".";
				JOptionPane.showMessageDialog(game, String.format(msg, score, MAX_SCORE));
				*/
				if (passed) {
					roundComplete();
				}
				else {
				//	this.start(currentQuestion.getSubject(), currentQuestion.getDifficulty());
					game.setDisplayPanel(game.GAME_OVER_SCREEN);
				}

			}
			else {
				this.currentQuestion = questionSet.getQuestion();
				this.round++;
			}
		}
		else {
			decrementScore();
		}
	}

	private void roundComplete() {
		game.setDisplayPanel(game.CONTINUE_SCREEN);
		game.CONTINUE_SCREEN.init();
	}

	private void decrementScore() {
		if (score > 0) this.score--;
		if (totalScore > 0) this.totalScore--;
	}

	public Question getQuestion() {
		return currentQuestion;
	}

	public int getTotalScore() {
		return totalScore;
	}
}
