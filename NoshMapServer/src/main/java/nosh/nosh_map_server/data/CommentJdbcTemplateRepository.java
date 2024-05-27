package nosh.nosh_map_server.data;

import nosh.nosh_map_server.data.mappers.CommentMapper;
import nosh.nosh_map_server.models.Comment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class CommentJdbcTemplateRepository implements CommentRepository {

    private final JdbcTemplate jdbcTemplate;

    public CommentJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Comment findByCommentId(int commentId) {
        final String sql =
                "SELECT " +
                        "c.user_comment_id, " +
                        "c.`description`, " +
                        "c.is_public, " +
                        "c.post_date, " +
                        "c.app_user_id, " +
                        "c.restaurant_id " +
                        "FROM user_comment c " +
                        "WHERE c.user_comment_id = ?;";
        return jdbcTemplate.query(sql, new CommentMapper(), commentId).stream().findFirst().orElse(null);
    }

    @Override
    public List<Comment> findByUserId(int appUserId) {
        final String sql =
                "SELECT " +
                        "c.user_comment_id, " +
                        "c.`description`, " +
                        "c.is_public, " +
                        "c.post_date, " +
                        "c.app_user_id, " +
                        "c.restaurant_id " +
                        "FROM user_comment c " +
                        "WHERE c.app_user_id = ?;";
        return jdbcTemplate.query(sql, new CommentMapper(), appUserId);
    }

    @Override
    public List<Comment> findByRestaurantId(int restaurantId) {
        final String sql =
                "SELECT " +
                        "c.user_comment_id, " +
                        "c.`description`, " +
                        "c.is_public, " +
                        "c.post_date, " +
                        "c.app_user_id, " +
                        "c.restaurant_id " +
                        "FROM user_comment c " +
                        "WHERE c.restaurant_id = ?;";
        return jdbcTemplate.query(sql, new CommentMapper(), restaurantId);
    }

    @Override
    public List<Comment> getPublic(int restaurantId) {
        final String sql =
                "SELECT " +
                        "c.user_comment_id, " +
                        "c.`description`, " +
                        "c.is_public, " +
                        "c.post_date, " +
                        "c.app_user_id, " +
                        "c.restaurant_id " +
                        "FROM user_comment c " +
                        "WHERE c.restaurant_id = ? && c.is_public;";
        return jdbcTemplate.query(sql, new CommentMapper(), restaurantId);
    }

    @Override
    public Comment add(Comment comment) {
        final String sql =
                "insert into user_comment (`description`,is_public,post_date,app_user_id,restaurant_id) " +
                        "values " +
                        "(?,?,?,?,?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, comment.getDescription());
            ps.setBoolean(2, comment.isPublic());
            ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            ps.setInt(4, comment.getAppUserId());
            ps.setInt(5, comment.getRestaurantId());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        comment.setCommentId(keyHolder.getKey().intValue());
        return comment;
    }

    @Override
    public boolean update(Comment comment) {
        final String sql = "update user_comment set " +
                "`description` = ?, " +
                "is_public = ? " +
                "where user_comment_id = ?;";

        return jdbcTemplate.update(sql,
                comment.getDescription(),
                comment.isPublic(),
                comment.getCommentId()) > 0;
    }

    @Override
    public boolean deleteById(int commentId) {
        return jdbcTemplate.update(
                "delete from user_comment where user_comment_id = ?", commentId) > 0;
    }
}
