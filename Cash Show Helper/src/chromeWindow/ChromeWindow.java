package chromeWindow;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

import vision.ScreenUtils;

public class ChromeWindow implements Runnable {
	private WebDriver driver;
	private String question;

	public ChromeWindow(WebDriver driver, String question) {
		this.driver = driver;
		this.question = question;
	}

	@Override
	public void run() {
		org.openqa.selenium.Point windowPosition = new org.openqa.selenium.Point(0, 0);
		driver.manage().window().setPosition(windowPosition);

		driver.manage().window().setSize(
				new Dimension((int) ((int) ScreenUtils.getScreenWidth() / 3.5), (int) ScreenUtils.getScreenHeight()));

		driver.get((new StringBuilder("https://www.google.com/").append("search?q=").append(question)).toString());

	}

}
