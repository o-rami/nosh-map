package nosh.nosh_map_server.data;

import nosh.nosh_map_server.models.Rating;

public interface RatingRepository {

    Rating findById(int ratingId);
    Rating findByRestaurantUserId(int restaurantId, int appUserId);

    Rating add(Rating rating);

    boolean update(Rating rating);

    boolean deleteById(int rating_id);
}
