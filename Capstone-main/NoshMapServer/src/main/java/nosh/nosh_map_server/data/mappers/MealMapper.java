package nosh.nosh_map_server.data.mappers;

import nosh.nosh_map_server.models.Meal;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MealMapper implements RowMapper<Meal> {
    @Override
    public Meal mapRow(ResultSet rs, int rowNum) throws SQLException {

        Meal meal = new Meal();
        meal.setMealId(rs.getInt("meal_id"));
        meal.setTitle(rs.getString("title"));
        meal.setPrice(rs.getBigDecimal("price"));
        meal.setImageUrl(rs.getString("image_url"));
        meal.setDescription(rs.getString("description"));
        meal.setLastUpdated(null);
        meal.setPublic(rs.getBoolean("is_public"));
        meal.setAppUserId(rs.getInt("app_user_id"));
        meal.setRestaurantId(rs.getInt("restaurant_id"));

        return meal;
    }
}
