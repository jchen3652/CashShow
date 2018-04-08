package vision;

import java.awt.Color;
import java.awt.Robot;

import main.Config;
import main.Main;

public class ScreenListener {
	int red;
	int green; 
	int blue;
	
	public void refreshListener(Robot robot) {
		Color pixelColor = robot.getPixelColor(Main.pixelLocationX, Main.pixelLocationY);
		red = pixelColor.getRed();
		green = pixelColor.getGreen();
		blue = pixelColor.getBlue();
	}
	
	public boolean isGreen() {
		return ((red > Config.greenRToleranceRange[0]) && (red < Config.greenRToleranceRange[1]))
				&& ((green > Config.greenGToleranceRange[0]) && (green < Config.greenGToleranceRange[1]))
				&& ((blue > Config.greenBToleranceRange[0]) && (blue < Config.greenBToleranceRange[1]));
	}
}
