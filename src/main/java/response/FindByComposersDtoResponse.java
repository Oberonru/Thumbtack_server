package response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import model.Song;

import java.util.ArrayList;
import java.util.List;

public class FindByComposersDtoResponse {
    private List<Song> songs = new ArrayList<Song>();

    public FindByComposersDtoResponse() {
    }

    public List<Song> getSongs() {
        return songs;
    }
}

