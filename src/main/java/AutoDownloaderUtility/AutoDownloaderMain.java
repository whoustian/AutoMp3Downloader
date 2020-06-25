package AutoDownloaderUtility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import Selenium.PageObject;
import Selenium.SeleniumWebDriver;

public class AutoDownloaderMain {

	public static WebDriver driver;
	public static final String SCLOUDDOWNLOADER = "sCloudDownloader";
	public static final String KLICKAUD = "klickAud";

	public static final String downloadWebsite = KLICKAUD;

	public static void main(String[] Args) {
		try {
			DownloadPlaylist();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void DownloadPlaylist() throws IOException, InterruptedException {
		String plLink = getPlaylistLink();
		int plSize = 1;
		WebElement link;

		driver = SeleniumWebDriver.setUp("C:\\SoundCloudDownloader\\dependencies\\chromedriver.exe");
		driver.manage().window().maximize();

		driver.get(plLink);

		while (!ObjectRepo.soundCloud_BottomBorder.isVisible(driver)) {
			JavascriptExecutor js = ((JavascriptExecutor) driver);
			js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
			Thread.sleep(1000);
		}

		for (int count = 1; count < 5000; count++) {
			PageObject songCount = ObjectRepo.getCurrentScPlaylistSong(count);
			if (!songCount.isVisible(driver)) {
				plSize = count - 1;
				break;
			}
		}

		for (int i = 1; i <= plSize; i++) {
			ObjectRepo.soundCloud_FollowButton.waitForVisible(driver, 5);
			PageObject currentObject = ObjectRepo.getCurrentScPlaylistSong(i);
			while (!currentObject.isVisible(driver)) {
				JavascriptExecutor js = ((JavascriptExecutor) driver);
				js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
				Thread.sleep(1000);
			}
			link = driver.findElement(currentObject.getLocator());
			String linkLocation = link.getAttribute("href");
			downloadSong(linkLocation, downloadWebsite);
			driver.get(plLink);
		}

		System.out.println("Playlist downloaded!");

		driver.close();

	}

	public static String getPlaylistLink() throws IOException {
		String plLink = "";
		File playlistFile = new File("C:\\PlaylistDownloader\\PlaylistLink.txt");
		BufferedReader br = new BufferedReader(new FileReader(playlistFile));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			plLink = sb.toString();
		} finally {
			br.close();
		}
		return plLink;
	}

	public static void DownloadMusic(String dlSite) throws IOException, InterruptedException {
		String currentArtist;
		String currentSong;
		String currentArtistResult;
		String currentSongResult;
		String songUrl;
		int index = 1;
		boolean songFound = false;
		ArrayList<String> searchResults = new ArrayList<String>();
		String songListDirectory = getSongListDirectory();
		List<Song> songs = getSongList(songListDirectory);

		try {
			driver = SeleniumWebDriver.setUp("C:\\SoundCloudDownloader\\dependencies\\chromedriver.exe");
			driver.manage().window().maximize();
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (Song entry : songs) {
			searchResults.clear();
			currentArtist = entry.getArtist();
			currentSong = entry.getTitle();

			driver.get("https://www.soundcloud.com");
			searchOnSoundcloud(currentArtist, currentSong);

			searchResults = grabSearchResults();

			index = 1;

			for (String i : searchResults) {
				currentArtistResult = i.split("\n")[0];
				currentSongResult = i.split("\n")[1];
				if (songMatches(currentArtist, currentSong, currentArtistResult, currentSongResult)) {
					ObjectRepo.getSongLink(index).click(driver);
					songFound = true;
					break;
				}
				index++;
			}

			if (!songFound) {
				System.out.println("Unable to find song: " + currentSong);
				continue;
			}

			ObjectRepo.soundCloud_FollowButton.waitForVisible(driver, 5);
			songUrl = driver.getCurrentUrl();

			downloadSong(songUrl, dlSite);
		}

		driver.quit();
	}

	private static ArrayList<String> grabSearchResults() {
		int index = 1;
		ArrayList<String> searchResults = new ArrayList<String>();
		try {
			while (index < 5) {
				searchResults.add(ObjectRepo.getArtistSongResults(index).getText(driver));
				index++;
			}
		} catch (Exception e) {
			System.out.println("All results grabbed.");
		}
		return searchResults;
	}

	private static boolean songMatches(String expectedArtist, String expectedSong, String actualArtist,
			String actualSong) {
		int artistCharMatchCount = 0;
		int songCharMatchCount = 0;
		char[] artistCharArray = actualArtist.toCharArray();
		char[] songCharArray = actualSong.toCharArray();

		for (int i = 0; i < expectedArtist.length(); i++) {
			char c = expectedArtist.charAt(i);
			if (Character.toString(c).equalsIgnoreCase(Character.toString(artistCharArray[i]))) {
				artistCharMatchCount++;
			}
		}

		for (int i = 0; i < expectedSong.length(); i++) {
			char c = expectedSong.charAt(i);
			if (Character.toString(c).equalsIgnoreCase(Character.toString(songCharArray[i]))) {
				songCharMatchCount++;
			}
		}

		int totalArtistLength = artistCharArray.length;
		int totalSongLength = songCharArray.length;

		double acceptableArtistCount = totalArtistLength / 1.4;
		double acceptableSongCount = totalSongLength / 1.4;

		boolean artistMatch = artistCharMatchCount >= acceptableArtistCount;
		boolean songMatch = songCharMatchCount >= acceptableSongCount;
		return artistMatch && songMatch;
	}

	private static void downloadSong(String songUrl, String dlSite) throws InterruptedException {
		if (dlSite.equalsIgnoreCase(SCLOUDDOWNLOADER)) {
			sCloudDownloader(songUrl);
		} else if (dlSite.equalsIgnoreCase(KLICKAUD)) {
			klickAud(songUrl);
		}

	}

	private static void klickAud(String songUrl) throws InterruptedException {
		driver.get("https://www.klickaud.co/");
		ObjectRepo.klickAud_SearchBar.setValue(driver, songUrl.trim());
		ObjectRepo.klickAud_SubmitButton.click(driver);
		ObjectRepo.klickAud_DownloadButton.waitForVisible(driver, 120);
		Thread.sleep(1000);
		ObjectRepo.klickAud_DownloadButton.click(driver);
		Thread.sleep(2000);
	}

	private static void sCloudDownloader(String songUrl) throws InterruptedException {
		driver.get("https://sclouddownloader.net/");
		ObjectRepo.scDlr_SearchBar.waitForVisible(driver, 120);
		ObjectRepo.scDlr_SearchBar.setValue(driver, songUrl.trim());
		ObjectRepo.scDlr_SubmitButton.click(driver);
		Thread.sleep(1000);

		if (ObjectRepo.scDlr_ErrorMsg.isVisible(driver)) {
			int count = 0;
			while (ObjectRepo.scDlr_ErrorMsg.isVisible(driver) && count < 10) {
				ObjectRepo.scDlr_SearchBar.setValue(driver, songUrl.trim());
				ObjectRepo.scDlr_SubmitButton.click(driver);
				count++;
			}
		}

		ObjectRepo.scDlr_DownloadButton.waitForVisible(driver, 120);
		ObjectRepo.scDlr_DownloadButton.click(driver);
		Thread.sleep(3000);
	}

	private static void searchOnSoundcloud(String currentArtist, String currentSong) {
		ObjectRepo.soundCloud_LandingPageSearchBar.waitForVisible(driver, 60);
		ObjectRepo.soundCloud_LandingPageSearchBar.setValue(driver, currentArtist + " " + currentSong);
		ObjectRepo.soundCloud_LandingPageSearchButton.click(driver);
		ObjectRepo.getArtistSongResults(1).waitForVisible(driver, 60);
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
			try {
				while ((currentLine = br.readLine()) != null) {
					artist = currentLine.split("%")[0].trim();
					song = currentLine.split("%")[1].trim();
					Song currentSong = new Song(artist, song);
					songList.add(currentSong);
				}
			} catch (Exception e) {
				System.out.println("Got all songs.");
			}
			for (Song i : songList) {
				System.out.println(i.getTitle());
			}
			br.close();
			return songList;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static String getSongListDirectory() throws IOException {
		BufferedReader br = new BufferedReader(
				new FileReader(new File("C:\\SoundCloudDownloader\\config\\SongListDirectory.txt")));
		String dir = br.readLine();
		br.close();

		if (dir.contains("/")) {
			dir = dir.replaceAll("/", "\\\\");
		}

		if (!dir.endsWith("\\SongList.txt")) {
			dir = dir + "\\SongList.txt";
		}

		return dir;
	}

}
