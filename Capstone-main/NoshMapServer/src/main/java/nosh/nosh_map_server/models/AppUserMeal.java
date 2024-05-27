package nosh.nosh_map_server.models;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class AppUserMeal {

    @NotNull(message = "uppUserId cannot be null.")
    private int appUserId;
    @NotNull(message = "restaurantId cannot be null.")
    private int mealId;
    private String identifier;

    public AppUserMeal() {
    }

    public AppUserMeal(int appUserId, int mealId, String identifier) {
        this.appUserId = appUserId;
        this.mealId = mealId;
        this.identifier = identifier;
    }

    public int getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(int appUserId) {
        this.appUserId = appUserId;
    }

    public int getMealId() {
        return mealId;
    }

    public void setMealId(int mealId) {
        this.mealId = mealId;
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
        AppUserMeal that = (AppUserMeal) o;
        return appUserId == that.appUserId && mealId == that.mealId && Objects.equals(identifier, that.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appUserId, mealId, identifier);
    }
}
