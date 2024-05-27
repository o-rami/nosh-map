package nosh.nosh_map_server.data.mappers;

import nosh.nosh_map_server.models.Restaurant;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RestaurantMapper implements RowMapper<Restaurant> {
    @Override
    public Restaurant mapRow(ResultSet rs, int rowNum) throws SQLException {
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(rs.getInt("restaurant_id"));
        restaurant.setGooglePlaceId(rs.getString("google_place_id"));
        restaurant.setName(rs.getString("restaurant_name"));
        restaurant.setAddress(rs.getString("address"));
        restaurant.setZipCode(rs.getString("zipcode"));
        restaurant.setPhone(rs.getString("phone"));
        restaurant.setWebsite(rs.getString("website"));
        restaurant.setEmail(rs.getString("email"));
        restaurant.setLatitude(Double.parseDouble(rs.getString("latitude")));
        restaurant.setLongitude(Double.parseDouble(rs.getString("longitude")));
        restaurant.setState(rs.getString("state"));
        restaurant.setCity(rs.getString("city"));
        restaurant.setHoursInterval(rs.getString("hours_interval"));
        restaurant.setCuisineType(rs.getString("cuisine_type"));
        return restaurant;
    }
}


