package nosh.nosh_map_server.data;

import nosh.nosh_map_server.data.mappers.RatingMapper;
import nosh.nosh_map_server.models.Rating;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class RatingJdbcTemplateRepository implements RatingRepository {

    private final JdbcTemplate jdbcTemplate;

    public RatingJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Rating findById(int ratingId) {
        final String sql = "select r.rating_id, r.score, r.`description`, a.app_user_id, re.restaurant_id " +
                "from rating r " +
                "inner join app_user a on a.app_user_id = r.app_user_id " +
                "inner join restaurant re on re.restaurant_id = r.restaurant_id " +
                "where r.rating_id = ?;";
        return jdbcTemplate.query(sql, new RatingMapper(), ratingId).stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public Rating findByRestaurantUserId(int restaurantId, int appUserId) {
        final String sql = "select r.rating_id, r.score, r.`description`, a.app_user_id, re.restaurant_id " +
                "from rating r " +
                "inner join app_user a on a.app_user_id = r.app_user_id " +
                "inner join restaurant re on re.restaurant_id = r.restaurant_id " +
                "where re.restaurant_id = ? and a.app_user_id = ?;";
        return jdbcTemplate.query(sql, new RatingMapper(), restaurantId, appUserId).stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public Rating add(Rating rating) {
        final String sql = "insert into rating (score, `description`, app_user_id, restaurant_id) values (?,?,?,?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, rating.getScore());
            ps.setString(2, rating.getDescription());
            ps.setInt(3, rating.getAppUserId());
            ps.setInt(4, rating.getRestaurantId());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }
        rating.setRatingId(keyHolder.getKey().intValue());
        return rating;
    }

    @Override
    public boolean update(Rating rating) {
        final String sql = "update rating set "
                + "score = ?, "
                + "`description` = ? "
                + "where rating_id = ?";
        return jdbcTemplate.update(sql, rating.getScore(), rating.getDescription(), rating.getRatingId()) > 0;
    }

    @Override
    public boolean deleteById(int rating_id) {
        return jdbcTemplate.update("delete from rating where rating_id = ?", rating_id) > 0;
    }
}