package response;

import model.Song;

import java.util.ArrayList;
import java.util.List;

public class FindSongByComposersDtoResponse {
    private List<Song> songList = new ArrayList<Song>();

    public FindSongByComposersDtoResponse() {
    }

    public List<Song> getSongList() {
        return songList;
    }
}

