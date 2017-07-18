package aimtolearn;

import java.util.*;

/**
 * This class represents a single question, with its list of answers and correct answer
 */
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

		if (allAnswers.length < 2) throw new IllegalArgumentException("Must have at least 2 answers");

		this.allAnswers = new ArrayList<>(Arrays.asList(allAnswers));
		this.answerCount = allAnswers.length;
		resetAnswers();

		// correct answer is the first one from the list in the data file
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

	public void resetAnswers() {
		this.lastAnswerIndex = -1; // this is reset is randomAnswer()

		// shuffle until the correct answer is NOT first
		do {
			Collections.shuffle(allAnswers);
		} while (allAnswers.get(0).equals(correctAnswer));
	}

	public String randomAnswer() {
		this.lastAnswerIndex++;

		// if the index is beyond the length
		if (lastAnswerIndex >= answerCount) {
			// shuffle the list of answers
			Collections.shuffle(allAnswers);
			// and reset counter
			this.lastAnswerIndex = 0;
		}

		return allAnswers.get(lastAnswerIndex);
	}

	// === getters ===

	public String getQuestionPrompt() { return questionPrompt; }
	public Subject getSubject() { return subject; }
	public Difficulty getDifficulty() { return difficulty; }

	/**
	 * The Subject enum, containing each of the 3 subjects
	 */
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

	/**
	 * The Difficulty enum, containing each of the 3 difficulties
	 */
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
