package nosh.nosh_map_server.domain;

import nosh.nosh_map_server.data.CommentRepository;
import nosh.nosh_map_server.models.Comment;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final Validator validator;

    public CommentService(CommentRepository commentRepository, Validator validator) {
        this.commentRepository = commentRepository;
        this.validator = validator;
    }

    public Comment findByCommentId(int commentId) {return commentRepository.findByCommentId(commentId);}
    public List<Comment> findByUserId(int appUserId) {return commentRepository.findByUserId(appUserId);}
    public List<Comment> findByRestaurantId(int restaurantId) {return commentRepository.findByRestaurantId(restaurantId);}
    public List<Comment> getPublic(int restaurantId) {return commentRepository.getPublic(restaurantId);}

    public Result<Comment> add(Comment comment) {

        Result<Comment> result = validate(comment);
        if (result.getStatus() != ActionStatus.SUCCESS) {
            return result;
        }

        System.out.println("PASSED VALIDATION METHOD");

        Comment inserted = commentRepository.add(comment);
        if (inserted == null) {
            result.addMessage(ActionStatus.INVALID, "insert failed");
        } else {
            result.setPayload(inserted);
        }

        return result;
    }

    public Result<Comment> update(Comment comment) {

        Result<Comment> result = validate(comment);
        if (result.getStatus() != ActionStatus.SUCCESS) {
            return result;
        }

        if (!commentRepository.update(comment)) {
            result.addMessage(ActionStatus.NOT_FOUND, "comment id `" + comment.getCommentId() + "` not found.");
        }

        return result;
    }

    public Result<Comment> deleteById(int commentId) {
        Result<Comment> result = new Result<>();
        boolean deleted = commentRepository.deleteById(commentId);
        if (!deleted) {
            result.addMessage(ActionStatus.NOT_FOUND, "comment id `" + commentId + "` not found.");
        }
        return result;
    }

    private Result<Comment> validate(Comment comment) {

        Result<Comment> result = new Result<>();

        if (comment == null) {
            result.addMessage(ActionStatus.INVALID, "comment cannot be null.");
            return result;
        }

        System.out.println("COMMENT IS NOT NULL");

        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);

        for (ConstraintViolation<Comment> violation : violations) {
            result.addMessage(ActionStatus.INVALID, violation.getMessage());
        }

        System.out.println("PASSED VALIDATOR" + result.getMessages());

        return result;
    }
}
