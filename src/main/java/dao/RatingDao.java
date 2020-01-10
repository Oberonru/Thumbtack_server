package dao;

import model.Rating;

import java.util.List;

public interface RatingDao {
    void insert(Rating rating) throws Exception;

    void deleteRating(Rating rating) throws Exception;

    List<Rating> getRatingList();
}
