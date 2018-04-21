package vision;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;

import consoleOutput.ConsoleOutput;

public class SmartScreen {

	double titleHeight;
	double taskbarHeight;
	double monitorScreenHeight;
	double monitorScreenWidth;

	public int screenshotXCoordinate;
	public int screenshotYCoordinate;
	public double unroundedScreenshotXCoordinate;
	public double unroundedScreenshotYCoordinate;
	
	public double phoneHeight;
	public double phoneWidth;

	public SmartScreen(double monitorX, double monitorY, double tHeight, double bHeight) {
		monitorScreenHeight = monitorY;
		monitorScreenWidth = monitorX;
		titleHeight = tHeight;
		taskbarHeight = bHeight;

	}

	public void getScreenInformation() {
		phoneHeight = (monitorScreenHeight - (taskbarHeight + titleHeight));
		phoneWidth = (9.0 * phoneHeight / 16.0);
		unroundedScreenshotXCoordinate = (monitorScreenWidth - phoneWidth) / 2.0;
		unroundedScreenshotYCoordinate = titleHeight;
		
		screenshotXCoordinate = (int) Math.round((monitorScreenWidth - phoneWidth) / 2.0);
		screenshotYCoordinate = (int) titleHeight;
	}

	public double scaleToAnyScreen(int dimension) {
		return phoneHeight * (double) dimension / 1080.0;
	}

	/**
	 * 
	 * @param relLocation
	 * @param baseCoordinate
	 * @return
	 */
	public int relToAbsHorizontal(double relLocation, double baseCoordinate) {
		return (int) Math.round(baseCoordinate + relLocation / 557.0 * phoneWidth);
	}


	/**
	 * Calculates the
	 * 
	 * @return
	 */
	public static int getTaskbarSize() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice sd = ge.getDefaultScreenDevice();
		GraphicsConfiguration[] gc = sd.getConfigurations();
		Insets bounds = Toolkit.getDefaultToolkit().getScreenInsets(gc[0]);
		return bounds.bottom;

	}

	public Rectangle getRectangle() {


		return (new Rectangle(screenshotXCoordinate, screenshotYCoordinate, (int) phoneWidth, (int) phoneHeight));
	}

	public static void main(String[] args) {
		ConsoleOutput output = new ConsoleOutput();
		output.setVisible(true);
		SmartScreen screen = new SmartScreen(ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight(),
				output.getConsoleHeight(), ScreenUtils.getTaskbarHeight());
		screen.getScreenInformation();

		System.out.println(screen.screenshotXCoordinate);
		System.out.println(screen.screenshotYCoordinate);
		System.out.println(screen.phoneWidth);
		System.out.println(screen.phoneHeight);
	}
}
