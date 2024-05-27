package nosh.nosh_map_server.domain;

import nosh.nosh_map_server.data.MealRepository;
import nosh.nosh_map_server.models.Meal;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

@Service
public class MealService {

    private final MealRepository mealRepository;
    private final Validator validator;

    public MealService(MealRepository mealRepository, Validator validator) {
        this.mealRepository = mealRepository;
        this.validator = validator;
    }

    public Meal findByMealId(int mealId) {
        return mealRepository.findByMealId(mealId);
    }

    public List<Meal> findByUserId(int appUserId) {
        return mealRepository.findByUserId(appUserId);
    }

    public List<Meal> findByRestaurantId(int restaurantId) {
        return mealRepository.findByRestaurantId(restaurantId);
    }

    public List<Meal> getPublic(int appUserId) {
        return mealRepository.getPublic(appUserId);
    }

    public Result<Meal> add(Meal meal) {

        Result<Meal> result = validate(meal);
        if (result.getStatus() != ActionStatus.SUCCESS) {
            return result;
        }

        Meal inserted  = mealRepository.add(meal);
        if (inserted == null) {
            result.addMessage(ActionStatus.INVALID, "insert failed");
        } else {
            result.setPayload(inserted);
        }

        return result;
    }

    public Result<Meal> update(Meal meal) {

        Result<Meal> result = validate(meal);
        if (result.getStatus() != ActionStatus.SUCCESS) {
            return result;
        }

        if (!mealRepository.update(meal)) {
            result.addMessage(ActionStatus.NOT_FOUND, "meal id `" + meal.getMealId() + "` not found.");
        }

        return result;
    }

    public Result<Meal> deleteById(int mealId) {
        Result<Meal> result = new Result<>();
        boolean deleted = mealRepository.deleteById(mealId);
        if (!deleted) {
            result.addMessage(ActionStatus.NOT_FOUND, "meal id `" + mealId + "` not found.");
        }
        return result;
    }

    private Result<Meal> validate(Meal meal) {

        Result<Meal> result = new Result<>();

        if (meal == null) {
            result.addMessage(ActionStatus.INVALID, "meal cannot be null.");
            return result;
        }

        Set<ConstraintViolation<Meal>> violations = validator.validate(meal);

        for (ConstraintViolation<Meal> violation : violations) {
            result.addMessage(ActionStatus.INVALID, violation.getMessage());
        }

        return result;
    }
}
