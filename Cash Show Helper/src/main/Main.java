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

public class Main {

	private static int[] allScores = new int[3];
	static String questionText;

	static String fullSearchableText;
	public static final String screenshotDirectory = "D:\\Users\\James\\Desktop\\";
	public static final String screenshotIdentifier = "testscreenshot.png";
	static File rawOutputfile = new File(screenshotDirectory + screenshotIdentifier);

	public static final int pixelLocationX = 951; // 955 is left, 964 is right
	public static final int pixelLocationY = 135;
	
	public static final int alternatePixelLocationX = 950;
	public static final int alternatePixelLocationY = 335;
	
	public static final int[] grayRToleranceRange = {200, 230};
	public static final int[] grayGToleranceRange = {200, 230};
	public static final int[] grayBToleranceRange = {205, 230};

	public static final int[] greenRToleranceRange = {50, 75};
	public static final int[] greenGToleranceRange = {215, 245};
	public static final int[] greenBToleranceRange = {50, 70};
	
	public static final int[] randomRToleranceRange = {254, 255};
	public static final int[] randomGToleranceRange = {254, 255};
	public static final int[] randomBToleranceRange = {254, 255};
	
	

	public static final boolean isLiveShow = false;

	private static final int[] phoneScreenArea = {680, 40, 557, 990};

