package threads;

import java.io.IOException;

import main.Config;
import vision.ImageProcessor;

public class QuestionThread implements Runnable {
	private ImageProcessor processor;
	String questionText;
	public boolean isFinished = false;
	private long startTime;

	public QuestionThread(ImageProcessor processor) {
		this.processor = processor;
	}

	@Override
	public void run() {
		startTime = System.currentTimeMillis();
		try {
			questionText = processor.getQuestionText();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Config.printStream.println("Question time: " + String.valueOf(System.currentTimeMillis() - startTime));
		isFinished = true;

	}

	public String getQuestionText() {
		//		while (isFinished == false) {
		//			
		//		}
		return questionText;
	}
}
