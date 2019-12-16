package dao;

import database.DataBase;
import model.Song;

public class SongDataImpl implements SongDao {
    DataBase db = DataBase.getInstance();
    public void insert(Song song) {
        db.addSong(song);
    }
}
