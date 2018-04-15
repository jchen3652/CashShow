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
import consoleOutput.ConsoleOutput;
import vision.ImageProcessor;
import vision.PixelListener;
import vision.SmartScreen;

public class Main {
	private static int[] allScores = new int[3];
	static String questionText;
	static String fullSearchableText;

	private static PixelListener timerListener;
	private static PixelListener whiteListener;

	public static boolean watchingScreen = true;
	static int[] phoneRectangle;
	static Robot robot;
	public static  ConsoleOutput output = new ConsoleOutput();
			

	static long startTime;

	public static void main(String[] args) throws AWTException, IOException, InterruptedException {
		output.setVisible(true);
		
		phoneRectangle = SmartScreen.runSmartScreenCheck();
		System.out.println(SmartScreen.getTaskbarSize());
		System.out.println(SmartScreen.getTitleHeight());
		robot = new Robot();
		robot.setAutoDelay(0);
		robot.setAutoWaitForIdle(false);

		whiteListener = new PixelListener(Config.whitePixelXLocation, Config.whitePixelYLocation, robot);
		timerListener = new PixelListener(Config.timerPixelXLocation, Config.timerPixelYLocation, robot);

		System.out.println((new StringBuilder("Is live show: ")).append(Config.isLiveShow));
		//Thread.sleep(2000);
		int[] phoneRectangle = SmartScreen.runSmartScreenCheck();

		while (true) {
			if (isListening()) {
				timerListener.refreshPixelListener();
				whiteListener.refreshPixelListener();

				while (!((timerListener.isGray() || timerListener.isGreen()) && whiteListener.isWhite())) {
					timerListener.refreshPixelListener();
					whiteListener.refreshPixelListener();
				}

				System.out.println("Detected Question");

				if (Config.isDebug) {
					timerListener.printRGB();
					startTime = System.currentTimeMillis();
				}

				Thread.sleep(500);

				BufferedImage rawCapture = robot.createScreenCapture(
						new Rectangle(phoneRectangle[0], phoneRectangle[1], phoneRectangle[2], phoneRectangle[3]));
				if (Config.isDebug) {
					ImageIO.write(rawCapture, "png", new File(
							(new StringBuilder(Config.mainDirectory)).append(Config.screenshotIdentifier).toString()));
				}

				ImageProcessor processor = new ImageProcessor(rawCapture);
				questionText = processor.getQuestionText();
				String[] allAnswers = processor.getAnswerList();
				System.out.println(processor.humanQuestionText);
				fullSearchableText = GoogleSearcher.loadFullSearchableText(questionText);

				for (String o : processor.humanAnswerText) {
					System.out.println((new StringBuilder("***")).append(o));

				}

				for (int i = 0; i < 3; i++) {
					try {
						allScores[i] = 0;
						allScores[i] += Algorithms.primaryAlgorithm(questionText, fullSearchableText, allAnswers[i]);

						String[] splitAnswerText = allAnswers[i].split(" ");
						for (String o : splitAnswerText) {
							if (questionText.toLowerCase().contains(o.toLowerCase())) {
								System.out.println("contains shit");
								allScores[i] -= Algorithms.occuranceAlgorithmScore(fullSearchableText.toLowerCase(),
										o.toLowerCase());
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
					System.out.println(
							(new StringBuilder(processor.humanAnswerText[i])).append(": ").append(allScores[i]));
				}
				if (Config.isDebug) {
					System.out.println((System.currentTimeMillis() - startTime) / 1000.0);
				}
				whiteListener.refreshPixelListener();
				whiteListener.printRGB();

				while ((/* isGray || isGreen || */ whiteListener.isWhite())) {
					whiteListener.refreshPixelListener();

				}

				System.out.println("Screen changed");

				timerListener.refreshPixelListener();
				while (!timerListener.isGray() && !timerListener.isGreen()) {
					timerListener.refreshPixelListener();
				}

				System.out.println("Screen changed back to questions");

				if (Config.isLiveShow) {
					System.out.println("JK it's actually the answer reveal, waiting until it's over");

					whiteListener.refreshPixelListener();
					while (whiteListener.isWhite()) {
						whiteListener.refreshPixelListener();
					}

					System.out.println("Answer Reveal is over");

					timerListener.refreshPixelListener();
					Thread.sleep(50);
				}
			}
		}
	}

	public static void doEverything() throws IOException, InterruptedException, AWTException {

		if (true) {
			timerListener.refreshPixelListener();
			whiteListener.refreshPixelListener();

			while (!((timerListener.isGray() || timerListener.isGreen()) && whiteListener.isWhite())) {
				timerListener.refreshPixelListener();
				whiteListener.refreshPixelListener();
			}

			System.out.println("Detected Question");

			if (Config.isDebug) {
				timerListener.printRGB();
				startTime = System.currentTimeMillis();
			}

			Thread.sleep(500);

			BufferedImage rawCapture = robot.createScreenCapture(
					new Rectangle(phoneRectangle[0], phoneRectangle[1], phoneRectangle[2], phoneRectangle[3]));
			if (Config.isDebug) {
				ImageIO.write(rawCapture, "png", new File(
						(new StringBuilder(Config.mainDirectory)).append(Config.screenshotIdentifier).toString()));
			}

			ImageProcessor processor = new ImageProcessor(rawCapture);
			questionText = processor.getQuestionText();
			String[] allAnswers = processor.getAnswerList();
			System.out.println(processor.humanQuestionText);
			fullSearchableText = GoogleSearcher.loadFullSearchableText(questionText);

			for (String o : processor.humanAnswerText) {
				System.out.println((new StringBuilder("***")).append(o));

			}

			for (int i = 0; i < 3; i++) {
				try {
					allScores[i] = 0;
					allScores[i] += Algorithms.primaryAlgorithm(questionText, fullSearchableText, allAnswers[i]);

					String[] splitAnswerText = allAnswers[i].split(" ");
					for (String o : splitAnswerText) {
						if (questionText.toLowerCase().contains(o.toLowerCase())) {
							System.out.println("contains shit");
							allScores[i] -= Algorithms.occuranceAlgorithmScore(fullSearchableText.toLowerCase(),
									o.toLowerCase());
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
				System.out.println((new StringBuilder(processor.humanAnswerText[i])).append(": ").append(allScores[i]));
			}
			if (Config.isDebug) {
				System.out.println((System.currentTimeMillis() - startTime) / 1000.0);
			}
			whiteListener.refreshPixelListener();
			whiteListener.printRGB();

			while ((/* isGray || isGreen || */ whiteListener.isWhite())) {
				whiteListener.refreshPixelListener();

			}

			System.out.println("Screen changed");

			timerListener.refreshPixelListener();
			while (!timerListener.isGray() && !timerListener.isGreen()) {
				timerListener.refreshPixelListener();
			}

			System.out.println("Screen changed back to questions");

			if (Config.isLiveShow) {
				System.out.println("JK it's actually the answer reveal, waiting until it's over");

				whiteListener.refreshPixelListener();
				while (whiteListener.isWhite()) {
					whiteListener.refreshPixelListener();
				}

				System.out.println("Answer Reveal is over");

				timerListener.refreshPixelListener();
				Thread.sleep(50);
			}
		}
	}

	public static boolean isListening() {
		return watchingScreen;
	}

}