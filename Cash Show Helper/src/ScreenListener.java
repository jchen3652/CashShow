import java.awt.AWTException;
import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;

import consoleOutput.ConsoleOutput;

public class ScreenListener {
	public static boolean newQuestionExists = false;
	static File outputfile = new File("D:\\Users\\James\\Desktop\\testscreenshot.png");
	static int oldColor = 0;
	public static boolean restartEverything = false;
	static String[] allAnswers = null;
	static int[] allScores = new int[3];
	static String questionText;
	static String fullSearchableText;
	public static final String screenshotDirectory = "D:\\Users\\James\\Desktop\\";
	public static final String screenshotIdentifier = "testscreenshot.png";
	
	public static void main(String[] args) throws AWTException, IOException, InterruptedException {
		//new ConsoleOutput().show();
		Thread.sleep(2000);
		Robot robot = new Robot();
		robot.setAutoDelay(0);
		robot.setAutoWaitForIdle(false);
		while (1 == 1) {
			while (!newQuestionExists) {
				//System.out.println("new question");
				//System.out.println(new Date());
				
				//Rectangle screenRect = new Rectangle();
				//screenRect.setBounds(680, 40, 557, 990); //actual good phone screen
				
				
				//screenRect.setBounds(953, 132, 4, 4);
				//BufferedImage img = robot.createScreenCapture(screenRect);
				

				if (robot.getPixelColor(955, 134).getRGB() == -3026222) {
					Rectangle screenRect = new Rectangle();
					screenRect.setBounds(680, 40, 557, 990); //actual good phone screen
					
					
					//screenRect.setBounds(953, 132, 4, 4);
					BufferedImage img = robot.createScreenCapture(screenRect);
					ImageIO.write(img, "png", outputfile);
					
					ImageProcessor processor = new ImageProcessor(screenshotDirectory + screenshotIdentifier);
					questionText = processor.getQuestionText();		
					allAnswers = processor.getAnswerList();
					System.out.println(processor.humanQuestionText);
					fullSearchableText = googleSearcher.loadFullSearchableText(questionText);
					
					
					for (String o : processor.humanAnswerText) {
						System.out.println("***" + o);

					}
				
					// System.out.println(fullSearchableText);
					for (int i = 0; i < 3; i++) {
						allScores[i] += Algorithms.occuranceAlgorithmScore(
								fullSearchableText, allAnswers[i]);
						System.out.println(processor.humanAnswerText[i] + ": " + allScores[i]);
						allScores[i] = 0;
					}
					
				//	newQuestionExists = true;
					while(robot.getPixelColor(955, 134).getRGB() == -3026222 ||robot.getPixelColor(955, 134).getRGB() == -16777216) {
						
					}
					System.out.println(robot.getPixelColor(955, 134).getRGB());
				} 
			}
			

			
			
			
			/*while(newQuestionExists) {
				
					
					//Rectangle screenRect = new Rectangle();
					//screenRect.setBounds(953, 132, 4, 4);
				//BufferedImage img = robot.createScreenCapture(screenRect);
				if (robot.getPixelColor(955, 134).getRGB() != -3026222) {
					newQuestionExists = false;
				}
			}*/
			
		}

	}
}