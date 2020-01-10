package dao;

import database.DataBase;
import model.Rating;

import java.util.List;

public class RatingDaoImpl implements RatingDao {
    private DataBase db = DataBase.getInstance();

    public void insert(Rating rating) throws Exception{
        db.updateRaiting(rating);
    }

    public void updateRating(Rating rating) throws Exception {
        db.updateRaiting(rating);
    }

    public List<Rating> getRatingList() {
        return db.getRatingList();
    }
}
