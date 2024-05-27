package nosh.nosh_map_server.data;

import nosh.nosh_map_server.models.Comment;

import java.util.List;

public interface CommentRepository {
    Comment findByCommentId(int commentId);

    List<Comment> findByUserId(int appUserId);
    List<Comment> findByRestaurantId(int restaurantId);

    List<Comment> getPublic(int restaurantId);

    Comment add(Comment comment);

    boolean update(Comment comment);

    boolean deleteById(int commentId);


}
