package dao;

import model.Rating;

import java.util.List;

public interface RatingDao {
    void insert(Rating rating);

    void updateRating(Rating rating);

    List<Rating> getRatingList();
}
