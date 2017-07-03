package aimtolearn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Question {

	public final String questionPrompt, correctAnswer;
	public final String[] otherAnswers;
	private final List<String> allAnswers;
	private final int answerCount;

	public Question(String questionPrompt, String correctAnswer, String[] otherAnswers) {
		this.questionPrompt = questionPrompt;
		this.correctAnswer = correctAnswer;
		this.otherAnswers = otherAnswers;

		this.allAnswers = new ArrayList<>(Arrays.asList(otherAnswers));
		allAnswers.add(correctAnswer);

		this.answerCount = otherAnswers.length + 1;
	}

	public boolean isCorrect(String testAnswer) {
		return correctAnswer.equals(testAnswer);
	}

	public String randomAnswer() {
		return allAnswers.get(Constants.RAND.nextInt(answerCount));
	}
}
