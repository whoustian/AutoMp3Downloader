package AutoDownloaderUtility;

import org.openqa.selenium.By;

import Selenium.PageObject;

public class ObjectRepo {

	public static final PageObject soundCloud_LandingPageSearchBar = new PageObject (By.xpath("(//*[@aria-label='Search'])[2]"), "");
	public static final PageObject soundCloud_LandingPageSearchButton = new PageObject (By.xpath("(//*[@type='submit'])[2]"), "");
	public static final PageObject soundCloud_FollowButton = new PageObject (By.xpath("//*[@type='button' and @title='Follow']"), "");
	public static final PageObject klickAud_SearchBar = new PageObject (By.xpath("//*[@title='Enter The SoundCloud URL']"), "");
	public static final PageObject klickAud_SubmitButton = new PageObject (By.xpath("//*[@type='submit']"), "");
	public static final PageObject klickAud_DownloadButton = new PageObject (By.xpath("//*[@id='dlMP3']"), "");
	public static final PageObject klickAud_DownloadComplete = new PageObject (By.xpath("//*[contains(text(), 'DOWNLOAD COMPLETE')]"), "");
	
	public static final PageObject scDlr_SearchBar = new PageObject(By.xpath("//*[@title='Please Enter The SoundCloud URL']"), "");
	public static final PageObject scDlr_SubmitButton = new PageObject(By.xpath("//*[@id='demo-show-toast']"), "");
	public static final PageObject scDlr_DownloadButton = new PageObject(By.xpath("//*[contains(text(), 'Download Song.mp3')]"), "");
	public static final PageObject scDlr_DownloadComplete = new PageObject(By.xpath("//*[text()='DOWNLOAD COMPLETE... CTRL+J']"), "");

	public static PageObject getArtistSongResults(int index) {
		return new PageObject(By.xpath("(//*[@class='soundTitle__usernameTitleContainer'])" + "[" + String.valueOf(index) + "]"), "");
	}

	public static PageObject getSongLink(int index) {
		return new PageObject(By.xpath("(//*[@class='soundTitle__title sc-link-dark'])" + "[" + String.valueOf(index) + "]"), "");
	}

}
