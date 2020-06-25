package AutoDownloaderUtility;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class NormalChrome {

	public static String chromedriverPath = "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe";

	// my default profile folder
	// public static String chromeProfilePath =
	// "C:\\Users\\pburgr\\AppData\\Local\\Google\\Chrome\\User Data";

	public static WebDriver driver;

	public static WebDriver getNormalChomeDriver() {
		System.setProperty("webdriver.chrome.driver", chromedriverPath);
//		ChromeOptions options = new ChromeOptions();

		// loading Chrome with my existing profile instead of a temporary
		// profile
		// options.addArguments("user-data-dir=" + chromeProfilePath);

		driver = new ChromeDriver();
		driver.manage().window().maximize();
		return driver;
	}

	public static void shutdownChrome() {
		driver.close();
		driver.quit();
	}
}
