package main;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import algorithms.Algorithms;
import algorithms.GoogleSearcher;
import vision.ImageProcessor;
import vision.ScreenListenerAlgorithms;

public class Main {
	String[] allAnswers;
	private static int[] allScores = new int[3];
	static String questionText;
	static String fullSearchableText;

	static File rawOutputfile = new File(Config.mainDirectory + Config.screenshotIdentifier);

	private static ScreenListenerAlgorithms timerListener;
	private static ScreenListenerAlgorithms whiteListener;

	static Rectangle screenRect = new Rectangle(Config.phoneScreenArea[0], Config.phoneScreenArea[1],
			Config.phoneScreenArea[2], Config.phoneScreenArea[3]);

	public static final boolean isLiveShow = true;

	public static void main(String[] args) throws AWTException, IOException, InterruptedException {
		//new ConsoleOutput().setVisible(true);
		Thread.sleep(2000);
		System.out.println((new StringBuilder("Is live show: ")).append(isLiveShow));
		Robot robot = new Robot();
		robot.setAutoDelay(0);
		robot.setAutoWaitForIdle(false);

		whiteListener = new ScreenListenerAlgorithms(Config.whitePixelXLocation, Config.whitePixelYLocation, robot);
		timerListener = new ScreenListenerAlgorithms(Config.timerPixelXLocation, Config.timerPixelYLocation, robot);

		while (true) {
			timerListener.refreshPixelListener();

			while (!(timerListener.isGray() || timerListener.isGreen())) {
				timerListener.refreshPixelListener();
			}
			System.out.println("Detected Question");
			long start = System.currentTimeMillis();

			Thread.sleep(50);
			BufferedImage img = robot.createScreenCapture(screenRect);
			if (Config.isDebug) {
				ImageIO.write(img, "png", rawOutputfile);
			}
			ImageProcessor processor = new ImageProcessor(img);
			questionText = processor.getQuestionText();
			String[] allAnswers = processor.getAnswerList();
			System.out.println(processor.humanQuestionText);
			fullSearchableText = GoogleSearcher.loadFullSearchableText(questionText);

			for (String o : processor.humanAnswerText) {
				System.out.println("***" + o);

			}

			for (int i = 0; i < 3; i++) {
				try {
					allScores[i] = 0;
					allScores[i] += Algorithms.primaryAlgorithm(questionText, fullSearchableText, allAnswers[i]);

					String[] splitQuestionText = questionText.split(" ");
					for (String o : splitQuestionText) {
						if (questionText.toLowerCase().contains(o.toLowerCase())) {
							allScores[i] -= Algorithms.occuranceAlgorithmScore(fullSearchableText, o);
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if ((allScores[0] < 2 && allScores[1] < 2 && (allScores[2] < 2))) {
				System.out.println("No results, alternate started");
				for (int i = 0; i < 3; i++) {
					allScores[i] = Algorithms.googleResultsAlgorithm(questionText, allAnswers[i]);

				}
			}

			for (int i = 0; i < 3; i++) {
				System.out.println(processor.humanAnswerText[i] + ": " + allScores[i]);
			}
			System.out.println((System.currentTimeMillis() - start) / 1000.0);

			whiteListener.refreshPixelListener();
			System.out.println(whiteListener.getR());
			System.out.println(whiteListener.getG());
			System.out.println(whiteListener.getB());

			while ((/* isGray || isGreen || */ whiteListener.isWhite())) {
				whiteListener.refreshPixelListener();

			}

			System.out.println("Screen changed");

			timerListener.refreshPixelListener();

			while (!timerListener.isGray() && !timerListener.isGreen()) {

				//do nothing
				timerListener.refreshPixelListener();

			}
			System.out.println("Screen changed back to questions");

			if (isLiveShow) {
				System.out.print(" jk it's the answer reveal, waiting until it's not");
				whiteListener.refreshPixelListener();
				while (whiteListener.isWhite()) {

					whiteListener.refreshPixelListener();

				}

				System.out.println("Now answer reveal is over");

				timerListener.refreshPixelListener();
				Thread.sleep(50);

			}

		}
	}
}