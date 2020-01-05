package dao;

import database.DataBase;
import model.Rating;

public class RatingDaoImpl implements RatingDao {
    private DataBase db = DataBase.getInstance();

    public void insert(Rating rating) {
        db.updateRaiting(rating);
    }

    public void updateRating(Rating rating) {
        db.updateRaiting(rating);
    }
}
