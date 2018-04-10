package main;

import java.awt.AWTException;
import java.awt.Color;
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
import vision.ScreenListenerAlgorithms;

public class Main {

	private static int[] allScores = new int[3];
	static String questionText;

	static String fullSearchableText;
	public static final String screenshotDirectory = "D:\\Users\\James\\Desktop\\";
	public static final String screenshotIdentifier = "testscreenshot.png";
	static File rawOutputfile = new File(screenshotDirectory + screenshotIdentifier);

	

	

	static Color pixelColor;

	private static ScreenListenerAlgorithms algorithms = new ScreenListenerAlgorithms();

	public static final boolean isLiveShow = true;

	private static final int[] phoneScreenArea = {680, 40, 557, 990};

	public static void main(String[] args) throws AWTException, IOException, InterruptedException {
		new ConsoleOutput().setVisible(true);

		System.out.println("Is live show: " + false);
		Thread.sleep(4000);
		Robot robot = new Robot();
		robot.setAutoDelay(0);
		robot.setAutoWaitForIdle(false);
		while (true) {
			algorithms.refreshPixelListener(Config.timerPixelXLocation, Config.timerPixelYLocation, robot);
			if (algorithms.isGray() || algorithms.isGreen()) {
				long start = System.currentTimeMillis();
			
				Rectangle screenRect = new Rectangle();
				screenRect.setBounds(phoneScreenArea[0], phoneScreenArea[1], phoneScreenArea[2], phoneScreenArea[3]); // actual good phone screen
				Thread.sleep(1050);
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
				
				if (Algorithms.arrayContains(questionText, processor.humanAnswerText)) {
					System.out.println("compensating");
					for (int i = 0; i < 3; i++) {
						allScores[i] -= Algorithms.occuranceAlgorithmScore(fullSearchableText, allAnswers[i]);

					}
				}
				for (int i = 0; i < 3; i++) {
					System.out.println(processor.humanAnswerText[i] + ": " + allScores[i]);
				}
				System.out.println((System.currentTimeMillis() - start) / 1000.0);
				
				
				algorithms.refreshPixelListener(Config.whitePixelXLocation, Config.whitePixelYLocation, robot);
				System.out.println(algorithms.getR());
				System.out.println(algorithms.getG());
				System.out.println(algorithms.getB());

				while ((/* isGray || isGreen || */ algorithms.isWhite())) {
					algorithms.refreshPixelListener(Config.whitePixelXLocation, Config.whitePixelYLocation, robot);
					
				}

				System.out.println("Screen changed");
				
				algorithms.refreshPixelListener(Config.timerPixelXLocation, Config.timerPixelYLocation, robot);
				
				while (!algorithms.isGray() && !algorithms.isGreen()) {

					//do nothing
					algorithms.refreshPixelListener(Config.timerPixelXLocation, Config.timerPixelYLocation, robot);
					
				}
				System.out.println("Screen changed back to questions");

				if (isLiveShow) {
					System.out.print(" jk it's the answer reveal, waiting until it's not");
					algorithms.refreshPixelListener(Config.timerPixelXLocation, Config.timerPixelYLocation, robot);
					while (algorithms.isGray() || algorithms.isGreen()) {

						algorithms.refreshPixelListener(Config.timerPixelXLocation, Config.timerPixelYLocation, robot);
						
					}
					System.out.println("Now answer reveal is over");
				}
			}
		}
	}
}