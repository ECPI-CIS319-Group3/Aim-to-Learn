package aimtolearn;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class handles loading the question data file and parses it to a complex map.
 * It also tracks the current question number, subject and difficulty, allowing the game to easily get each question.
 */
public class QuestionSet {

	// the parsed data file
	private final EnumMap<Question.Subject, EnumMap<Question.Difficulty, List<Question>>> masterData;

	// current status info
	private Question.Subject currentSubject;
	private Question.Difficulty currentDiff;
	private int currentQuestionNum;

	public QuestionSet() {
		Reader input = new InputStreamReader(Constants.class.getResourceAsStream("QnA.json"));
		Type dataType = new TypeToken<Map<String, Map<String, LinkedHashMap<String, String[]>>>>() {}.getType();
		Gson gson = new Gson();

		// Map structure: subject -> difficulty -> question -> answers
		Map<String, Map<String, LinkedHashMap<String, String[]>>> data = gson.fromJson(input, dataType);

		this.masterData = new EnumMap<>(Question.Subject.class);

		data.forEach((subj, subjectData) -> {

			Question.Subject subject = Utils.getEnum(subj, Question.Subject.class);

			EnumMap<Question.Difficulty, List<Question>> temp = new EnumMap<>(Question.Difficulty.class);

			subjectData.forEach((diff, diffData) -> {

				Question.Difficulty difficulty = Utils.getEnum(diff, Question.Difficulty.class);

				List<Question> questions = diffData.entrySet().stream()
					.map(entry -> new Question(entry.getKey(), subject, difficulty, entry.getValue()))
					.collect(Collectors.toList());

				temp.put(difficulty, questions);

			});

			this.masterData.put(subject, temp);
		});

		randomize();

		this.currentSubject = null;
		this.currentDiff = null;
		this.currentQuestionNum = -1;
	}

	/**
	 * Randomize each list of questions to they are chosen in a random order each time
	 */
	private void randomize() {
		for (Map<Question.Difficulty, List<Question>> subjects : masterData.values()) {
			for (List<Question> questions : subjects.values())
				Collections.shuffle(questions);
		}
	}

	/**
	 * Reset current question data to default
	 */
	public void resetQuestions() {
		this.currentQuestionNum = 0;
		this.currentSubject = null;
		this.currentDiff = null;

		for (Map<Question.Difficulty, List<Question>> subjects : masterData.values()) {
			for (List<Question> questions : subjects.values()) {
				for (Question q : questions)
					q.resetAnswers();
			}
		}
	}

	/**
	 * Get another question from the current subject and difficulty
	 */
	public Question getQuestion() {
		if (this.currentSubject == null || this.currentDiff == null)
			throw new IllegalStateException("Cannot get next question - no subject or difficulty set.");

		return getQuestion(currentSubject, currentDiff);
	}

	/**
	 * Get a question from the given subject and difficulty. Gets next question if current difficulty and subject
	 * are given, otherwise, starts a new counter
	 */
	public Question getQuestion(Question.Subject subject, Question.Difficulty diff) {

		if (subject == currentSubject && diff == currentDiff)
			this.currentQuestionNum++;
		else
			this.currentQuestionNum = 0;

		this.currentSubject = subject;
		this.currentDiff = diff;

		if (currentQuestionNum >= getQuestionCount(subject, diff))
			throw new IllegalStateException("No more questions for subject " + subject + " and difficulty " + diff + ".");

		return masterData
			.get(subject)
			.get(diff)
			.get(currentQuestionNum);
	}

	/**
	 * Returns whether or not this subject and difficulty is out of questions
	 */
	public boolean outOfQuestions() {
		return currentQuestionNum >= getQuestionCount(currentSubject, currentDiff) - 1;
	}

	/**
	 * Get the question count for a given subject and difficulty
	 */
	private int getQuestionCount(Question.Subject subject, Question.Difficulty diff) {
		return masterData.get(subject).get(diff).size();
	}
}
