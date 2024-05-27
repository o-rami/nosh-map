package nosh.nosh_map_server.controllers;

import nosh.nosh_map_server.domain.RestaurantService;
import nosh.nosh_map_server.domain.Result;
import nosh.nosh_map_server.models.Restaurant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin
@RequestMapping("/api/restaurant")
public class RestaurantController {

    private final RestaurantService service;

    public RestaurantController(RestaurantService service) {
        this.service = service;
    }

    @GetMapping
    public List<Restaurant> findAll() {
        return service.findAll();
    }

    @GetMapping("/appUserId/{appUserId}")
    public List<Restaurant> getFavRestaurants(@PathVariable int appUserId) {
        return service.getFavRestaurants(appUserId);
    }

    @GetMapping("/listId/{appUserId}")
    public List<Restaurant> findByUserId(@PathVariable int appUserId) {
        List<Restaurant> result = service.findByUserId(appUserId);
        return result;
    }

    @GetMapping("/restaurantId/{restaurantId}")
    public ResponseEntity<Restaurant> findByRestaurantId(@PathVariable int restaurantId) {
        Restaurant restaurant = service.findByRestaurantId(restaurantId);
        if (restaurant == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(restaurant);
    }


    @PostMapping
    public ResponseEntity<Object> add(@RequestBody Restaurant restaurant) {
        Result<Restaurant> result = service.add(restaurant);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @PutMapping("/{restaurantId}")
    public ResponseEntity<Object> update(@PathVariable int restaurantId, @RequestBody Restaurant restaurant) {
        if (restaurant != null && restaurantId != restaurant.getRestaurantId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<Restaurant> result = service.update(restaurant);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponse.build(result);
    }

    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<Void> deleteById(@PathVariable int restaurantId) {
        Result<Restaurant> result = service.deleteById(restaurantId);
        return new ResponseEntity<>(getStatus(result, HttpStatus.NO_CONTENT));
    }

    private HttpStatus getStatus(Result<Restaurant> result, HttpStatus statusDefault) {
        return switch (result.getStatus()) {
            case INVALID -> HttpStatus.PRECONDITION_FAILED;
            case DUPLICATE -> HttpStatus.FORBIDDEN;
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            default -> statusDefault;
        };
    }
}
