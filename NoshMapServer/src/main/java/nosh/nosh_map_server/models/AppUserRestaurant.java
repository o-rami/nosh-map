package nosh.nosh_map_server.models;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class AppUserRestaurant {
    @NotNull(message = "uppUserId cannot be null.")
    private int appUserId;
    @NotNull(message = "restaurantId cannot be null.")
    private int restaurantId;
    private String identifier;

    public AppUserRestaurant() {
    }

    public AppUserRestaurant(int appUserId, int restaurantId) {
        this.appUserId = appUserId;
        this.restaurantId = restaurantId;
    }

    public AppUserRestaurant(int appUserId, int restaurantId, String identifier) {
        this.appUserId = appUserId;
        this.restaurantId = restaurantId;
        this.identifier = identifier;
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

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppUserRestaurant that = (AppUserRestaurant) o;
        return appUserId == that.appUserId && restaurantId == that.restaurantId && Objects.equals(identifier, that.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appUserId, restaurantId, identifier);
    }

    @Override
    public String toString() {
        return "AppUserRestaurant{" +
                "appUserId=" + appUserId +
                ", restaurantId=" + restaurantId +
                ", identifier='" + identifier + '\'' +
                '}';
    }
}
