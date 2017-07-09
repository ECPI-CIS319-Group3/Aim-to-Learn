package aimtolearn;

import java.util.Arrays;

public class Question {

	public final String questionPrompt, correctAnswer;
	public final String[] otherAnswers;
	private final String[] allAnswers;
	private final int answerCount;

	public Question(String questionPrompt, String[] allAnswers) {
		this.questionPrompt = questionPrompt;

		this.allAnswers = allAnswers;
		this.answerCount = allAnswers.length;

		this.correctAnswer = allAnswers[0];
		this.otherAnswers = Arrays.copyOfRange(allAnswers, 1, answerCount);

		if (this.questionPrompt.length() > 50)
			System.out.printf("Warning: long answer (%d chars):\n\t\"%s\"\n", questionPrompt.length(), questionPrompt);
	}

	public boolean isCorrect(String testAnswer) {
		return correctAnswer.equals(testAnswer);
	}

	public String randomAnswer() {
		return allAnswers[Constants.RAND.nextInt(answerCount)];
	}

	public enum Subject {
		MATH, SCIENCE, HISTORY;
	}

	public enum Difficulty {
		EASY, NORMAL, HARD;
	}
}
