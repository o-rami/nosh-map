package nosh.nosh_map_server.data.mappers;

import nosh.nosh_map_server.models.Comment;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommentMapper implements RowMapper<Comment> {
    @Override
    public Comment mapRow(ResultSet rs, int i) throws SQLException {
        Comment comment = new Comment();
        comment.setCommentId(rs.getInt("user_comment_id"));
        comment.setDescription(rs.getString("description"));
        comment.setPostDate(rs.getTimestamp("post_date").toLocalDateTime());
        comment.setPublic(rs.getBoolean("is_public"));
        comment.setRestaurantId(rs.getInt("restaurant_id"));
        comment.setAppUserId(rs.getInt("app_user_id"));

        return comment;
    }
}
