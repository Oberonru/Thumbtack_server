package dao;

import model.Rating;

public interface RatingDao {
    void insert(Rating rating);
    void updateRating(Rating rating);
}
