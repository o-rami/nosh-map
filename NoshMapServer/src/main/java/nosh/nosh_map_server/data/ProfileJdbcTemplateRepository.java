package nosh.nosh_map_server.data;

import nosh.nosh_map_server.data.mappers.AppUserMapper;
import nosh.nosh_map_server.data.mappers.ProfileMapper;
import nosh.nosh_map_server.models.AppUser;
import nosh.nosh_map_server.models.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProfileJdbcTemplateRepository implements ProfileRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProfileJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Profile> findAll() {
        final String sql = "select first_name, last_name, address, app_user_id from user_profile limit 1000;";
        return jdbcTemplate.query(sql, new ProfileMapper());
    }

    @Override
    public Profile findByUserId(int appUserId) {
        final String sql ="SELECT u.first_name, u.last_name, u.address, u.app_user_id "
                + "FROM user_profile u "
                + "INNER JOIN app_user a on a.app_user_id = u.app_user_id "
                + "WHERE u.app_user_id = ?;";
        Profile result = jdbcTemplate.query(sql, new ProfileMapper(), appUserId).stream()
                .findFirst()
                .orElse(null);

        if(result != null) {
            addAppUserId(result);
        }

        return result;
    }

    @Override
    public boolean add(Profile profile) {
        final String sql = "insert into user_profile (first_name, last_name, address, app_user_id) "
                + "values (?,?,?,?);";

        if(profile == null) {
            return false;
        }

        System.out.println("profile: " + profile.toString() + "In add in repo");

        return jdbcTemplate.update(sql,
                profile.getFirstName(),
                profile.getLastName(),
                profile.getAddress(),
                profile.getAppUserId()) > 0;
    }

    @Override
    public boolean update(Profile profile) {
        final String sql = "update user_profile set "
                + "first_name = ?, "
                + "last_name = ?, "
                + "address = ? "
                + "where app_user_id = ?;";
        return jdbcTemplate.update(sql,
                profile.getFirstName(),
                profile.getLastName(),
                profile.getAddress(),
                profile.getAppUserId()) > 0;
    }

    @Override
    public boolean deleteById(int appUserId) {
        return jdbcTemplate.update(
                "delete from user_profile where app_user_id = ?", appUserId) > 0;

    }

    private void addAppUserId(Profile profile){
        final String sql = "select a.app_user_id, a.username, a.password_hash, a.enabled "
                + "from app_user a "
                + "where a.app_user_id = ?;";
        List<AppUser> appUsers = jdbcTemplate.query(sql, new AppUserMapper(new ArrayList<>(List.of("USER"))), profile.getAppUserId());

        if(!appUsers.isEmpty()) {
            int appUserId = appUsers.get(0).getAppUserId();
            profile.setAppUserId(appUserId);
        }
    }
}