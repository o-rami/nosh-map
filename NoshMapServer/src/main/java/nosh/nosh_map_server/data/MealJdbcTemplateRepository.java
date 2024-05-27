package nosh.nosh_map_server.data;

import nosh.nosh_map_server.data.mappers.MealMapper;
import nosh.nosh_map_server.models.Meal;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class MealJdbcTemplateRepository implements MealRepository {

    private final JdbcTemplate jdbcTemplate;

    public MealJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Meal findByMealId(int mealId) {
        final String sql = "SELECT meal_id, title, price, image_url, `description`, last_updated, is_public, app_user_id, "
                + "restaurant_id "
                + "FROM meal "
                + "WHERE meal_id = ?;";

        return jdbcTemplate.query(sql, new MealMapper(), mealId)
                .stream().findFirst().orElse(null);
    }

    @Override
    public List<Meal> findByUserId(int appUserId) {
        final String sql = "SELECT meal_id, title, price, image_url, `description`, last_updated, is_public, app_user_id, "
                + "restaurant_id "
                + "FROM meal "
                + "WHERE app_user_id = ?;";

        return jdbcTemplate.query(sql, new MealMapper(), appUserId);
    }

    @Override
    public List<Meal> findByRestaurantId(int restaurantId) {
        final String sql = "SELECT "
                + "meal_id, "
                + "title, "
                + "price, "
                + "image_url, "
                + "`description`, "
                + "last_updated, "
                + "is_public, "
                + "app_user_id, "
                + "restaurant_id "
                + "FROM meal "
                + "WHERE restaurant_id = ?;";

        return jdbcTemplate.query(sql, new MealMapper(), restaurantId);
    }

    @Override
    public List<Meal> getPublic(int userId) {
        final String sql = "SELECT meal_id, title, price, image_url, `description`, last_updated, is_public, app_user_id, "
                + "restaurant_id "
                + "FROM meal "
                + "WHERE is_public = ? "
                + "AND app_user_id = ?;";
        return jdbcTemplate.query(sql, new MealMapper(), true, userId);
    }

    @Override
    public Meal add(Meal meal) {

        final String sql = "INSERT INTO meal "
                + "(title, price, image_url, `description`, last_updated, is_public, app_user_id, restaurant_id) "
                + "VALUES (?,?,?,?,?,?,?,?);";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, meal.getTitle());
            ps.setBigDecimal(2, meal.getPrice());
            ps.setString(3, meal.getImageUrl());
            ps.setString(4, meal.getDescription());
            ps.setTimestamp(5, null);
            ps.setBoolean(6, meal.isPublic());
            ps.setInt(7, meal.getAppUserId());
            ps.setInt(8, meal.getRestaurantId());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        meal.setMealId(keyHolder.getKey().intValue());

        return meal;
    }

    @Override
    public boolean update(Meal meal) {

        final String sql = "UPDATE meal SET "
                + "title = ?, "
                + "price = ?, "
                + "image_url = ?, "
                + "`description` = ?, "
                + "last_updated = ?, "
                + "app_user_id = ?, "
                + "restaurant_id = ? "
                + "WHERE meal_id = ?;";

        return jdbcTemplate.update(sql,
                meal.getTitle(),
                meal.getPrice(),
                meal.getImageUrl(),
                meal.getDescription(),
                meal.getLastUpdated(),
                meal.getAppUserId(),
                meal.getRestaurantId(),
                meal.getMealId()) > 0;
    }

    @Override
    public boolean deleteById(int mealId) {
        final String sql = "DELETE FROM meal WHERE meal_id = ?";
        return jdbcTemplate.update(sql, mealId) > 0;
    }

}