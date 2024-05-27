package nosh.nosh_map_server.data.mappers;

import nosh.nosh_map_server.models.AppUser;
import nosh.nosh_map_server.models.AppUserRestaurant;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AppUserRestaurantMapper implements RowMapper<AppUserRestaurant> {

    @Override
    public AppUserRestaurant mapRow(ResultSet rs, int i) throws SQLException {
        return new AppUserRestaurant(
                rs.getInt("app_user_id"),
                rs.getInt("restaurant_id"),
                rs.getString("identifier"));
    }
}
