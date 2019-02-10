package AutoDownloaderUtility;

public class Song {

	public Song(String artist, String title) {
		this.artist = artist;
		this.title = title;
	}

	private String artist;
	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

}
