package main;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import algorithms.Algorithms;
import chromeWindow.ChromeWindow;
import consoleOutput.ConsoleOutput;
import threads.AnswerThread;
import threads.GoogleSearcherThread;
import threads.QuestionThread;
import vision.ImageProcessor;
import vision.PixelListener;
import vision.ScreenUtils;
import vision.SmartScreen;

public class Main {
	public static ConsoleOutput console;

	private static PixelListener timerListener;
	private static PixelListener whiteListener;
	private static Robot robot;
	private static SmartScreen smartscreen;
	private static ImageProcessor processor = new ImageProcessor();
	private static BufferedImage phoneScreen;
	private static WebDriver driver;
	public static String tessDataPath;

	public static void main(String[] args) throws AWTException, IOException, InterruptedException {
		

		//String[] allAnswers = null;
		//String questionText = null;
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

		// 
		driver.get("about:blank");
		for (int i = 0; i < 5; i++) {
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_MINUS);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			robot.keyRelease(KeyEvent.VK_MINUS);
		}

		console = new ConsoleOutput();
		console.setVisible(true);
		smartscreen = new SmartScreen(ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight(),
				console.getConsoleHeight(), ScreenUtils.getTaskbarHeight());
		smartscreen.getScreenInformation();
		robot.setAutoDelay(0);
		robot.setAutoWaitForIdle(false);

		whiteListener = new PixelListener(
				smartscreen.scaleToNewMonitor(Config.whiteXLocation, smartscreen.screenshotXCoordinate),
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

			// Exited loop, this means a question popped up
			console.println("Detected Question");
			Trivia trivia = new Trivia();
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
			while (trivia.getQuestionText() == null || trivia.getAnswerArray() == null) {
				if (trivia.getQuestionText() == null) {
					trivia.setQuestionText(qt.getQuestionText());
					if (trivia.getQuestionText() != null) {

						
						(new ChromeWindow(driver, trivia.getFilteredQuestionText())).run();

						console.println(processor.rawQuestionText);
						gt.setQuery(trivia.getQuestionText());
						gt.run();
					}
				}
				if (trivia.getAnswerArray() == null) {
					trivia.setAnswerArray(at.getAnswerList());
					if (trivia.getAnswerArray() != null) {
						trivia.setAnswerArray(at.getAnswerList());
						for (String o : processor.rawAnswerStrings) {
							console.println((new StringBuilder("***")).append(o).toString());
						}
					}
				}
			}

			trivia.setQuestionText(qt.getQuestionText());
			
			trivia.setJSONTools(gt.getResult());

			trivia.calculate();
			trivia.printInfo();

			whiteListener.refreshPixelListener();
			while ((whiteListener.isWhite())) {
				whiteListener.refreshPixelListener();
			}

			console.println("Screen changed away from question");

			timerListener.refreshPixelListener();
			whiteListener.refreshPixelListener();

			// Wait until the question or answer reveal shows up
			while (!((timerListener.isGray() || timerListener.isGreen()) && whiteListener.isWhite())) {
				timerListener.refreshPixelListener();
				whiteListener.refreshPixelListener();
			}

			if (Config.isLiveShow) {
				console.println("JK it's actually the answer reveal, waiting until it's over");

				whiteListener.refreshPixelListener();
				while (whiteListener.isWhite()) {
					whiteListener.refreshPixelListener();
				}

				console.println("Answer Reveal is over");

				timerListener.refreshPixelListener();
				Thread.sleep(50);
			} else {
				console.println("Screen changed back to questions");
			}
			
		
		}
	}
}