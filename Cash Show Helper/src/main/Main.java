package main;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import algorithms.Algorithms;
import algorithms.GoogleSearcher;
import algorithms.PrimaryAlgorithmThread;
import chromeWindow.ChromeWindow;
import consoleOutput.ConsoleOutput;
import vision.AnswerThread;
import vision.ImageProcessor;
import vision.PixelListener;
import vision.QuestionThread;
import vision.ScreenUtils;
import vision.SmartScreen;

public class Main {
	public static ConsoleOutput output;
	
	private static String[] allAnswers;
	private static int[] allScores = {0, 0, 0};
	private static String questionText;
	private static PixelListener timerListener;
	private static PixelListener whiteListener;
	private static Robot robot;
	private static SmartScreen smartscreen;
	private static ImageProcessor processor = new ImageProcessor();
	private static BufferedImage phoneScreen;
	private static WebDriver driver;
	public static String tessDataPath;

	public static void main(String[] args) throws AWTException, IOException, InterruptedException {
		File tessDataFolder = new File("Tesseract-OCR");
		tessDataPath = tessDataFolder.getAbsolutePath();

		robot = new Robot();
		System.setProperty("webdriver.chrome.args", "--disable-logging");
		System.setProperty("webdriver.chrome.silentOutput", "true");

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--log-level=3");

		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
		driver = new ChromeDriver(options);
		driver.switchTo().defaultContent();
		org.openqa.selenium.Point windowPosition = new org.openqa.selenium.Point(0, 0);
		Main.driver.manage().window().setPosition(windowPosition);
		Main.driver.manage().window().setSize(
				new Dimension((int) ((int) ScreenUtils.getScreenWidth() / 3.5), (int) ScreenUtils.getScreenHeight()));

		driver.get("http://www.bing.com");
		for (int i = 0; i < 5; i++) {
			Main.robot.keyPress(KeyEvent.VK_CONTROL);
			Main.robot.keyPress(KeyEvent.VK_MINUS);
			Main.robot.keyRelease(KeyEvent.VK_CONTROL);
			Main.robot.keyRelease(KeyEvent.VK_MINUS);
		}

		output = new ConsoleOutput();
		output.setVisible(true);
		smartscreen = new SmartScreen(ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight(),
				output.getConsoleHeight(), ScreenUtils.getTaskbarHeight());
		smartscreen.getScreenInformation();
		robot.setAutoDelay(0);
		robot.setAutoWaitForIdle(false);

		whiteListener = new PixelListener(
				smartscreen.scaleToNewMonitor(Config.whiteXLocation, smartscreen.unroundedScreenshotXCoordinate),
				smartscreen.scaleToNewMonitor(Config.whiteYLocation, smartscreen.unroundedScreenshotYCoordinate),
				robot);

		timerListener = new PixelListener(
				smartscreen.scaleToNewMonitor(Config.timerXLocation, smartscreen.screenshotXCoordinate),
				smartscreen.scaleToNewMonitor(Config.timerYLocation, smartscreen.screenshotYCoordinate), robot);

		output.println((new StringBuilder("Is live show: ")).append(Config.isLiveShow).toString());

		// Do everything in this forever
		while (true) {
			timerListener.refreshPixelListener();
			whiteListener.refreshPixelListener();

			// Do nothing until the wheel has turned gray/green and white box showed up
			while (!((timerListener.isGray() || timerListener.isGreen()) && whiteListener.isWhite())) {
				timerListener.refreshPixelListener();
				whiteListener.refreshPixelListener();
			}

			// Left loop, this means a question popped up
			output.println("Detected Question");

			// Waiting for the cash show text to load
			Thread.sleep(750);
			output.println("Done waiting");
			phoneScreen = robot.createScreenCapture(smartscreen.getPhoneRectangle());
			processor.setImage(phoneScreen);

			AnswerThread at = new AnswerThread(processor);
			QuestionThread qt = new QuestionThread(processor);
			(qt).run();
			(at).run();
			questionText = qt.getQuestionText();

			(new ChromeWindow(driver, questionText)).run();
			allAnswers = at.getAnswerList();

			output.println(processor.rawQuestionText);
			for (String o : processor.rawAnswerStrings) {
				output.println((new StringBuilder("***")).append(o).toString());

			}

			String googleResultsString = GoogleSearcher.getGoogleResultsString(questionText);
			
			PrimaryAlgorithmThread[] algorithms = new PrimaryAlgorithmThread[3];
			
			for(int i = 0; i < 3; i ++) {
				algorithms[i] = new PrimaryAlgorithmThread(questionText, googleResultsString, allAnswers[i]);
				algorithms[i].run();
			}
			
			for (int i = 0; i < 3; i++) {
				try {

					allScores[i] += algorithms[i].getScore();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			
			
			
			

			// If the first algorithm failed, try the other one
			if ((allScores[0] < 2 && allScores[1] < 2 && (allScores[2] < 2))) {
				output.println("No results, alternate started");
				for (int i = 0; i < 3; i++) {
					allScores[i] = Algorithms.googleResultsAlgorithm(questionText, allAnswers[i]);
				}

				boolean lessThanThreshold = false;

				for (int i : allScores) {
					if (i < 1000) {
						lessThanThreshold = true;
					}
				}
				if (!lessThanThreshold) {
					for (int i : allScores) {
						i = (int) Math.round(((double) i) / Config.googleResultsScaleDown);
					}
				}
			}

			// Determines the largest scores and prints out the results
			int largestIndex = 0;
			int largestScore = 0;
			for (int i = 0; i < 3; i++) {
				output.println((new StringBuilder(processor.rawAnswerStrings[i])).append(": ").append(allScores[i])
						.toString());
				if (allScores[i] > largestScore) {
					largestScore = allScores[i];
					largestIndex = i;
				}

				allScores[i] = 0;
			}

			output.println((new StringBuilder("Best Answer: ").append(allAnswers[largestIndex])).toString());

			whiteListener.refreshPixelListener();

			while ((whiteListener.isWhite())) {
				whiteListener.refreshPixelListener();
			}

			output.println("Screen changed away from question");

			timerListener.refreshPixelListener();
			whiteListener.refreshPixelListener();
			// Wait until the question showed up
			while (!((timerListener.isGray() || timerListener.isGreen()) && whiteListener.isWhite())) {
				timerListener.refreshPixelListener();
				whiteListener.refreshPixelListener();
			}

			output.println("Screen changed back to questions");

			if (Config.isLiveShow) {
				output.println("JK it's actually the answer reveal, waiting until it's over");

				whiteListener.refreshPixelListener();
				while (whiteListener.isWhite()) {
					whiteListener.refreshPixelListener();
				}

				output.println("Answer Reveal is over");

				timerListener.refreshPixelListener();
				Thread.sleep(50);
			}
		}
	}
}