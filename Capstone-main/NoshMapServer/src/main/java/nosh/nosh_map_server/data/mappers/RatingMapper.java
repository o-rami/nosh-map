package nosh.nosh_map_server.data.mappers;

import nosh.nosh_map_server.models.Rating;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RatingMapper implements RowMapper<Rating> {
    @Override
    public Rating mapRow(ResultSet resultSet, int i) throws SQLException {
        Rating rating = new Rating();
        rating.setRatingId(resultSet.getInt("rating_id"));
        rating.setScore(resultSet.getInt("score"));
        rating.setAppUserId(resultSet.getInt("app_user_id"));
        rating.setRestaurantId(resultSet.getInt("restaurant_id"));
        return rating;
    }
}
