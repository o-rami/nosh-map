package nosh.nosh_map_server.data;

import nosh.nosh_map_server.data.mappers.RestaurantMapper;
import nosh.nosh_map_server.models.Restaurant;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class RestaurantJdbcTemplateRepository implements RestaurantRepository {

    private final JdbcTemplate jdbcTemplate;

    public RestaurantJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Restaurant> findAll() {
        final String sql = "SELECT "
                + "restaurant_id, "
                + "google_place_id, "
                + "restaurant_name, "
                + "address, "
                + "zipcode, "
                + "phone, "
                + "website, "
                + "email, "
                + "latitude, "
                + "longitude, "
                + "city, "
                + "state, "
                + "hours_interval, "
                + "cuisine_type "
                + "FROM restaurant;";
        return jdbcTemplate.query(sql, new RestaurantMapper());
    }

    @Override
    public Restaurant findByRestaurantId(int restaurantId) {
        final String sql = "SELECT "
                + "restaurant_id, "
                + "google_place_id, "
                + "restaurant_name, "
                + "address, "
                + "zipcode, "
                + "phone, "
                + "website, "
                + "email, "
                + "latitude, "
                + "longitude, "
                + "state, "
                + "city, "
                + "hours_interval, "
                + "cuisine_type "
                + "FROM restaurant "
                + "WHERE restaurant_id = ?;";
        return jdbcTemplate.query(sql, new RestaurantMapper(), restaurantId).stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Restaurant> findByUserId(int userId) {
        final String sql = "SELECT "
                + "r.restaurant_id, "
                + "r.google_place_id, "
                + "r.restaurant_name, "
                + "r.address, "
                + "r.zipcode, "
                + "r.phone, "
                + "r.website, "
                + "r.email, "
                + "r.latitude, "
                + "r.longitude, "
                + "r.state, "
                + "r.city, "
                + "r.hours_interval, "
                + "r.cuisine_type "
                + "FROM restaurant r "
                + "inner join app_user_restaurant a on a.restaurant_id = r.restaurant_id "
                + "WHERE app_user_id = ?;";
        return jdbcTemplate.query(sql, new RestaurantMapper(), userId);
    }

    public List<Restaurant> getFavRestaurants(int appUserId) {
        final String sql = "select " +
                "re.google_place_id, " +
                "re.restaurant_name, " +
                "re.address, " +
                "re.zipcode, " +
                "re.phone, " +
                "re.website, " +
                "re.email, " +
                "re.latitude, " +
                "re.longitude, " +
                "re.state, " +
                "re.city, " +
                "re.hours_interval, " +
                "re.cuisine_type, " +
                "r.score, " +
                "a.app_user_id, " +
                "re.restaurant_id " +
                "from rating r " +
                "inner join app_user a on a.app_user_id = r.app_user_id " +
                "inner join restaurant re on re.restaurant_id = r.restaurant_id " +
                "where r.score > 3.9 and a.app_user_id = ? " +
                "order by score desc;";
        return jdbcTemplate.query(sql, new RestaurantMapper(), appUserId);
    }

    @Override
    public Restaurant add(Restaurant restaurant) {
        final String sql = "INSERT INTO restaurant "
                + "(restaurant_name, google_place_id, address, zipcode, phone, website, email, "
                + "latitude, longitude, state, city, hours_interval, cuisine_type) "
                + "values (?,?,?,?,?,?,?,?,?,?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, restaurant.getName());
            ps.setString(2, restaurant.getGooglePlaceId());
            ps.setString(3, restaurant.getAddress());
            ps.setString(4, restaurant.getZipCode());
            ps.setString(5, restaurant.getPhone());
            ps.setString(6, restaurant.getWebsite());
            ps.setString(7, restaurant.getEmail());
            ps.setString(8, Double.toString(restaurant.getLatitude()));
            ps.setString(9, Double.toString(restaurant.getLongitude()));
            ps.setString(10, restaurant.getState());
            ps.setString(11, restaurant.getCity());
            ps.setString(12, restaurant.getHoursInterval());
            ps.setString(13, restaurant.getCuisineType());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        restaurant.setRestaurantId(keyHolder.getKey().intValue());

        return restaurant;
    }

    @Override
    public boolean update(Restaurant restaurant) {
        final String sql = "UPDATE restaurant SET "
                + "google_place_id = ?, "
                + "restaurant_name = ?, "
                + "address = ?, "
                + "zipcode = ?, "
                + "phone = ?, "
                + "website = ?, "
                + "email = ?, "
                + "latitude = ?, "
                + "longitude = ?, "
                + "state = ?, "
                + "city = ?, "
                + "hours_interval = ?, "
                + "cuisine_type = ? "
                + "WHERE restaurant_id = ?";

        return jdbcTemplate.update(sql,
                restaurant.getGooglePlaceId(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getZipCode(),
                restaurant.getPhone(),
                restaurant.getWebsite(), restaurant.getEmail(),
                Double.toString(restaurant.getLatitude()),
                Double.toString(restaurant.getLongitude()),
                restaurant.getState(),
                restaurant.getCity(),
                restaurant.getHoursInterval(),
                restaurant.getCuisineType(),
                restaurant.getRestaurantId()) > 0;
    }

    @Override
    public boolean deleteById(int restaurantId) {
        final String sqlChildMeal = "delete from meal where restaurant_id = ?";
        final String sqlChildComment = "delete from user_comment where restaurant_id = ?;";
        final String sqlChildRating = "delete from rating where restaurant_id = ?;";
        final String sqlChildUserRestaurant = "delete from app_user_restaurant where restaurant_id = ?;";

        final String sqlParent = "delete from restaurant where restaurant_id = ?;";

        jdbcTemplate.update(sqlChildMeal, restaurantId);
        jdbcTemplate.update(sqlChildComment, restaurantId);
        jdbcTemplate.update(sqlChildRating, restaurantId);
        jdbcTemplate.update(sqlChildUserRestaurant, restaurantId);

        return jdbcTemplate.update(sqlParent, restaurantId) > 0;
    }
}
