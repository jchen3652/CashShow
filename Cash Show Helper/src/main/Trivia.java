package main;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import algorithms.Algorithms;
import algorithms.GoogleSearcher;
import algorithms.PrimaryAlgorithmThread;

public class Trivia {
	private String question;
	private String googleResult;
	private String[] answerCandidates;

	private boolean isNegated;
	private int[] allScores = new int[3];

	public Trivia() {

	}

	public Trivia(String question, String[] answerCandidates) {
		this.question = question;
		this.answerCandidates = answerCandidates;

	}

	
	public static void main(String[] args) throws IOException {
		String question = "Which of the following countries is not a monarchy?";
		question = StringUtils.lowerCase(question);
		question = Algorithms.filterQuestionText(question);
		question = Algorithms.removeNegation(question);
		System.out.println(question);
		String[] allAnswers = {"Iraq", "Oman", "Sweeden"};
		Trivia trivia = new Trivia();
		trivia.setQuestionText(question);
		trivia.setAnswerList(allAnswers);
		
		trivia.setGoogleResult(GoogleSearcher.getGoogleResultsString(question));
		trivia.calculate();
		
//		for(int i = 0; i < 3; i ++) {
//			trivia.allScores[i] = Algorithms.googleResultsAlgorithm(question, allAnswers[i]);
//		}
		
		System.out.println(trivia.getBestScoreIndex());
	}
	
	
	public void calculate() throws IOException {
		PrimaryAlgorithmThread[] algorithms = new PrimaryAlgorithmThread[3];
		for(String o:Config.negatedGiveaways) {
			if (question.toLowerCase().contains(o)) {
				isNegated = true;
			}
		}
		
		
		
		

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

		// If the first algorithm failed, try the other one
		if ((allScores[0] < 2 && allScores[1] < 2 && (allScores[2] < 2))) {
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

	public void setAnswerList(String[] answerArray) {
		this.answerCandidates = answerArray;
	}

	public void setAnswer(String answer, int index) {
		answerCandidates[index] = answer;
	}

	public void setQuestionText(String question) {
		this.question = question;
	}

	public String getQuestionText() {
		return question;
	}

	public void setGoogleResult(String result) {
		this.googleResult = result;
	}

	public String getGoogleResult() {
		return googleResult;
	}

	public int getScore(int index) {
		return allScores[index];
	}

	public int getBestScoreIndex() {

		int largestIndex = 0;
		int largestScore = allScores[0];

		int smallestIndex = 0;
		int smallestScore = allScores[0];
		for (int i = 0; i < 3; i++) {
			Config.printStream.println((new StringBuilder(answerCandidates[i])).append(": ").append(allScores[i]).toString());

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
		//		if (isNegated) {
		//			return smallestIndex;
		//		} else {
		return largestIndex;
		//		}
	}

}
