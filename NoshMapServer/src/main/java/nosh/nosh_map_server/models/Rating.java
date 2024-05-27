package nosh.nosh_map_server.models;

import java.util.Objects;

public class Rating {

    private int ratingId;
    private int score;
    private String description;
    private int appUserId;
    private int restaurantId;

    public Rating() {

    }

    public Rating(int ratingId, int score, String description, int appUserId, int restaurantId) {
        this.ratingId = ratingId;
        this.score = score;
        this.description = description;
        this.appUserId = appUserId;
        this.restaurantId = restaurantId;
    }

    public int getRatingId() {
        return ratingId;
    }

    public void setRatingId(int ratingId) {
        this.ratingId = ratingId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(int appUserId) {
        this.appUserId = appUserId;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rating rating)) return false;
        return getRatingId() == rating.getRatingId() && getScore() == rating.getScore()
                && getAppUserId() == rating.getAppUserId() && getRestaurantId() == rating.getRestaurantId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRatingId(), getScore(), getAppUserId(), getRestaurantId());
    }

    @Override
    public String toString() {
        return "Rating{" +
                "ratingId=" + ratingId +
                ", score=" + score +
                ", description='" + description + '\'' +
                ", appUserId=" + appUserId +
                ", restaurantId=" + restaurantId +
                '}';
    }
}
