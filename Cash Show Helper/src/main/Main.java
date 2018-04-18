package main;

import java.awt.AWTException;
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
import vision.ScreenUtils;
import vision.SmartScreen;

public class Main {
	private static String[] allAnswers;
	private static int[] allScores = {0, 0, 0};
	private static String questionText;
	private static String googleResultsString;

	private static PixelListener timerListener;
	private static PixelListener whiteListener;
	

	private static Robot robot;
	public static ConsoleOutput output = new ConsoleOutput();
	public static SmartScreen smartscreen;

	public static void main(String[] args) throws AWTException, IOException, InterruptedException {
		output.setVisible(true);
		
		
		
		smartscreen = new SmartScreen(ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight(),
				output.getConsoleHeight(), ScreenUtils.getTaskbarHeight());
		smartscreen.getScreenInformation();
		robot = new Robot();

		robot.setAutoDelay(0);
		robot.setAutoWaitForIdle(false);

		whiteListener = new PixelListener(
				smartscreen.relToAbsHorizontal(Config.whiteXLocation, smartscreen.screenshotXCoordinate),
				smartscreen.relToAbsHorizontal(Config.whiteYLocation, smartscreen.screenshotYCoordinate), robot);

		timerListener = new PixelListener(
				smartscreen.relToAbsHorizontal(Config.timerXLocation, smartscreen.screenshotXCoordinate),
				smartscreen.relToAbsHorizontal(Config.timerYLocation, smartscreen.screenshotYCoordinate), robot);
		whiteListener.printLocation();
		
		BufferedImage phoneScreen = robot.createScreenCapture(smartscreen.getRectangle());
		ImageProcessor processor = new ImageProcessor(phoneScreen);

		ImageIO.write(phoneScreen, "png",
				new File((new StringBuilder(Config.mainDirectory).append("phoneScreen.png").toString())));

		System.out.println((new StringBuilder("Is live show: ")).append(Config.isLiveShow));

		// Do everything in this forever
		while (true) {
			timerListener.refreshPixelListener();
			whiteListener.refreshPixelListener();

			// Stay in this loop until the wheel has turned gray/green and white box showed up
			while (!((timerListener.isGray() || timerListener.isGreen()) && whiteListener.isWhite())) {
				timerListener.refreshPixelListener();
				whiteListener.refreshPixelListener();
			}

			// Exited loop, this means a question popped up
			System.out.println("Detected Question");

			// Waiting for the cash show text to load
			Thread.sleep(700);
			phoneScreen = robot.createScreenCapture(smartscreen.getRectangle());
			processor = new ImageProcessor(phoneScreen);

			questionText = processor.getQuestionText();
			allAnswers = processor.getAnswerList();
			System.out.println(processor.rawQuestionText);
			googleResultsString = GoogleSearcher.getGoogleResultsString(questionText);

			for (String o : processor.rawAnswerStrings) {
				System.out.println((new StringBuilder("***")).append(o));

			}

			for (int i = 0; i < 3; i++) {
				try {

					allScores[i] += Algorithms.primaryAlgorithm(questionText, googleResultsString, allAnswers[i]);

					String[] splitAnswerText = allAnswers[i].split(" ");
					for (String o : splitAnswerText) {
						if (questionText.toLowerCase().contains(o.toLowerCase())) {
							System.out.println("Question contains Answer String");
							allScores[i] -= Algorithms.occuranceAlgorithmScore(googleResultsString.toLowerCase(),
									o.toLowerCase());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// If the first algorithm failed, try the other one
			if ((allScores[0] < 2 && allScores[1] < 2 && (allScores[2] < 2))) {
				System.out.println("No results, alternate started");
				for (int i = 0; i < 3; i++) {
					allScores[i] = Algorithms.googleResultsAlgorithm(questionText, allAnswers[i]);
				}
			}

			// Print out the answers with their respective scores
			for (int i = 0; i < 3; i++) {
				System.out
						.println((new StringBuilder(processor.rawAnswerStrings[i])).append(": ").append(allScores[i]));
				allScores[i] = 0;
			}

			whiteListener.refreshPixelListener();

			while ((whiteListener.isWhite())) {
				whiteListener.refreshPixelListener();
			}

			System.out.println("Screen changed away from question");

			timerListener.refreshPixelListener();
			whiteListener.refreshPixelListener();
			// Wait until the question showed up
			while (!((timerListener.isGray() || timerListener.isGreen()) && whiteListener.isWhite())) {
				timerListener.refreshPixelListener();
				whiteListener.refreshPixelListener();
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