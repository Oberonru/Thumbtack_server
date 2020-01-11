package dao;

import model.Song;

import java.util.List;

public interface SongDao {
    void insert(Song song);

    String deleteSong(Song song) throws Exception;

    Song findSongById(int songId);

    List<Song> getSongList();
}
