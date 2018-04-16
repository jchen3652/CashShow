package vision;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Toolkit;

public class ScreenUtils {
	public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static double getScreenHeight() {
		return screenSize.getHeight();
	}
	
	public static double getScreenWidth() {
		return screenSize.getWidth();
	}
	
	public static double getTaskbarHeight() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice sd = ge.getDefaultScreenDevice();
		GraphicsConfiguration[] gc = sd.getConfigurations();
		Insets bounds = Toolkit.getDefaultToolkit().getScreenInsets(gc[0]);
		return bounds.bottom;
	}
}
