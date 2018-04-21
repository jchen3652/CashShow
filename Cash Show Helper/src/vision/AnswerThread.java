package vision;

import java.io.IOException;

public class AnswerThread implements Runnable {
	private ImageProcessor processor;
	String[] answerList;
	public boolean isFinished = false;

	public AnswerThread(ImageProcessor processor) {
		this.processor = processor;
	}

	@Override
	public void run() {

		try {
			answerList = processor.getAnswerList();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		isFinished = true;

	}

	public String[] getAnswerList() {
		while (isFinished == false) {
			
		}
		return answerList;
	}
}
