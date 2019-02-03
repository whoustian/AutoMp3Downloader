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
			currentArtist = entry.getKey();
			currentSong = entry.getValue();

			SeleniumWebDriver.goToUrl("https://www.soundcloud.com");
			SeleniumUtil.type(ObjectRepo.soundCloud_LandingPageSearchBar, currentArtist + " " + currentSong);
			wait(1);
			SeleniumUtil.click(ObjectRepo.soundCloud_LandingPageSearchButton);
			wait(1);

			try {
				while (index < 20) {
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

			songUrl = SeleniumWebDriver.getCurrentUrl();

			SeleniumWebDriver.goToUrl("https://www.klickaud.com");
			wait(1);
			SeleniumUtil.type(ObjectRepo.klickAud_SearchBar, songUrl);
			wait(1);
			SeleniumUtil.click(ObjectRepo.klickAud_SubmitButton);
			wait(5);
			SeleniumUtil.click(ObjectRepo.klickAud_DownloadButton);
			wait(3);
		}
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
