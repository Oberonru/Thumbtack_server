package dao;

import model.Song;

public interface SongDao {
    void insert(Song song);
    String deleteSong(Song song);
}
