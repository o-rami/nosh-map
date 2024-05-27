package nosh.nosh_map_server.data;

import nosh.nosh_map_server.data.mappers.AppUserRestaurantMapper;
import nosh.nosh_map_server.models.AppUserRestaurant;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class AppUserRestaurantJdbcTemplateRepository implements AppUserRestaurantRepository {

    private final JdbcTemplate jdbcTemplate;

    public AppUserRestaurantJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public AppUserRestaurant addToBridge(AppUserRestaurant aur) {
        final String sql = "insert into app_user_restaurant (app_user_id, restaurant_id, identifier) values (?, ?, ?);";

        System.out.println("aur in repo add " + aur);

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, aur.getAppUserId());
            ps.setInt(2, aur.getRestaurantId());
            ps.setString(3, aur.getIdentifier());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }
        return aur;
    }

    @Override
    public List<AppUserRestaurant> findByUserId(int appUserId) {
        final String sql =
                "SELECT " +
                        "a.app_user_id, " +
                        "a.restaurant_id, " +
                        "a.identifier " +
                        "FROM app_user_restaurant a " +
                        "WHERE a.app_user_id = ?;";
        return jdbcTemplate.query(sql, new AppUserRestaurantMapper(), appUserId);
    }
}
