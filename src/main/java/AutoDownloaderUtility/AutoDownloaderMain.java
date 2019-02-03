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
		int index = 1;
		ArrayList<String> searchResults = new ArrayList<String>();
		String songListDirectory = "C:\\Users\\Will\\workspace\\AutoMp3Downloader\\src\\main\\resources\\SongsToDownload.txt";
		HashMap<String, String> songs = getSongList(songListDirectory);

		SeleniumWebDriver.setUp();
		SeleniumWebDriver.goToUrl("https://www.soundcloud.com");

		for (Entry<String, String> entry : songs.entrySet()) {
			currentArtist = entry.getKey();
			currentSong = entry.getValue();

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

			for (String i : searchResults) {
				currentArtistResult = i.split("/n")[0];
				currentSongResult = i.split("/n")[1];
				if (currentArtistResult.equalsIgnoreCase(currentArtist)
						&& currentSongResult.equalsIgnoreCase(currentSong)) {
					System.out.println("got here");
				}
			}
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
