package aimtolearn.screens;

import aimtolearn.Game;
import aimtolearn.Question;
import aimtolearn.QuestionSet;
import aimtolearn.Sound;
import aimtolearn.sprites.AnswerSprite;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class GameplayScreen extends BaseGameplayScreen {

	private QuestionSet questionSet;

	private int totalScore;
	private static final int PASSING_SCORE = 7;

	public GameplayScreen(Game game) {
		super(game);

		this.totalScore = 0;
		this.questionSet = new QuestionSet();

	}

	public void start(Question.Subject subject, Question.Difficulty difficulty) {

		questionSet.resetQuestions();
		updateQuestion(questionSet.getQuestion(subject, difficulty));

		init();

		setLevel(difficulty.ordinal() + 1);
	}

	public void resetScore() {
		this.totalScore = 0;
	}

	@Override
	protected void updateScreen(Graphics graphics) {

		if (!isReady()) return;

		Graphics2D g = ((Graphics2D) graphics);


		super.updateScreen(g);
	}

	@Override
	public void tick() {
		super.tick();
		repaint();
	}

	@Override
	protected void onAnswerHit(AnswerSprite answer, Rectangle shotBounds) {

		super.onAnswerHit(answer, shotBounds);

		if (getQuestion().isCorrect(answer.getText())) {

			answers.clear();
			shots.clear();
			setScore(getScore()+1);
			this.totalScore++;

			if (questionSet.outOfQuestions()) {

				boolean passed = getScore() >= PASSING_SCORE;

				if (passed) roundComplete();
				else gameOver();

			}
			else {
				updateQuestion(questionSet.getQuestion());
				setRound(getRound()+1);
			}
		}
		else {
			decrementScore();
		}
	}

	@Override
	protected void onShipHit(AnswerSprite answer) {
		if (!ship.isInvincible()) {
			ship.impacted();
			decrementScore();
		}
		else {
			Sound.SHIELD_HIT.play();
		}
	}

	private void updateQuestion(Question question) {
		setQuestion(question);
		setCenterBoxText(question.getQuestionPrompt());
	}

	private void gameOver() {
		game.setDisplayPanel(game.GAME_OVER_SCREEN);
	}

	private void roundComplete() {
		if (getQuestion().getDifficulty() == Question.Difficulty.HARD) {
			game.setDisplayPanel(game.SUBJECT_SCREEN);
			game.SUBJECT_SCREEN.setDisabledSubject(getQuestion().getSubject());
			game.SUBJECT_SCREEN.init(true);
		}
		else {
			game.setDisplayPanel(game.CONTINUE_SCREEN);
			game.CONTINUE_SCREEN.init();
		}
	}

	private void decrementScore() {
		if (getScore() > 0) setScore(getScore()-1);
		if (totalScore > 0) this.totalScore--;
	}

	public int getTotalScore() {
		return totalScore;
	}
}
