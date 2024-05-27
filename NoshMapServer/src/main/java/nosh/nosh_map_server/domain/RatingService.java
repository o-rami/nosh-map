package nosh.nosh_map_server.domain;

import nosh.nosh_map_server.data.RatingRepository;
import nosh.nosh_map_server.models.Rating;
import org.springframework.stereotype.Service;


@Service
public class RatingService {

    private final RatingRepository ratingRepository;

    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public Rating findById(int ratingId) {
        return ratingRepository.findById(ratingId);
    }
    public Rating findByRestaurantUserId(int restaurantId, int appUserId) {return ratingRepository.findByRestaurantUserId(restaurantId, appUserId);}

    public Result<Rating> add(Rating rating) {
        Result<Rating> result = validate(rating);
        if(!result.isSuccess()) {
            return result;
        }

        if(rating.getRatingId() != 0) {
            result.addMessage(ActionStatus.INVALID, "ratingId cannot be set for `add` operation");
            return result;
        }

        Rating isDuplicate = ratingRepository.findByRestaurantUserId(rating.getRestaurantId(), rating.getAppUserId());

        // Don't allow two ratings from one user for one restaurant, no error, just update it instead
        if (isDuplicate != null) {
            rating.setRatingId(isDuplicate.getRatingId());
            return update(rating);
        }

        rating = ratingRepository.add(rating);
        result.setPayload(rating);
        return result;
    }

    public Result<Rating> update(Rating rating) {
        Result<Rating> result = validate(rating);
        if(!result.isSuccess()){
            return result;
        }
        if(rating.getRatingId() <= 0){
            result.addMessage(ActionStatus.INVALID, "ratingId must be set for `update` operation ");
            return result;
        }
        if(!ratingRepository.update(rating)) {
            String msg = String.format("ratingId: %s, not found", rating.getRatingId());
            result.addMessage(ActionStatus.NOT_FOUND, msg);
        }

        return result;
    }

    public boolean deleteById(int ratingId) {
        return ratingRepository.deleteById(ratingId);
    }

    private Result<Rating> validate(Rating rating){
        Result<Rating> result = new Result<>();

        if(rating == null) {
            result.addMessage(ActionStatus.INVALID, "score cannot be null");
            return result;
        }

        if(rating.getScore() <= 1 || rating.getScore() > 5) {
            result.addMessage(ActionStatus.INVALID, "Score has to be between 1 - 5 ");
            return result;
        }

        if(rating.getDescription() == null) {
            result.addMessage(ActionStatus.INVALID, "description cannot be null");
            return result;
        }

       return result;
    }
}