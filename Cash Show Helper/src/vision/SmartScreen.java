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
	public int phoneHeight;
	public double phoneWidth;

	public SmartScreen(double monitorX, double monitorY, double tHeight, double bHeight) {
		monitorScreenHeight = monitorY;
		monitorScreenWidth = monitorX;
		titleHeight = tHeight;
		taskbarHeight = bHeight;

	}

	public void actualSmartScreenCheck() {
		phoneHeight = (int) Math.round(monitorScreenHeight - (taskbarHeight + titleHeight));
		phoneWidth = (int) Math.round(9.0 * phoneHeight / 16.0);
		screenshotXCoordinate = (int) Math.round((monitorScreenWidth - phoneWidth) / 2.0);
		screenshotYCoordinate = (int) titleHeight;
	}

	public double scaleToAnyScreen(int dimension) {
		return phoneHeight * dimension / 1080.0;
	}

	/**
	 * 
	 * @param relLocation
	 * @param baseCoordinate
	 * @return
	 */
	public int relToAbsHorizontal(int relLocation, int baseCoordinate) {
		return (int) Math.round(baseCoordinate + relLocation / 557.0 * phoneWidth);
	}
	
	

	//	/**
	//	 * Calculates and returns the x, y, width, and height of the phone screen
	//	 * based on overall resolution
	//	 * @param output Console Output object which is used to find the height of the window
	//	 * @return Integer array with the calculated data, this can be passed
	//	 *         straight to rectangle coordinates
	//	 */
	//	public static Rectangle runSmartScreenCheck(ConsoleOutput output) {
	//		thisOutput = output;
	//		int availableHeight = (int) Math.round(monitorScreenHeight - (getTaskbarSize() + output.getConsoleHeight()));
	//		int availableWidth = (int) Math.round(availableHeight * monitorScreenHeight / monitorScreenWidth);
	//		screenshotXCoordinate = (int) Math.round((monitorScreenWidth - availableWidth) / 2);
	//		screenshotYCoordinate = output.getConsoleHeight();
	//		return new Rectangle(screenshotXCoordinate, screenshotYCoordinate, availableWidth, availableHeight);
	//	}

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
		System.out.println(screenshotXCoordinate);
		System.out.println(screenshotYCoordinate);
		System.out.println(phoneWidth);
		System.out.println(phoneHeight);

		
		
		return (new Rectangle(screenshotXCoordinate, screenshotYCoordinate, (int) phoneWidth, phoneHeight));
	}

	public static void main(String[] args) {
		ConsoleOutput output = new ConsoleOutput();
		output.show();
		SmartScreen screen = new SmartScreen(ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight(), output.getConsoleHeight(), ScreenUtils.getTaskbarHeight());
		screen.actualSmartScreenCheck();
		
		System.out.println(screen.screenshotXCoordinate);
		System.out.println(screen.screenshotYCoordinate);
		System.out.println(screen.phoneWidth);
		System.out.println(screen.phoneHeight);
	}
}
