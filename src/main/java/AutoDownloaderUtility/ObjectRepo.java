package AutoDownloaderUtility;

import org.openqa.selenium.By;

public class ObjectRepo {

	public static final By soundCloud_LandingPageSearchBar = By.xpath("(//*[@aria-label='Search'])[2]");
	public static final By soundCloud_LandingPageSearchButton = By.xpath("(//*[@type='submit'])[2]");

	public static By getArtistSongResults(int index) {
		return By.xpath("(//*[@class='soundTitle__usernameTitleContainer'])" + "[" + String.valueOf(index) + "]");
	}

}
