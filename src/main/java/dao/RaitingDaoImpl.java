package dao;

import database.DataBase;
import model.Raiting;

public class RaitingDaoImpl implements RaitingDao {
    private DataBase db = DataBase.getInstance();
    public void insert(Raiting raiting) {
        db.addRaiting(raiting);
    }
}
