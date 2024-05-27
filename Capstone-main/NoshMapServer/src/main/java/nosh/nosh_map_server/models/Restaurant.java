package nosh.nosh_map_server.models;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.Objects;

public class Restaurant {
    @PositiveOrZero
    private int restaurantId;
    private String googlePlaceId;
    @NotNull(message = "Name cannot be null.")
    private String name;
    @NotNull(message = "Address cannot be null.")
    private String address;
    @NotNull(message = "Zipcode cannot be null.")
    private String zipCode;

    private String website;
    private String email;
    private String phone;
    @Min(value = -90, message = "Latitude must be between -90 and 90 degrees.")
    @Max(value = 90, message = "Latitude must be between -90 and 90 degrees.")
    private double latitude;
    @Min(value = -180, message = "Longitude must be between -180 and 180 degrees.")
    @Max(value = 180, message = "Longitude must be between -180 and 180 degrees.")
    private double longitude;

    @NotNull(message = "State cannot be null.")
    private String state;
    @NotNull(message = "City cannot be null.")
    private String city;
    private String hoursInterval;
    private String cuisineType;

    public Restaurant() {

    }

    public Restaurant(int restaurantId, String googlePlaceId, String name,
                      String address, String zipCode, String website, String email,
                      String phone, double latitude, double longitude, String state,
                      String city, String hoursInterval, String cuisineType) {
        this.restaurantId = restaurantId;
        this.googlePlaceId = googlePlaceId;
        this.name = name;
        this.address = address;
        this.zipCode = zipCode;
        this.website = website;
        this.email = email;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.state = state;
        this.city = city;
        this.hoursInterval = hoursInterval;
        this.cuisineType = cuisineType;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getGooglePlaceId() {
        return googlePlaceId;
    }

    public void setGooglePlaceId(String googlePlaceId) {
        this.googlePlaceId = googlePlaceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getHoursInterval() {
        return hoursInterval;
    }

    public void setHoursInterval(String hoursInterval) {
        this.hoursInterval = hoursInterval;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public void setCuisineType(String cuisineType) {
        this.cuisineType = cuisineType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Restaurant that = (Restaurant) o;
        return restaurantId == that.restaurantId &&
                Double.compare(that.latitude, latitude) == 0 &&
                Double.compare(that.longitude, longitude) == 0 &&
                Objects.equals(googlePlaceId, that.googlePlaceId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(address, that.address) &&
                Objects.equals(zipCode, that.zipCode) &&
                Objects.equals(website, that.website) &&
                Objects.equals(email, that.email) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(state, that.state) &&
                Objects.equals(city, that.city) &&
                Objects.equals(hoursInterval, that.hoursInterval) &&
                Objects.equals(cuisineType, that.cuisineType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(restaurantId, googlePlaceId, name,
                address, zipCode, website, email, phone, latitude,
                longitude, state, city, hoursInterval, cuisineType);
    }
}
