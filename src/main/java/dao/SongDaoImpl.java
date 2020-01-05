package dao;

import database.DataBase;
import model.Song;

import java.util.List;

public class SongDaoImpl implements SongDao {
    private DataBase db = DataBase.getInstance();

    public void insert(Song song) {
        db.addSong(song);
    }

    public String deleteSong(Song song) {
        return db.deleteSong(song);
    }

    public List<Song> getSongList() {
        return db.getSongList();
    }
}
