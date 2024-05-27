package nosh.nosh_map_server.data.mappers;

import nosh.nosh_map_server.models.AppUserMeal;
import nosh.nosh_map_server.models.AppUserRestaurant;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AppUserMealMapper implements RowMapper<AppUserMeal> {
    @Override
    public AppUserMeal mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new AppUserMeal(
                rs.getInt("app_user_id"),
                rs.getInt("meal_id"),
                rs.getString("identifier"));
    }
}
