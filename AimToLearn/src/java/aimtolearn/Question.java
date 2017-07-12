package aimtolearn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Question {

	private final String questionPrompt, correctAnswer;
	private final List<String> allAnswers;
	private final int answerCount;
	private final Subject subject;
	private final Difficulty difficulty;

	private int lastAnswerIndex;

	private static final int LENGTH_LIMIT = 50;

	public Question(String questionPrompt, Subject subject, Difficulty difficulty, String[] allAnswers) {
		this.questionPrompt = questionPrompt;
		this.subject = subject;
		this.difficulty = difficulty;

		this.allAnswers = new ArrayList<>(Arrays.asList(allAnswers));
		this.answerCount = allAnswers.length;
		this.lastAnswerIndex = answerCount; // reset in randomAnswer()

		this.correctAnswer = allAnswers[0];

		// helpful check for questions with any of their lines that are too long
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
		this.lastAnswerIndex++;

		// if the index is beyond the length
		if (lastAnswerIndex >= answerCount) {
			// shuffle until the correct answer is NOT first
			do {
				Collections.shuffle(allAnswers);
			} while (allAnswers.get(0).equals(correctAnswer));

			// and reset counter
			this.lastAnswerIndex = 0;
		}

		return allAnswers.get(lastAnswerIndex);


		// previous implementation, using array
	//	return allAnswers[Constants.RAND.nextInt(answerCount)];
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
		MATH, SCIENCE, HISTORY;

		private static String[] items;
		static {
			Subject[] vals = Subject.values();
			items = new String[vals.length];
			for (int i = 0; i < vals.length; i++) items[i] = vals[i].name();
		}

		public static String[] items() { return items; }
	}

	public enum Difficulty {
		EASY, NORMAL, HARD;

		private static String[] items;
		static {
			Subject[] vals = Subject.values();
			items = new String[vals.length];
			for (int i = 0; i < vals.length; i++) items[i] = vals[i].name();
		}

		public static String[] items() { return items; }
	}
}
