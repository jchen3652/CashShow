package vision;

import java.awt.Color;
import java.awt.Robot;

import main.Config;

public class ScreenListenerAlgorithms {
	int red;
	int green;
	int blue;

	public void refreshPixelListener(int x, int y, Robot robot) {
		Color pixelColor = robot.getPixelColor(x, y);
		red = pixelColor.getRed();
		green = pixelColor.getGreen();
		blue = pixelColor.getBlue();
	}

	/**
	 * Used to tell whether is
	 * 
	 * @param rtol
	 *            Red tolerance array
	 * @param gtol
	 *            Green tolerance array
	 * @param btol
	 *            Blue Tolerance array
	 * @return
	 */
	public boolean isColor(int[] rtol, int[] gtol, int[] btol) {
		return ((red >= rtol[0]) && (red <= rtol[1])) && ((green >= gtol[0]) && (green <= gtol[1]))
				&& ((blue >= btol[0]) && (blue <= btol[1]));
	}

	public boolean isGray() {
		return isColor(Config.grayRTOL, Config.grayGTOL, Config.grayBTOL);
	}

	public boolean isGreen() {
		return isColor(Config.greenRTOL, Config.greenGTOL, Config.greenBTOL);
	}
	
	public boolean isWhite() {
		return isColor(Config.whiteRTOL, Config.whiteGTOL, Config.whiteBTOL);
	}
	
	public int getR() {
		return red;
	}
	
	public int getB() {
		return blue;
	}
	
	public int getG() {
		return green;
	}
}