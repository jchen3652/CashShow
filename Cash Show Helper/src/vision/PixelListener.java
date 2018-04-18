package vision;

import java.awt.Color;
import java.awt.Robot;

import main.Config;

/**
 * Monitors a specified pixel location and reports information about color
 * 
 * @author James
 *
 */
public class PixelListener {
	int red;
	int green;
	int blue;

	Robot bot;
	int xCoordinate;
	int yCoordinate;

	/**
	 * 
	 * @param x
	 *            X location
	 * @param y
	 *            Y location
	 * @param robot
	 *            Robot object
	 */
	public PixelListener(int x, int y, Robot robot) {
		xCoordinate = x;
		yCoordinate = y;
		bot = robot;
	}

	public void refreshPixelListener() {
		Color pixelColor = bot.getPixelColor(xCoordinate, yCoordinate);
		red = pixelColor.getRed();
		green = pixelColor.getGreen();
		blue = pixelColor.getBlue();
	}

	/**
	 * Used to tell whether if it is as color
	 * 
	 * @param rtol
	 *            Red tolerance range array
	 * @param gtol
	 *            Green tolerance range array
	 * @param btol
	 *            Blue Tolerance range array
	 * @return Whether the pixel color is within the specified tolerance range
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

	public int getG() {
		return green;
	}

	public int getB() {
		return blue;
	}

	public void printLocation() {
		System.out.println((new StringBuilder("x: ").append(xCoordinate).append(" y: ").append(yCoordinate)));
	}

	public void printRGB() {
		System.out.println((new StringBuilder("R: ")).append(red));
		System.out.println((new StringBuilder("G: ")).append(green));
		System.out.println((new StringBuilder("B: ")).append(blue));
	}
}