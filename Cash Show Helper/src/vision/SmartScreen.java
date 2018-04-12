package vision;

import java.awt.Dimension;
import java.awt.Toolkit;

public class SmartScreen {
	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
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
