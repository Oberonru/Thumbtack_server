package response;

import model.Song;

import java.util.ArrayList;
import java.util.List;

public class FindSongsListDtoResponse {
    private List<Song> songList = new ArrayList<Song>();

    public List<Song> getSongList() {
        return songList;
    }
}
