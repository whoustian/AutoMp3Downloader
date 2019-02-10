package AutoDownloaderUtility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import Selenium.SeleniumUtil;
import Selenium.SeleniumWebDriver;

public class AutoDownloaderMain {

	public static void main(String[] Args) {
		String currentArtist;
		String currentSong;
		String currentArtistResult;
		String currentSongResult;
		String songUrl;
		int index = 1;
		ArrayList<String> searchResults = new ArrayList<String>();
		String songListDirectory = "C:\\Users\\Will\\workspace\\AutoMp3Downloader\\src\\main\\resources\\SongsToDownload.txt";
		HashMap<String, String> songs = getSongList(songListDirectory);

		SeleniumWebDriver.setUp();

		for (Entry<String, String> entry : songs.entrySet()) {
			searchResults.clear();
			currentArtist = entry.getKey();
			currentSong = entry.getValue();

			SeleniumWebDriver.goToUrl("https://www.soundcloud.com");
			searchOnSoundcloud(currentArtist, currentSong);

			try {
				while (index < 10) {
					searchResults.add(SeleniumUtil.getText(ObjectRepo.getArtistSongResults(index)));
					index++;
				}
			} catch (Exception e) {
				System.out.println("All results grabbed.");
			}

			index = 1;

			for (String i : searchResults) {
				currentArtistResult = i.split("\n")[0];
				currentSongResult = i.split("\n")[1];
				if (currentArtistResult.equalsIgnoreCase(currentArtist)
						&& currentSongResult.equalsIgnoreCase(currentSong)) {
					SeleniumUtil.click(ObjectRepo.getSongLink(index));
					break;
				}
				index++;
			}

			SeleniumUtil.waitForElementVisible(ObjectRepo.soundCloud_FollowButton, 5);
			songUrl = SeleniumWebDriver.getCurrentUrl();

			SeleniumWebDriver.goToUrl("https://www.klickaud.com");
			downloadSong(songUrl);
		}

		SeleniumWebDriver.closeBrowser();
	}

	private static void downloadSong(String songUrl) {
		SeleniumUtil.waitForElementVisible(ObjectRepo.klickAud_SearchBar, 5);
		SeleniumUtil.type(ObjectRepo.klickAud_SearchBar, songUrl);
		SeleniumUtil.click(ObjectRepo.klickAud_SubmitButton);
		SeleniumUtil.waitForElementVisible(ObjectRepo.klickAud_DownloadButton, 5);
		SeleniumUtil.click(ObjectRepo.klickAud_DownloadButton);
		SeleniumUtil.waitForElementVisible(ObjectRepo.klickAud_DownloadComplete, 5);
	}

	private static void searchOnSoundcloud(String currentArtist, String currentSong) {
		SeleniumUtil.waitForElementVisible(ObjectRepo.soundCloud_LandingPageSearchBar, 5);
		SeleniumUtil.type(ObjectRepo.soundCloud_LandingPageSearchBar, currentArtist + " " + currentSong);
		SeleniumUtil.click(ObjectRepo.soundCloud_LandingPageSearchButton);
		SeleniumUtil.waitForElementVisible(ObjectRepo.getArtistSongResults(1), 5);
	}

	public static void wait(int timeInSeconds) {
		try {
			Thread.sleep(timeInSeconds * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static HashMap<String, String> getSongList(String songListDirectory) {
		try {
			String currentLine;
			String artist;
			String song;
			HashMap<String, String> songList = new HashMap<String, String>();
			File songListFile = new File(songListDirectory);
			BufferedReader br = new BufferedReader(new FileReader(songListFile));
			while ((currentLine = br.readLine()) != null) {
				artist = currentLine.split(" - ")[0];
				song = currentLine.split(" - ")[1];
				songList.put(artist, song);
			}
			br.close();
			return songList;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
