package vision;

import java.io.IOException;

public class QuestionThread implements Runnable {
	private ImageProcessor processor;
	String questionText;
	public boolean isFinished = false;

	public QuestionThread(ImageProcessor processor) {
		this.processor = processor;
	}

	@Override
	public void run() {

		try {
			questionText = processor.getQuestionText();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		isFinished = true;

	}

	public String getQuestionText() {
		while (isFinished == false) {
			
		}
		return questionText;
	}
}
