package main;

import java.io.IOException;

import algorithms.Algorithms;
import algorithms.PrimaryAlgorithmThread;

public class Trivia {
	private String question;
	private String googleResult;
	private String[] answerCandidates;
	private int[] allScores = new int[3];
	
	
	public Trivia() {
		
	}
	
	public Trivia(String question, String[] answerCandidates) {
		this.question = question;
		this.answerCandidates = answerCandidates;
		
	}
	
	public void calculate() throws IOException {
		PrimaryAlgorithmThread[] algorithms = new PrimaryAlgorithmThread[3];
				
		for(int i = 0; i < 3; i ++) {
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
			Main.console.println("No results, alternate started");
			for (int i = 0; i < 3; i++) {
				allScores[i] = Algorithms.googleResultsAlgorithm(question, answerCandidates[i]);
			}

			boolean lessThanThreshold = false;

			for (int i : allScores) {
				if (i < 1000) {
					lessThanThreshold = true;
				}
			}
			if (!lessThanThreshold) {
				for (int i : allScores) {
					i = (int) Math.round(((double) i) / Config.googleResultsScaleDown);
				}
			}
		}
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
		int largestScore = 0;
		for (int i = 0; i < 3; i++) {
			Main.console.println((new StringBuilder(answerCandidates[i])).append(": ").append(allScores[i])
					.toString());
			
			
			if (allScores[i] > largestScore) {
				largestScore = allScores[i];
				largestIndex = i;
			}

			allScores[i] = 0;
		}
		return largestIndex;
	}




}
