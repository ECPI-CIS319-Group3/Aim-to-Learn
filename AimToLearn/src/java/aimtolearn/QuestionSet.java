package aimtolearn;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class QuestionSet {

	private static final Gson JSON = new Gson();
	private static final String FILE_NAME = "QnA.json";
	private final EnumMap<Question.Subject, EnumMap<Question.Difficulty, List<Question>>> masterData;

	private Question.Subject currentSubject;
	private Question.Difficulty currentDiff;
	private int currentQuestionNum;

	public QuestionSet() {
		Reader input = new InputStreamReader(Constants.class.getResourceAsStream(FILE_NAME));
		Type dataType = new TypeToken<Map<String, Map<String, LinkedHashMap<String, String[]>>>>() {}.getType();

		// Map structure: subject -> difficulty -> question -> answers
		Map<String, Map<String, LinkedHashMap<String, String[]>>> data = JSON.fromJson(input, dataType);

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

	public void randomize() {
		for (Map<Question.Difficulty, List<Question>> subjects : masterData.values()) {
			for (List<Question> questions : subjects.values())
				Collections.shuffle(questions);
		}
	}

	public Question getQuestion() {
		if (this.currentSubject == null || this.currentDiff == null)
			throw new IllegalStateException("Cannot get next question - no subject or difficulty set.");

		return getQuestion(currentSubject, currentDiff);
	}

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

	public int getQuestionCount(Question.Subject subject, Question.Difficulty diff) {
		return masterData.get(subject).get(diff).size();
	}
}
