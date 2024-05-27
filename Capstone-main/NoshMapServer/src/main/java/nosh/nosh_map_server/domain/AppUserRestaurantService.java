package nosh.nosh_map_server.domain;

import nosh.nosh_map_server.data.AppUserRestaurantRepository;
import nosh.nosh_map_server.models.AppUserRestaurant;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

@Service
public class AppUserRestaurantService {

    private final AppUserRestaurantRepository restaurantRepository;
    private final Validator validator;

    public AppUserRestaurantService(AppUserRestaurantRepository restaurantRepository, Validator validator) {
        this.restaurantRepository = restaurantRepository;
        this.validator = validator;
    }

    public List<AppUserRestaurant> findByUserId(int appUserId) {
        return restaurantRepository.findByUserId(appUserId);
    }

    public Result<AppUserRestaurant> addToBridge(AppUserRestaurant aur) {

        Result<AppUserRestaurant> result = validate(aur);
        if (result.getStatus() != ActionStatus.SUCCESS) {
            return result;
        }

        List<AppUserRestaurant> userRestaurants = findByUserId(aur.getAppUserId());
        for (int i = 0; i < userRestaurants.size(); i++) {
            if (userRestaurants.get(i).getIdentifier().equalsIgnoreCase(aur.getIdentifier())) {
                result.setPayload(userRestaurants.get(i));
                return result;
            }
        }

        AppUserRestaurant inserted  = restaurantRepository.addToBridge(aur);
        if (inserted == null) {
            result.addMessage(ActionStatus.INVALID, "insert failed");
        } else {
            result.setPayload(inserted);
        }

        return result;
    }

    private Result<AppUserRestaurant> validate(AppUserRestaurant aur) {

        Result<AppUserRestaurant> result = new Result<>();

        if (aur == null) {
            result.addMessage(ActionStatus.INVALID, "AppUserRestaurant cannot be null.");
            return result;
        }

        Set<ConstraintViolation<AppUserRestaurant>> violations = validator.validate(aur);

        for (ConstraintViolation<AppUserRestaurant> violation : violations) {
            result.addMessage(ActionStatus.INVALID, violation.getMessage());
        }

        /*List<AppUserRestaurant> aurs = restaurantRepository.findByUserId(aur.getAppUserId());
        for (AppUserRestaurant a : aurs) {
            if (aur == a) {
                result.addMessage(ActionStatus.INVALID, "AppUserRestaurant cannot be a duplicate.");
            }
        }*/

        return result;
    }
}
