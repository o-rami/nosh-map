package nosh.nosh_map_server.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Objects;

public class Comment {

    @PositiveOrZero
    private int commentId;
    @NotNull(message = "message cannot be null.")
    @NotBlank(message = "message cannot be blank.")
    private String description;
    @Positive(message = "restaurantId must be greater than 0.")
    private int restaurantId;
    @Positive(message = "appUserId must be greater than 0.")
    private int appUserId;
    @NotNull(message = "date and time cannot be null.")
    @PastOrPresent(message = "date cannot be in the future.")
    //@DateTimeFormat(pattern = "yyyy-MM-dd-HH:mm:ss", fallbackPatterns = {"yyyy-MM-dd'T'HH:mm:ss", "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss"})
    private LocalDateTime postDate;
    private boolean isPublic;

    public Comment(int commentId, String description, int restaurantId, int appUserId, LocalDateTime postDate, boolean isPublic) {
        this.commentId = commentId;
        this.description = description;
        this.restaurantId = restaurantId;
        this.appUserId = appUserId;
        this.postDate = postDate;
        this.isPublic = isPublic;
    }

    public Comment() {}

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public int getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(int appUserId) {
        this.appUserId = appUserId;
    }

    public LocalDateTime getPostDate() {
        return postDate;
    }

    public void setPostDate(LocalDateTime postDate) {
        this.postDate = postDate;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return commentId == comment.commentId && restaurantId == comment.restaurantId && appUserId == comment.appUserId
                && Objects.equals(description, comment.description) && Objects.equals(postDate, comment.postDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentId, description, restaurantId, appUserId, postDate);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", description='" + description + '\'' +
                ", restaurantId=" + restaurantId +
                ", appUserId=" + appUserId +
                ", postDate=" + postDate +
                ", isPublic=" + isPublic +
                '}';
    }
}
