package dao;

import database.DataBase;
import model.Song;

public class SongDataImpl implements SongDao {
    DataBase db = new DataBase();
    public void insert(Song song) {
        db.addSong(song);
    }
}
