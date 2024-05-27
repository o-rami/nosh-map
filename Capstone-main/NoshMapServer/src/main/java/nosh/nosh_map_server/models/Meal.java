package nosh.nosh_map_server.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Meal {

    @PositiveOrZero
    private int mealId;
    @NotNull(message = "title cannot be null.")
    @NotBlank(message = "title cannot be blank.")
    private String title;
    @DecimalMin(value = "0.0", message ="price cannot be less than zero.")
    @NotNull(message = "price cannot be null.")
    private BigDecimal price;

    //@NotNull(message = "image URL cannot be null.")
    //@NotBlank(message = "image URL cannot be blank.")
    private String imageUrl;

    @NotBlank(message = "description cannot be blank.")
    @NotNull(message = "description cannot be null.")
    @Size(max = 500, message = "description cannot be greater than 500 characters.")
    private String description;

    //@NotNull(message = "date and time cannot be null.")
    //@PastOrPresent(message = "date cannot be in the future.")
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdated;
    @NotNull(message = "isPublic cannot be null.")
    private boolean isPublic;
    @Min(value = 1, message = "appUserId cannot be less than 1.")
    private int appUserId;
    @Min(value = 1, message = "restaurantId cannot be less than 1.")
    private int restaurantId;

    @NotNull
    public Meal() {

    }

    public Meal(int mealId, String title, BigDecimal price, String imageUrl, String description, LocalDateTime lastUpdated,
                boolean isPublic, int appUserId, int restaurantId) {
        this.mealId = mealId;
        this.title = title;
        this.price = price;
        this.imageUrl = imageUrl;
        this.description = description;
        this.lastUpdated = lastUpdated;
        this.isPublic = isPublic;
        this.appUserId = appUserId;
        this.restaurantId = restaurantId;
    }

    public int getMealId() {
        return mealId;
    }

    public void setMealId(int mealId) {
        this.mealId = mealId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
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
        if (o == null || getClass() != o.getClass()) return false;
        Meal meal = (Meal) o;
        return mealId == meal.mealId && isPublic == meal.isPublic && appUserId == meal.appUserId
                && restaurantId == meal.restaurantId && Objects.equals(title, meal.title)
                && Objects.equals(price, meal.price) && Objects.equals(imageUrl, meal.imageUrl)
                && Objects.equals(description, meal.description) && Objects.equals(lastUpdated, meal.lastUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mealId, title, price, imageUrl, description, lastUpdated, isPublic, appUserId,
                restaurantId);
    }
}
