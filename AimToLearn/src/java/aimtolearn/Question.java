package aimtolearn;

public class Question {

	private final String questionPrompt, correctAnswer;
	private final String[] allAnswers;
	private final int answerCount;
	private final Subject subject;
	private final Difficulty difficulty;

	private static final int LENGTH_LIMIT = 50;

	public Question(String questionPrompt, Subject subject, Difficulty difficulty, String[] allAnswers) {
		this.questionPrompt = questionPrompt;
		this.subject = subject;
		this.difficulty = difficulty;

		this.allAnswers = allAnswers;
		this.answerCount = allAnswers.length;

		this.correctAnswer = allAnswers[0];

		for (String line : questionPrompt.split("\n")) {
			if (line.length() > LENGTH_LIMIT)
				System.out.printf("Warning: long question (%s/%s, %d chars):\n\t...\"%s\"...\n",
					subject, difficulty,
					line.length(), line);
		}
	}

	public boolean isCorrect(String testAnswer) {
		return correctAnswer.equals(testAnswer);
	}

	public String randomAnswer() {
		return allAnswers[Constants.RAND.nextInt(answerCount)];
	}

	public String getQuestionPrompt() {
		return questionPrompt;
	}

	public Subject getSubject() {
		return subject;
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public enum Subject {
		MATH, SCIENCE, HISTORY
	}

	public enum Difficulty {
		EASY, NORMAL, HARD
	}
}
