package nosh.nosh_map_server.data;

import nosh.nosh_map_server.models.Rating;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RatingJdbcTemplateRepositoryTest {


    final static int NEXT_ID = 4;

    @Autowired
    RatingJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;
    @Autowired
    JdbcTemplate jdbcTemplate;

    // Some tests weren't passing
    // knownGoodState.hasRun stays true once it's flipped

//    @BeforeEach
//    void setUp() {
//        knownGoodState.set();
//    }

    @BeforeEach
    void setup() {
        jdbcTemplate.update("call set_known_good_state();");
    }

    @Test
    void shouldFindByRatingId() {
        Rating rating = repository.findById(1);
        assertNotNull(rating);
    }

    @Test
    void shouldFindByRestaurantUserId() {
        Rating rating = repository.findByRestaurantUserId(2, 1);
        assertNotNull(rating);
    }

    @Test
    void shouldNotFindByNonExistingRatingId() {
        Rating rating = repository.findById(NEXT_ID);
        assertNull(rating);
    }

    @Test
    void shouldAddNewRating() {
        Rating rating = makeRating();
        rating.setRatingId(NEXT_ID);
        Rating actual = repository.add(rating);
        assertNotNull(actual);
    }

    @Test
    void shouldUpdateExistingRatingId() {
        Rating rating = makeRating();
        rating.setScore(4);
        rating.setRatingId(1);
        assertTrue(repository.update(rating));
    }

    @Test
    void shouldNotUpdateNonExistingRatingId() {
        Rating rating = makeRating();
        rating.setScore(2);
        rating.setRatingId(NEXT_ID);
        assertFalse(repository.update(rating));
    }

    @Test
    void shouldDeleteByRatingId() {
        assertTrue(repository.deleteById(1));
        assertFalse(repository.deleteById(1));
    }

    private Rating makeRating() {
        Rating rating = new Rating();
        rating.setDescription("Passed Testing");
        rating.setAppUserId(1);
        rating.setRestaurantId(1);
        rating.setScore(5);
        return rating;
    }
}