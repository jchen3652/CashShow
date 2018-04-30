package vision;

import java.io.IOException;

import main.Main;

public class AnswerThread implements Runnable {
	private ImageProcessor processor;
	String[] answerList;
	public boolean isFinished = false;
	private long startTime; 

	public AnswerThread(ImageProcessor processor) {
		this.processor = processor;
	}

	@Override
	public void run() {
		startTime = System.currentTimeMillis();;
		try {
			answerList = processor.getAnswerList();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Main.console.println("Answer time: " + String.valueOf(System.currentTimeMillis() - startTime));
		isFinished = true;
	}

	public String[] getAnswerList() {
//		while (isFinished == false) {
//			// Do nothing
//		}
		
		return answerList;
	}
}
