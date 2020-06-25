package Selenium;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class SeleniumWebDriver {

	public static ChromeDriver setUp(String driverLocation) throws IOException {
		System.setProperty("webdriver.chrome.driver", driverLocation);
		String downloadFilePath = getDlDirectory();
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("download.default_directory", downloadFilePath);
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("prefs", chromePrefs);
		ChromeDriver driver = new ChromeDriver(options);
		return driver;
	}

	public static String getDlDirectory() throws IOException {
		BufferedReader br = new BufferedReader(
				new FileReader(new File("C:\\PlaylistDownloader\\config\\DownloadDirectory.txt")));
		String dir = br.readLine();
		br.close();

		if (dir.contains("/")) {
			dir = dir.replaceAll("/", "\\\\");
		}

		if (!dir.endsWith("\\")) {
			dir = dir + "\\";
		}

		return dir;
	}

}
