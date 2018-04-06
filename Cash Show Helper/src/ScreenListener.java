import java.awt.AWTException;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;

public class ScreenListener {
	public static boolean newQuestionExists = false;
	static File outputfile = new File("D:\\Users\\James\\Desktop\\testscreenshot.png");
	static int oldColor = 0;
	public static boolean restartEverything = false;

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
					//ImageIO.write(img, "png", outputfile);
					System.out.println("Performed payload");
				//	newQuestionExists = true;
					while(robot.getPixelColor(955, 134).getRGB() == -3026222) {
						System.out.println("doing nothing because payloads been deployed");
					}
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