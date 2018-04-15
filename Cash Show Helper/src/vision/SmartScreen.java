package vision;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;

import consoleOutput.ConsoleOutput;
import main.Main;

public class SmartScreen {
	static ConsoleOutput thisOutput;

	public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	public static int monitorScreenHeight = (int) screenSize.getHeight();
	public static int monitorScreenWidth = (int) screenSize.getWidth();
	public static int screenshotXCoordinate;
	public static int screenshotYCoordinate;

	public SmartScreen() {
		runSmartScreenCheck(Main.output);
	}

	public static double scaleToAnyScreen(int dimension) {
		return monitorScreenHeight * dimension / 1080.0;
	}

	/**
	 * 
	 * @param relLocation
	 * @param baseCoordinate
	 * @return
	 */
	public static int relToAbsPixLoc(int relLocation, int baseCoordinate) {
		return (int) Math.round(baseCoordinate + scaleToAnyScreen(relLocation));
	}

	/**
	 * Calculates and returns the x, y, width, and height of the phone screen
	 * based on overall resolution
	 * @param output Console Output object which is used to find the height of the window
	 * @return Integer array with the calculated data, this can be passed
	 *         straight to rectangle coordinates
	 */
	public static Rectangle runSmartScreenCheck(ConsoleOutput output) {
		thisOutput = output;
		int availableHeight = (int) Math.round(monitorScreenHeight - (getTaskbarSize() + getTitleHeight()));
		int availableWidth = (int) Math.round(availableHeight * monitorScreenHeight / monitorScreenWidth);
		screenshotXCoordinate = (int) Math.round((monitorScreenWidth - availableWidth) / 2);
		screenshotYCoordinate = getTitleHeight();
		return new Rectangle(screenshotXCoordinate, screenshotYCoordinate, availableWidth, availableHeight);
	}

	/**
	 * Calculates the 
	 * @return
	 */
	public static int getTaskbarSize() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice sd = ge.getDefaultScreenDevice();
		GraphicsConfiguration[] gc = sd.getConfigurations();
		Insets bounds = Toolkit.getDefaultToolkit().getScreenInsets(gc[0]);
		return bounds.bottom;

	}

	public static int getTitleHeight() {
		return (int) (Math.round(thisOutput.getInsets().top / 10.0) * 10);
	}

	public static void main(String[] args) {
		ConsoleOutput output = new ConsoleOutput();
		output.setVisible(true);
		System.out.println(runSmartScreenCheck(output).x);
		System.out.println(runSmartScreenCheck(output).y);
		System.out.println(runSmartScreenCheck(output).width);
		System.out.println(runSmartScreenCheck(output).height);
	}
}
