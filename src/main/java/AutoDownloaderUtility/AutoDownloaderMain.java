package AutoDownloaderUtility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import Selenium.SeleniumWebDriver;

public class AutoDownloaderMain {

	public static void main(String[] Args) {
		// OpenTabs();
		DownloadMusic();
	}

	public static void OpenTabs() {
		String currentArtist;
		String currentSong;
		String songListDirectory = "C:\\Users\\Will\\Desktop\\Producing\\Downloader Tool\\SongsToDownload.txt";
		List<Song> songs = getSongList(songListDirectory);
		int index = 0;
		SeleniumWebDriver.setUp();
		ArrayList<String> tabs = new ArrayList<String>(SeleniumWebDriver.getDriver().getWindowHandles());
		for (Song entry : songs) {
			tabs = new ArrayList<String>(SeleniumWebDriver.getDriver().getWindowHandles());
			currentArtist = entry.getArtist();
			currentSong = entry.getTitle();
			SeleniumWebDriver.goToUrl("https://www.soundcloud.com");
			searchOnSoundcloud(currentArtist, currentSong);
			SeleniumWebDriver.getDriver().findElement(By.cssSelector("head")).sendKeys(Keys.chord(Keys.CONTROL, "t"));
			SeleniumWebDriver.getDriver().switchTo().window(tabs.get(index));
			index++;
		}
	}

	public static void DownloadMusic() {
		String currentArtist;
		String currentSong;
		String currentArtistResult;
		String currentSongResult;
		String songUrl;
		int index = 1;
		boolean songFound = false;
		ArrayList<String> searchResults = new ArrayList<String>();
		String songListDirectory = "C:\\Users\\Will\\Desktop\\Producing\\Downloader Tool\\SongsToDownload.txt";
		List<Song> songs = getSongList(songListDirectory);

		SeleniumWebDriver.setUp();

		for (Song entry : songs) {
			searchResults.clear();
			currentArtist = entry.getArtist();
			currentSong = entry.getTitle();

			SeleniumWebDriver.goToUrl("https://www.soundcloud.com");
			searchOnSoundcloud(currentArtist, currentSong);

			searchResults = grabSearchResults();

			index = 1;

			for (String i : searchResults) {
				currentArtistResult = i.split("\n")[0];
				currentSongResult = i.split("\n")[1];
				if (checkSongMatch(currentArtist, currentSong, currentArtistResult, currentSongResult)) {
					ObjectRepo.getSongLink(index).click();
					;
					songFound = true;
					break;
				}
				index++;
			}

			if (!songFound) {
				System.out.println("Unable to find song: " + currentSong);
				continue;
			}

			ObjectRepo.soundCloud_FollowButton.waitForVisible(5);
			songUrl = SeleniumWebDriver.getCurrentUrl();

			SeleniumWebDriver.goToUrl("https://www.klickaud.com");
			downloadSong(songUrl);
		}

		SeleniumWebDriver.closeBrowser();
	}

	private static ArrayList<String> grabSearchResults() {
		int index = 1;
		ArrayList<String> searchResults = new ArrayList<String>();
		try {
			while (index < 5) {
				searchResults.add(ObjectRepo.getArtistSongResults(index).getText());
				index++;
			}
		} catch (Exception e) {
			System.out.println("All results grabbed.");
		}
		return searchResults;
	}

	private static boolean checkSongMatch(String expectedArtist, String expectedSong, String actualArtist,
			String actualSong) {
		boolean artistMatch = actualArtist.toLowerCase().substring(0, expectedArtist.length())
				.contains(expectedArtist.toLowerCase());
		boolean songMatch = actualSong.toLowerCase().substring(0, expectedSong.length())
				.contains(expectedSong.toLowerCase());
		return artistMatch && songMatch;
	}

	private static void downloadSong(String songUrl) {
		ObjectRepo.klickAud_SearchBar.waitForVisible(5);
		ObjectRepo.klickAud_SearchBar.setValue(songUrl);
		ObjectRepo.klickAud_SubmitButton.click();
		ObjectRepo.klickAud_DownloadButton.waitForVisible(5);
		ObjectRepo.klickAud_DownloadButton.click();
		ObjectRepo.klickAud_DownloadComplete.waitForVisible(5);
	}

	private static void searchOnSoundcloud(String currentArtist, String currentSong) {
		ObjectRepo.soundCloud_LandingPageSearchBar.waitForVisible(5);
		ObjectRepo.soundCloud_LandingPageSearchBar.setValue(currentArtist + " " + currentSong);
		ObjectRepo.soundCloud_LandingPageSearchButton.click();
		ObjectRepo.getArtistSongResults(1).waitForVisible(5);
	}

	public static void wait(int timeInSeconds) {
		try {
			Thread.sleep(timeInSeconds * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static List<Song> getSongList(String songListDirectory) {
		try {
			String currentLine;
			String artist;
			String song;
			List<Song> songList = new ArrayList<Song>();
			File songListFile = new File(songListDirectory);
			BufferedReader br = new BufferedReader(new FileReader(songListFile));
			while ((currentLine = br.readLine()) != null) {
				artist = currentLine.split(" - ")[0];
				song = currentLine.split(" - ")[1];
				Song currentSong = new Song(artist, song);
				songList.add(currentSong);
			}
			br.close();
			return songList;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

}
