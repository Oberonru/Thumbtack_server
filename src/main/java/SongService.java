import model.Song;

import java.util.ArrayList;
import java.util.List;

public class SongService {
    private List<Song> songList = new ArrayList<Song>();

    public SongService(List<Song> songList) {
        this.songList = songList;
    }
    public SongService() {}

    public Song createSong(String songName, String[] composer, String[] author, String musician, int songDuration) {
        Song song = new Song();
        song.setSongName(songName);
        song.setComposer(composer);
        song.setAuthor(author);
        song.setMusician(musician);
        song.setSongDuration(songDuration);
        songList.add(song);
        return song;
    }
}
