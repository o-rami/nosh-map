package nosh.nosh_map_server.domain;

import nosh.nosh_map_server.data.RatingRepository;
import nosh.nosh_map_server.models.Rating;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class RatingServiceTest {

    @Autowired
    RatingService service;

    @MockBean
    RatingRepository repository;

    @Test
    void shouldNotAddNullRating() {
        Result<Rating> rating = service.add(null);
        assertFalse(rating.isSuccess());
    }

    @Test
    void shouldNotAddWhenInvalidScore() {
        Rating rating = makeRating();
        rating.setScore(10);
        Result<Rating> actual = service.add(rating);
        assertEquals(ActionStatus.INVALID, actual.getStatus());
    }

    @Test
    void shouldNotAddWhenInvalidOrNullDescription() {
        Rating rating = makeRating();
        rating.setDescription(null);
        Result<Rating> actual = service.add(rating);
        assertEquals(ActionStatus.INVALID, actual.getStatus());
    }

    @Test
    void shouldAdd() {
        Rating rating = makeRating();
        Rating mockOut = makeRating();
        mockOut.setRatingId(1);

        when(repository.add(rating)).thenReturn(mockOut);

        Result<Rating> actual = service.add(rating);
        assertEquals(ActionStatus.SUCCESS, actual.getStatus());
        assertEquals(mockOut, actual.getPayload());
    }

    @Test
    void shouldNotUpdateWhenInvalidRating() {
        Rating rating = makeRating();
        Result<Rating> actual = service.update(rating);
        assertEquals(ActionStatus.INVALID, actual.getStatus());
    }

    @Test
    void shouldNotUpdateWithInvalidDescription() {
        Rating rating = makeRating();
        rating.setDescription(null);
        Result<Rating> actual = service.update(rating);
        assertEquals(ActionStatus.INVALID, actual.getStatus());
    }

    @Test
    void shouldUpdateHappyPath() {
        Rating rating = makeRating();
        rating.setRatingId(1);

        when(repository.update(rating)).thenReturn(true);

        Result<Rating> actual = service.update(rating);
        assertEquals(ActionStatus.SUCCESS, actual.getStatus());
    }


    private Rating makeRating() {
        Rating rating = new Rating();
        rating.setScore(2);
        rating.setDescription("Test description");
        rating.setRestaurantId(1);
        rating.setAppUserId(1);
        return rating;
    }

}