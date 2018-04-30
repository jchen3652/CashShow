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
import algorithms.GoogleSearcherThread;
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
	public static ConsoleOutput console;
	
	
	private static int[] allScores = {0, 0, 0};

	private static PixelListener timerListener;
	private static PixelListener whiteListener;
	private static Robot robot;
	private static SmartScreen smartscreen;
	private static ImageProcessor processor = new ImageProcessor();
	private static BufferedImage phoneScreen;
	private static WebDriver driver;
	public static String tessDataPath;

	public static void main(String[] args) throws AWTException, IOException, InterruptedException {
		Trivia trivia = new Trivia();
		
		
		String[] allAnswers = null;
		String questionText = null;
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

		console = new ConsoleOutput();
		console.setVisible(true);
		smartscreen = new SmartScreen(ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight(),
				console.getConsoleHeight(), ScreenUtils.getTaskbarHeight());
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
		console.println("MAKE SURE YOU LOOK AT THE CHROME WINDOW, RETARD");
		console.println((new StringBuilder("Is live show: ")).append(Config.isLiveShow).toString());

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
			console.println("Detected Question");

			// Waiting for the cash show text to load
			Thread.sleep(750);
			console.println("Done waiting");
			phoneScreen = robot.createScreenCapture(smartscreen.getPhoneRectangle());
			processor.setImage(phoneScreen);

			AnswerThread at = new AnswerThread(processor);
			QuestionThread qt = new QuestionThread(processor);
			GoogleSearcherThread gt = new GoogleSearcherThread();
			
			(qt).run();
			(at).run();
			while(questionText == null || allAnswers == null) {
				if(questionText == null ) {
					questionText = qt.getQuestionText();
					if(questionText != null) {
						(new ChromeWindow(driver, questionText)).run();
						gt.setQuery(questionText);
						gt.run();
						allAnswers = at.getAnswerList();
					}
				}
				if(allAnswers != null) {
					allAnswers = at.getAnswerList();
				}
			}
			questionText = qt.getQuestionText();

			
			
			
			console.println(processor.rawQuestionText);
			for (String o : processor.rawAnswerStrings) {
				console.println((new StringBuilder("***")).append(o).toString());

			}

			String googleResultsString = gt.getResult();
			trivia.setQuestionText(qt.getQuestionText());
			trivia.setAnswerList(allAnswers);
			trivia.setGoogleResult(googleResultsString);
			trivia.calculate();
		

			console.println((new StringBuilder("Best Answer: ").append(allAnswers[trivia.getBestScoreIndex()])).toString());

			whiteListener.refreshPixelListener();

			while ((whiteListener.isWhite())) {
				whiteListener.refreshPixelListener();
			}

			console.println("Screen changed away from question");

			timerListener.refreshPixelListener();
			whiteListener.refreshPixelListener();
			// Wait until the question showed up
			while (!((timerListener.isGray() || timerListener.isGreen()) && whiteListener.isWhite())) {
				timerListener.refreshPixelListener();
				whiteListener.refreshPixelListener();
			}

			console.println("Screen changed back to questions");

			if (Config.isLiveShow) {
				console.println("JK it's actually the answer reveal, waiting until it's over");

				whiteListener.refreshPixelListener();
				while (whiteListener.isWhite()) {
					whiteListener.refreshPixelListener();
				}

				console.println("Answer Reveal is over");

				timerListener.refreshPixelListener();
				Thread.sleep(50);
			}
			allAnswers = null;
			questionText = null;
		}
	}
}