	public static void main(String[] args) throws AWTException, IOException, InterruptedException {
		new ConsoleOutput().show();
		System.out.println("Is live show: " + false);
		Thread.sleep(4000);
		Robot robot = new Robot();
		robot.setAutoDelay(0);
		robot.setAutoWaitForIdle(false);
		while (true) {

			// System.out.println("new question");
			// System.out.println(new Date());

			// Rectangle screenRect = new Rectangle();
			// screenRect.setBounds(680, 40, 557, 990); //actual good phone screen

			// screenRect.setBounds(953, 132, 4, 4);
			// BufferedImage img = robot.createScreenCapture(screenRect);
			Color pixelColor = robot.getPixelColor(pixelLocationX, pixelLocationY);
			int red = pixelColor.getRed();
			int green = pixelColor.getGreen();
			int blue = pixelColor.getBlue();
			boolean isGray = ((red > grayRToleranceRange[0]) && (red < grayRToleranceRange[1]))
					&& ((green > grayGToleranceRange[0]) && (green < grayGToleranceRange[1]))
					&& ((blue > grayBToleranceRange[0]) && (blue < grayBToleranceRange[1]));
			boolean isGreen = ((red > greenRToleranceRange[0]) && (red < greenRToleranceRange[1]))
					&& ((green > greenGToleranceRange[0]) && (green < greenGToleranceRange[1]))
					&& ((blue > greenBToleranceRange[0]) && (blue < greenBToleranceRange[1]));
			if (isGray || isGreen) {

				System.out.println("Is Gray: " + isGray);
				System.out.println("Is Green: " + isGreen);
				Rectangle screenRect = new Rectangle();
				screenRect.setBounds(phoneScreenArea[0], phoneScreenArea[1], phoneScreenArea[2], phoneScreenArea[3]); // actual good phone screen

				Thread.sleep(1000);
				BufferedImage img = robot.createScreenCapture(screenRect);
				ImageIO.write(img, "png", rawOutputfile);

				ImageProcessor processor = new ImageProcessor(screenshotDirectory + screenshotIdentifier);
				questionText = processor.getQuestionText();
				String[] allAnswers = processor.getAnswerList();
				System.out.println(processor.humanQuestionText);
				fullSearchableText = GoogleSearcher.loadFullSearchableText(questionText);

				for (String o : processor.humanAnswerText) {
					System.out.println("***" + o);

				}

				// System.out.println(fullSearchableText);

				for (int i = 0; i < 3; i++) {
					try {
						allScores[i] = 0;
						allScores[i] += Algorithms.primaryAlgorithm(fullSearchableText, allAnswers[i]);
						System.out.println(processor.humanAnswerText[i] + ": " + allScores[i]);
					} catch (Exception e) {
						System.out.println("Not enough results on the scanned area");
					}
				}

				pixelColor = robot.getPixelColor(alternatePixelLocationX, alternatePixelLocationY);
				red = pixelColor.getRed();
				green = pixelColor.getGreen();
				blue = pixelColor.getBlue();
//				isGray = ((red > grayRToleranceRange[0]) && (red < grayRToleranceRange[1]))
//						&& ((green > grayGToleranceRange[0]) && (green < grayGToleranceRange[1]))
//						&& ((blue > grayBToleranceRange[0]) && (blue < grayBToleranceRange[1]));
//				isGreen = ((red > greenRToleranceRange[0]) && (red < greenRToleranceRange[1]))
//						&& ((green > greenGToleranceRange[0]) && (green < greenGToleranceRange[1]))
//						&& ((blue > greenBToleranceRange[0]) && (blue < greenBToleranceRange[1]));
				boolean isRandom = ((red >= randomRToleranceRange[0]) && (red <= randomRToleranceRange[1]))
						&& ((green >= randomGToleranceRange[0]) && (green <= randomGToleranceRange[1]))
						&& ((blue >= randomBToleranceRange[0]) && (blue <= randomBToleranceRange[1]));
				// newQuestionExists = true;
			
				while ((/*isGray || isGreen ||*/ isRandom)) {

					//do nothing
					pixelColor = robot.getPixelColor(alternatePixelLocationX, alternatePixelLocationY);
					red = pixelColor.getRed();
					green = pixelColor.getGreen();
					blue = pixelColor.getBlue();
					/*isGray = ((red > grayRToleranceRange[0]) && (red < grayRToleranceRange[1]))
							&& ((green > grayGToleranceRange[0]) && (green < grayGToleranceRange[1]))
							&& ((blue > grayBToleranceRange[0]) && (blue < grayBToleranceRange[1]));
					isGreen = ((red > greenRToleranceRange[0]) && (red < greenRToleranceRange[1]))
							&& ((green > greenGToleranceRange[0]) && (green < greenGToleranceRange[1]))
							&& ((blue > greenBToleranceRange[0]) && (blue < greenBToleranceRange[1]));*/
					isRandom = ((red >= randomRToleranceRange[0]) && (red <= randomRToleranceRange[1]))
							&& ((green >= randomGToleranceRange[0]) && (green <= randomGToleranceRange[1]))
							&& ((blue >= randomBToleranceRange[0]) && (blue <= randomBToleranceRange[1]));
				

				}
				System.out.println("r" + red);
				System.out.println("g" + green);
				System.out.println("b" + blue);
				System.out.println("Screen changed");
				pixelColor = robot.getPixelColor(pixelLocationX, pixelLocationY);
				red = pixelColor.getRed();
				green = pixelColor.getGreen();
				blue = pixelColor.getBlue();
				isGray = ((red > grayRToleranceRange[0]) && (red < grayRToleranceRange[1]))
						&& ((green > grayGToleranceRange[0]) && (green < grayGToleranceRange[1]))
						&& ((blue > grayBToleranceRange[0]) && (blue < grayBToleranceRange[1]));
				isGreen = ((red > greenRToleranceRange[0]) && (red < greenRToleranceRange[1]))
						&& ((green > greenGToleranceRange[0]) && (green < greenGToleranceRange[1]))
						&& ((blue > greenBToleranceRange[0]) && (blue < greenBToleranceRange[1]));
				while (!isGray && !isGreen) {

					//do nothing
					pixelColor = robot.getPixelColor(pixelLocationX, pixelLocationY);
					red = pixelColor.getRed();
					green = pixelColor.getGreen();
					blue = pixelColor.getBlue();
					isGray = ((red > grayRToleranceRange[0]) && (red < grayRToleranceRange[1]))
							&& ((green > grayGToleranceRange[0]) && (green < grayGToleranceRange[1]))
							&& ((blue > grayBToleranceRange[0]) && (blue < grayBToleranceRange[1]));
					isGreen = ((red > greenRToleranceRange[0]) && (red < greenRToleranceRange[1]))
							&& ((green > greenGToleranceRange[0]) && (green < greenGToleranceRange[1]))
							&& ((blue > greenBToleranceRange[0]) && (blue < greenBToleranceRange[1]));
				}
				System.out.println("Screen changed back to questions");
				
				if (isLiveShow) {
					System.out.print(" jk it's the answer reveal, waiting until it's not");
					while (isGray || isGreen) {

						pixelColor = robot.getPixelColor(pixelLocationX, pixelLocationY);
						red = pixelColor.getRed();
						green = pixelColor.getGreen();
						blue = pixelColor.getBlue();
						isGray = ((red > grayRToleranceRange[0]) && (red < grayRToleranceRange[1]))
								&& ((green > grayGToleranceRange[0]) && (green < grayGToleranceRange[1]))
								&& ((blue > grayBToleranceRange[0]) && (blue < grayBToleranceRange[1]));
						isGreen = ((red > greenRToleranceRange[0]) && (red < greenRToleranceRange[1]))
								&& ((green > greenGToleranceRange[0]) && (green < greenGToleranceRange[1]))
								&& ((blue > greenBToleranceRange[0]) && (blue < greenBToleranceRange[1]));
					}
					System.out.println("Now answer reveal is over");
				}
			}
		}
	}
}