package nosh.nosh_map_server.domain;

import nosh.nosh_map_server.data.RestaurantRepository;
import org.springframework.stereotype.Service;
import nosh.nosh_map_server.models.Restaurant;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final Validator validator;

    public RestaurantService(RestaurantRepository restaurantRepository, Validator validator) {
        this.restaurantRepository = restaurantRepository;
        this.validator = validator;
    }

    public List<Restaurant> findAll() {
        return restaurantRepository.findAll();
    }

    public Restaurant findByRestaurantId(int restaurantId) {
        return restaurantRepository.findByRestaurantId(restaurantId);
    }

    public List<Restaurant> getFavRestaurants(int appUserId) {
        return restaurantRepository.getFavRestaurants(appUserId);
    }

    public List<Restaurant> findByUserId(int appUserId) {
        return restaurantRepository.findByUserId(appUserId);
    }

    public Result<Restaurant> add(Restaurant restaurant) {

        Result<Restaurant> result = validate(restaurant);
        if (result.getStatus() != ActionStatus.SUCCESS) {
            return result;
        }

        List<Restaurant> restaurants = findAll();
        for (int i = 0; i < restaurants.size(); i++) {
            if (restaurants.get(i).getGooglePlaceId().equalsIgnoreCase(restaurant.getGooglePlaceId())) {
                result.setPayload(restaurants.get(i));
                return result;
            }
        }

        Restaurant inserted  = restaurantRepository.add(restaurant);
        if (inserted == null) {
            result.addMessage(ActionStatus.INVALID, "insert failed");
        } else {
            result.setPayload(inserted);
        }

        return result;
    }

    public Result<Restaurant> update(Restaurant restaurant) {

        Result<Restaurant> result = validate(restaurant);
        if (result.getStatus() != ActionStatus.SUCCESS) {
            return result;
        }

        if (!restaurantRepository.update(restaurant)) {
            result.addMessage(ActionStatus.NOT_FOUND, "restaurant id `" + restaurant.getRestaurantId() + "` not found.");
        }

        return result;
    }

    public Result<Restaurant> deleteById(int restaurantId) {
        Result<Restaurant> result = new Result<>();
        boolean deleted = restaurantRepository.deleteById(restaurantId);
        if (!deleted) {
            result.addMessage(ActionStatus.NOT_FOUND, "restaurant id `" + restaurantId + "` not found.");
        }
        return result;
    }

    private Result<Restaurant> validate(Restaurant restaurant) {

        Result<Restaurant> result = new Result<>();

        if (restaurant == null) {
            result.addMessage(ActionStatus.INVALID, "restaurant cannot be null.");
            return result;
        }

        Set<ConstraintViolation<Restaurant>> violations = validator.validate(restaurant);

        for (ConstraintViolation<Restaurant> violation : violations) {
            result.addMessage(ActionStatus.INVALID, violation.getMessage());
        }

        return result;
    }
}
