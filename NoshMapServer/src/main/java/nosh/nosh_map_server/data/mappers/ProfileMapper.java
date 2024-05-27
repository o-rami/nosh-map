package nosh.nosh_map_server.data.mappers;

import nosh.nosh_map_server.models.Profile;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileMapper implements RowMapper<Profile> {
    @Override
    public Profile mapRow(ResultSet rs, int rowNum) throws SQLException {
        Profile profile = new Profile();
        profile.setFirstName(rs.getString("first_name"));
        profile.setLastName(rs.getString("last_name"));
        profile.setAddress(rs.getString("address"));
        profile.setAppUserId(rs.getInt("app_user_id"));
        return profile;
    }
}
