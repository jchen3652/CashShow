package threads;

import java.io.IOException;

import main.Config;
import vision.ImageProcessor;

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
		startTime = System.currentTimeMillis();
		;
		try {
			answerList = processor.getAnswerList();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Config.printStream.println("Answer time: " + String.valueOf(System.currentTimeMillis() - startTime));
		isFinished = true;
	}

	public String[] getAnswerList() {
		return answerList;
	}
}
