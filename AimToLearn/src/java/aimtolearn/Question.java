package aimtolearn;

public class Question {

	private final String questionPrompt, correctAnswer;
	private final String[] allAnswers;
	private final int answerCount;

	public Question(String questionPrompt, String[] allAnswers) {
		this.questionPrompt = questionPrompt;

		this.allAnswers = allAnswers;
		this.answerCount = allAnswers.length;

		this.correctAnswer = allAnswers[0];

		if (this.questionPrompt.length() > 50)
			System.out.printf("Warning: long answer (%d chars):\n\t\"%s\"\n", questionPrompt.length(), questionPrompt);
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

	public enum Subject {
		MATH, SCIENCE, HISTORY
	}

	public enum Difficulty {
		EASY, NORMAL, HARD
	}
}
