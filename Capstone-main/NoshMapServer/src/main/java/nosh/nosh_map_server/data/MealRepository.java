package nosh.nosh_map_server.data;

import nosh.nosh_map_server.models.Meal;

import java.util.List;

public interface MealRepository {

    Meal findByMealId(int mealId);

    List<Meal> findByUserId(int appUserId);

    List<Meal> findByRestaurantId(int mealId);

    List<Meal> getPublic(int appUserId);

    Meal add(Meal meal);

    boolean update(Meal meal);

    boolean deleteById(int mealId);
}