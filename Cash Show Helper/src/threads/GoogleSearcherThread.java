package threads;

import java.io.IOException;

import algorithms.JSONTools;

public class GoogleSearcherThread implements Runnable {
	private JSONTools tools = new JSONTools();
	private String question;
	boolean isFinished;

	public GoogleSearcherThread(String question) {
		this.question = question;
	}
	
	public GoogleSearcherThread() {
		
	}

	@Override
	public void run() {
		try {
			tools.loadQuery(question);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		isFinished = true;
	}
	
	public void setQuery(String query) {
		question = query;
	}
	
	public JSONTools getResult() {
		while(isFinished != true) {
		}
		return tools;
	}
}