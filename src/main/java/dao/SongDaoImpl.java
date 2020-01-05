package dao;

import database.DataBase;
import model.Song;

public class SongDaoImpl implements SongDao {
    private DataBase db = DataBase.getInstance();

    public void insert(Song song) {
        db.addSong(song);
    }

    public String deleteSong(Song song) {
       return db.deleteSong(song);
    }
}
