package algorithms;

import java.io.IOException;

import org.json.JSONException;

public class PrimaryAlgorithmThread implements Runnable{
	private String question;
	private String googleResult;
	private String answerCandidate;
	
	private int score;
	
	private boolean isFinished;
	
	
	public PrimaryAlgorithmThread(String question, String googleResult, String answerCandidate) {
		this.question = question;
		this.googleResult = googleResult;
		this.answerCandidate = answerCandidate;
	}
	@Override
	public void run() {
		try {
			score = Algorithms.primaryAlgorithm(question, googleResult, answerCandidate);
		} catch (JSONException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		isFinished = true;
	}
	
	public int getScore() {
		while(!isFinished) {
			
		}
		
		return score;
	}

}
