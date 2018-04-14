package vision;

import java.awt.Dimension;
import java.awt.Toolkit;

public class SmartScreen {
	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	/**
	 * Calculates and returns the x, y, width, and height of the phone screen based on overall resolution
	 * @return Integer array with the calculated data, this can be passed straight to rectangle coords
	 */
	public static int[] runSmartScreenCheck() {
		
		int availableHeight = (int) Math.round(screenSize.getHeight() - 90);
		int availableWidth = (int) Math.round(availableHeight*1080/1920);
		int xCoordinate = (int) Math.round((screenSize.getWidth() - availableWidth)/2);
		int yCoordinate = 40;
		int[] dimensions = {xCoordinate, yCoordinate, availableWidth, availableHeight};
		return dimensions;
	}
	
	public static void main(String[] args) {
		//System.out.println(screenSize.getHeight());
		for(double o:runSmartScreenCheck()) {
			System.out.println(o);
		}
	}
}
