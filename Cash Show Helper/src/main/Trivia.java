package main;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import algorithms.Algorithms;
import algorithms.GoogleSearcher;
import algorithms.PrimaryAlgorithmThread;

public class Trivia {
	private String question;
	private String googleResult;
	private String[] answerCandidates = null;

	private boolean isNegated;
	private int[] allScores = new int[3];

	public Trivia() {

	}

	public Trivia(String question, String[] answerCandidates) {
		this.question = question;
		this.answerCandidates = answerCandidates;

	}

	public static void main(String[] args) throws IOException {
		String question = "Which of these condiments was created by a monarch's personal chef?";
		question = StringUtils.lowerCase(question);
		question = Algorithms.filterQuestionText(question);
		question = Algorithms.removeNegation(question);
		System.out.println(question);
		String[] allAnswers = {"Siracha", "Grey Poupon", "A.1."};
		Trivia trivia = new Trivia();
		trivia.setQuestionText(question);
		trivia.setAnswerArray(allAnswers);

		trivia.setGoogleResult(GoogleSearcher.getGoogleResultsString(question));
		trivia.calculate();

		//		for(int i = 0; i < 3; i ++) {
		//			trivia.allScores[i] = Algorithms.googleResultsAlgorithm(question, allAnswers[i]);
		//		}

		System.out.println(trivia.getBestScoreIndex());
	}

	public void calculate() throws IOException {
		question = StringUtils.lowerCase(question);
		for (String o : Config.negatedGiveaways) {
			if (question.contains(o)) {
				isNegated = true;
				System.out.println("Is negated");
				Config.printStream.println("Is negated");
			} else {
				isNegated = false;
			}
		}

		question = Algorithms.filterQuestionText(question);
		if (isNegated) {
			question = Algorithms.removeNegation(question);
		}
		Config.printStream.println("Filtered question: " + question);
		PrimaryAlgorithmThread[] algorithms = new PrimaryAlgorithmThread[3];

		//		if (!isNegated) {
		for (int i = 0; i < 3; i++) {
			algorithms[i] = new PrimaryAlgorithmThread(question, googleResult, answerCandidates[i]);
			algorithms[i].run();
		}

		for (int i = 0; i < 3; i++) {
			try {
				allScores[i] += algorithms[i].getScore();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		int numberOfZeros = 0;
		for (int o : allScores) {
			if (o == 0) {
				numberOfZeros++;
			}

		}
		System.out.println(numberOfZeros);

		// If the first algorithm failed, try the other one
		if ((allScores[0] < 1 && allScores[1] < 1 && (allScores[2] < 1)) || (isNegated && numberOfZeros == 2)) {
			Config.printStream.println("No results, alternate started");
			for (int i = 0; i < 3; i++) {
				allScores[i] = Algorithms.googleResultsAlgorithm(question, answerCandidates[i]);
			}

		}

		for (int i : allScores) {
			i = (int) Math.round(((double) i) / Config.googleResultsScaleDown);
		}
		//		} else {
		//			Main.console.println("Negation detected");
		//			for (int i = 0; i < 3; i++) {
		//				allScores[i] = Algorithms.googleResultsAlgorithm(question, answerCandidates[i]);
		//			}
		//		}

	}

	public String getAnswer(int index) {
		return answerCandidates[index];
	}

	public void setAnswer(String answer, int index) {
		answerCandidates[index] = answer;
	}

	public String[] getAnswerArray() {
		return answerCandidates;
	}

	public void setAnswerArray(String[] answerArray) {
		this.answerCandidates = answerArray;
	}

	public String getQuestionText() {
		return question;
	}

	public void setQuestionText(String question) {
		this.question = question;
	}

	public String getGoogleResult() {
		return googleResult;
	}

	public void setGoogleResult(String result) {
		this.googleResult = result;
	}

	public int getScore(int index) {
		return allScores[index];
	}

	public int getBestScoreIndex() {
		int largestIndex = 0;
		int largestScore = allScores[0];

		int smallestIndex = 0;
		int smallestScore = allScores[0];

		int totalScore = 0;
		for (int o : allScores) {
			totalScore += o;
		}

		for (int i = 0; i < 3; i++) {
			double percent = Math.round((double) allScores[i] / ((double) totalScore) * 100.0);
			Config.printStream.println((new StringBuilder(answerCandidates[i])).append(": ").append(allScores[i])
					.append(" (").append(percent).append("%)").toString());

			if (allScores[i] > largestScore) {
				largestScore = allScores[i];
				largestIndex = i;
			}

			if (allScores[i] < smallestScore) {
				smallestScore = allScores[i];
				smallestIndex = i;
			}

			allScores[i] = 0;
		}
		if (isNegated) {
			return smallestIndex;
		} else {
			return largestIndex;
		}
	}
}