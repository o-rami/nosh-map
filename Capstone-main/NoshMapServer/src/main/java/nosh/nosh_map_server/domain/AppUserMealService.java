package nosh.nosh_map_server.domain;

import nosh.nosh_map_server.data.AppUserMealRepository;
import nosh.nosh_map_server.models.AppUserMeal;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

@Service
public class AppUserMealService {

    private final AppUserMealRepository repository;
    private final Validator validator;

    public AppUserMealService(AppUserMealRepository repository, Validator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    public List<AppUserMeal> findByUserId(int appUserId) {
        return repository.findByUserId(appUserId);
    }

    public Result<AppUserMeal> addToBridge(AppUserMeal aum) {

        Result<AppUserMeal> result = validate(aum);
        if (result.getStatus() != ActionStatus.SUCCESS) {
            return result;
        }

        List<AppUserMeal> userMeals = findByUserId(aum.getAppUserId());
        for (int i = 0; i < userMeals.size(); i++) {
            if (userMeals.get(i).getIdentifier().equalsIgnoreCase(aum.getIdentifier())) {
                result.setPayload(userMeals.get(i));
                return result;
            }
        }

        AppUserMeal inserted  = repository.addToBridge(aum);
        if (inserted == null) {
            result.addMessage(ActionStatus.INVALID, "insert failed");
        } else {
            result.setPayload(inserted);
        }

        return result;
    }

    private Result<AppUserMeal> validate(AppUserMeal aum) {

        Result<AppUserMeal> result = new Result<>();

        if (aum == null) {
            result.addMessage(ActionStatus.INVALID, "AppUserMeal cannot be null.");
            return result;
        }

        Set<ConstraintViolation<AppUserMeal>> violations = validator.validate(aum);

        for (ConstraintViolation<AppUserMeal> violation : violations) {
            result.addMessage(ActionStatus.INVALID, violation.getMessage());
        }

        return result;
    }
}
