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

	private int currentQuestionNum;

	public QuestionSet() {
		Reader input = new InputStreamReader(Constants.class.getResourceAsStream(FILE_NAME));
		Type dataType = new TypeToken<Map<String, Map<String, LinkedHashMap<String, String[]>>>>() {}.getType();

		// Map structure: subject -> difficulty -> question -> answers
		Map<String, Map<String, LinkedHashMap<String, String[]>>> data = JSON.fromJson(input, dataType);

		this.masterData = new EnumMap<>(Question.Subject.class);

		data.forEach((subject, subjectData) -> {

			EnumMap<Question.Difficulty, List<Question>> temp = new EnumMap<>(Question.Difficulty.class);

			subjectData.forEach((diff, diffData) -> {

				temp.put(Utils.getEnum(diff, Question.Difficulty.class),
					diffData.entrySet().stream()
					.map(entry -> new Question(entry.getKey(), entry.getValue()))
					.collect(Collectors.toList())
				);

			});

			this.masterData.put(Utils.getEnum(subject, Question.Subject.class), temp);
		});

		this.currentQuestionNum = -1;
	}

	public Question getQuestion(Question.Subject subject, Question.Difficulty diff) {

		this.currentQuestionNum++;

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
