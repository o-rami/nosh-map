package nosh.nosh_map_server.controllers;

import nosh.nosh_map_server.domain.MealService;
import nosh.nosh_map_server.domain.Result;
import nosh.nosh_map_server.models.Meal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/meal")
public class MealController {

    private final MealService service;

    public MealController(MealService service) {
        this.service = service;
    }

    @GetMapping("/{mealId}")
    public ResponseEntity<Meal> findByMealId(@PathVariable int mealId) {
        Meal meal = service.findByMealId(mealId);
        if (meal == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(meal);
    }

    @GetMapping("/appUserId/{appUserId}")
    public List<Meal> findByUserId(@PathVariable int appUserId) {
        return service.findByUserId(appUserId);
    }

    @GetMapping("/restaurantId/{restaurantId}")
    public List<Meal> findByRestaurantId(@PathVariable int restaurantId) {
        return service.findByRestaurantId(restaurantId);
    }

    @GetMapping("/public/{appUserId}")
    public List<Meal> getPublic(@PathVariable int appUserId) {
        return service.getPublic(appUserId);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody Meal meal) {
        Result<Meal> result = service.add(meal);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @PutMapping("/{mealId}")
    public ResponseEntity<Object> update(@PathVariable int mealId, @RequestBody Meal meal) {
        if (meal != null && mealId != meal.getMealId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<Meal> result = service.update(meal);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponse.build(result);
    }

    @DeleteMapping("/{mealId}")
    public ResponseEntity<Void> deleteById(@PathVariable int mealId) {
        Result<Meal> result = service.deleteById(mealId);
        return new ResponseEntity<>(getStatus(result, HttpStatus.NO_CONTENT));
    }

    private HttpStatus getStatus(Result<Meal> result, HttpStatus statusDefault) {
        return switch (result.getStatus()) {
            case INVALID -> HttpStatus.PRECONDITION_FAILED;
            case DUPLICATE -> HttpStatus.FORBIDDEN;
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            default -> statusDefault;
        };
    }
}
