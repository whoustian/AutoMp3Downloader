package AutoDownloaderUtility;

import org.openqa.selenium.By;

public class ObjectRepo {

	public static final By soundCloud_LandingPageSearchBar = By.xpath("(//*[@aria-label='Search'])[2]");
	public static final By soundCloud_LandingPageSearchButton = By.xpath("(//*[@type='submit'])[2]");
	public static final By klickAud_SearchBar = By.xpath("//*[@title='Enter The SoundCloud URL']");
	public static final By klickAud_SubmitButton = By.xpath("//*[@type='submit']");
	public static final By klickAud_DownloadButton = By.xpath("//*[@id='dlMP3']");

	public static By getArtistSongResults(int index) {
		return By.xpath("(//*[@class='soundTitle__usernameTitleContainer'])" + "[" + String.valueOf(index) + "]");
	}

	public static By getSongLink(int index) {
		return By.xpath("(//*[@class='soundTitle__title sc-link-dark'])" + "[" + String.valueOf(index) + "]");
	}

}
