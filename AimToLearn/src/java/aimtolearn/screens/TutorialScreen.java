package aimtolearn.screens;

import aimtolearn.Game;
import aimtolearn.Question;
import aimtolearn.Sound;
import aimtolearn.sprites.AnswerSprite;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public class TutorialScreen extends BaseGameplayScreen {

	private int step;
	private int hitCount;

	private long stepStartTime;
	private static final int STEP_PROMPT_DELAY = 3000;

	private static final String[] STEP_STRINGS = {
		"Press ← and → (or A and D) to move",
		"Press ↑ (or W) to shoot",
		"Shoot the correct answer to continue",
		"Shoot the _______ answer to continue",
		"Your score rises as you shoot correct answers\nand falls on incorrect hits",
		"Your _____ rises as you shoot correct answers\nand falls on incorrect hits",
		"When hit by an answer, you lose 1 score point\nand become momentarily invincible",
		"You must get a score of 7 or higher to\ncomplete a round. When ready, shoot \"Done\""
	};

	public TutorialScreen(Game game) {
		super(game);
	}

	@Override
	public void init() {
		this.hitCount = 0;
		this.step = -1;
		nextStep();
		setShowInterface(false);
		setShootingEnabled(false);
		setQuestion(null);
		super.init();
	}

	@Override
	protected void onKeyDown(KeyEvent e) {
		super.onKeyDown(e);
		int key = e.getKeyCode();

		if (step == 0 && (LEFT_KEYS.contains(key) || RIGHT_KEYS.contains(key))) nextStep();
	}

	@Override
	protected void updateScreen(Graphics graphics) {
		if (!isReady()) return;
		super.updateScreen(graphics);
	}

	private void nextStep() {

		this.step++;

		if (step >= STEP_STRINGS.length) {
			game.setDisplayPanel(game.MAIN_MENU);
			return;
		}

		this.stepStartTime = System.currentTimeMillis();
		setCenterBoxText(STEP_STRINGS[step]);

		switch (step) {
			case 1:
				setShootingEnabled(true);
				break;
			case 3:
				setQuestion(new Question("", null, null, new String[]{"Correct", "Incorrect"}));
				break;
			case 4:
				clear();
				setShowInterface(true);
				break;
			case 5:
				setQuestion(new Question("", null, null, new String[]{"Score", "Bread", "Intelligence"}));
				break;
			case 6:
				clear();
				setAnswerSpawnRate(200);
				setQuestion(new Question("", null, null, new String[]{"Answer", "Word"}));
				setScore(99);
				break;
			case 7:
				clear();
				setAnswerSpawnRate(DEFAULT_ANSWER_SPAWN_RATE);
				setQuestion(new Question("", null, null, new String[]{"Done", "Nope"}));
				break;
		}
	}

	private void clear() {
		answers.clear();
		shots.clear();
		setQuestion(null);
	}

	@Override
	public void tick() {
		super.tick();

		if ((step == 2 || step == 4) && System.currentTimeMillis() - stepStartTime >= STEP_PROMPT_DELAY)
			nextStep();

		repaint();
	}

	@Override
	protected void onAnswerHit(AnswerSprite answer, Rectangle shotBounds) {
		super.onAnswerHit(answer, shotBounds);

		Question question = getQuestion();
		if (question == null) return;

		if (question.isCorrect(answer.getText())) {
			setScore(getScore()+1);
			if (step == 3 || step == 5 || step == 7) nextStep();
		}
		else if (step >= 5) {
			setScore(getScore()-1);
		}
	}

	@Override
	protected void fireShot() {
		super.fireShot();

		if (step == 1) nextStep();
	}

	@Override
	protected void onShipHit(AnswerSprite answer) {
		if (step >= 6) {
			if (!ship.isInvincible()) {
				ship.impacted();
				setScore(getScore()-1);
				this.hitCount++;
				if (step == 6 && hitCount >= 3) nextStep();
			}
			else {
				Sound.SHIELD_HIT.play();
			}
		}
	}
}
