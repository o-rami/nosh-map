package nosh.nosh_map_server.data;

import nosh.nosh_map_server.data.mappers.AppUserMealMapper;
import nosh.nosh_map_server.data.mappers.AppUserRestaurantMapper;
import nosh.nosh_map_server.models.AppUserMeal;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class AppUserMealJdbcTemplateRepository implements AppUserMealRepository{

    private final JdbcTemplate jdbcTemplate;

    public AppUserMealJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public AppUserMeal addToBridge(AppUserMeal aum) {
        final String sql = "insert into app_user_meal (app_user_id, meal_id, identifier) values (?, ?, ?);";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, aum.getAppUserId());
            ps.setInt(2, aum.getMealId());
            ps.setString(3, aum.getIdentifier());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }
        return aum;
    }

    @Override
    public List<AppUserMeal> findByUserId(int appUserId) {
        final String sql =
                "SELECT " +
                        "a.app_user_id, " +
                        "a.meal_id, " +
                        "a.identifier " +
                        "FROM app_user_meal a " +
                        "WHERE a.app_user_id = ?;";
        return jdbcTemplate.query(sql, new AppUserMealMapper(), appUserId);
    }
}
