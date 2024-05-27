package nosh.nosh_map_server.controllers;

import nosh.nosh_map_server.domain.RatingService;
import nosh.nosh_map_server.domain.Result;
import nosh.nosh_map_server.models.Rating;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/rating")
public class RatingController {

    private final RatingService service;

    public RatingController(RatingService service) {
        this.service = service;
    }

    @GetMapping("/{ratingId}")
    public ResponseEntity<Rating> findByRatingId(@PathVariable int ratingId) {
        Rating rating = service.findById(ratingId);
        if(rating == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(rating);
    }

    @GetMapping("/{restaurantId}/{appUserId}")
    public ResponseEntity<Rating> findByRestaurantUserId(@PathVariable int restaurantId, @PathVariable int appUserId) {
        Rating rating = service.findByRestaurantUserId(restaurantId, appUserId);
        if(rating == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(rating);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody Rating score) {
        Result<Rating> result = service.add(score);
        if(result.isSuccess()){
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @PutMapping("/{ratingId}")
    public ResponseEntity<Object> update(@PathVariable int ratingId, @RequestBody Rating score){
        if(ratingId != score.getRatingId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<Rating> result = service.update(score);
        if(result.isSuccess()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponse.build(result);
    }

    @DeleteMapping("{ratingId}")
    public ResponseEntity<Void> deleteById(@PathVariable int ratingId){
        if(service.deleteById(ratingId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}