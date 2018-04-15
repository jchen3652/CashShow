package vision;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Toolkit;

import consoleOutput.ConsoleOutput;
import main.Main;

public class SmartScreen {
	static ConsoleOutput thisOutput;
	public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	/**
	 * Calculates and returns the x, y, width, and height of the phone screen
	 * based on overall resolution
	 * 
	 * @return Integer array with the calculated data, this can be passed
	 *         straight to rectangle coords
	 */

	public static int screenHeight = (int) screenSize.getHeight();
	public static int screenWidth = (int) screenSize.getWidth();

	public static int[] runSmartScreenCheck(ConsoleOutput output) {
		thisOutput = output;
		int availableHeight = (int) Math.round(screenHeight - 90 * screenHeight / 1080);
		int availableWidth = (int) Math.round(availableHeight * screenHeight / screenWidth);
		int xCoordinate = (int) Math.round((screenWidth - availableWidth) / 2);
		int yCoordinate = getTitleHeight();
		int[] dimensions = {xCoordinate, yCoordinate, availableWidth, availableHeight};
		return dimensions;
	}
	
	public static int getTaskbarSize() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice sd = ge.getDefaultScreenDevice();
		GraphicsConfiguration[] gc = sd.getConfigurations();		
		Insets bounds = Toolkit.getDefaultToolkit().getScreenInsets(gc[0]);
		return bounds.bottom;
		
	}
	
	public static int getTitleHeight() {
		return (int) (Math.round(thisOutput.getInsets().top/10.0)*10);
	}
	
	public static void main(String[] args) {
		ConsoleOutput output = new ConsoleOutput();
		output.setVisible(true);
		
	}
}
