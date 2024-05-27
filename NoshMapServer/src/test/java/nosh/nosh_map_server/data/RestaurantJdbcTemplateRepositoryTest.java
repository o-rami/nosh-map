package nosh.nosh_map_server.data;

import nosh.nosh_map_server.models.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RestaurantJdbcTemplateRepositoryTest {

    @Autowired
    RestaurantJdbcTemplateRepository restaurantRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        jdbcTemplate.update("call set_known_good_state();");
    }

    @Test
    void shouldFindAllRestaurants() {
        List<Restaurant> actual = restaurantRepository.findAll();

        assertTrue(actual.size() > 2);
        assertEquals("New York City", actual.get(0).getCity());
    }

    @Test
    void shouldGetFavRestaurants() {
        List<Restaurant> actual = restaurantRepository.getFavRestaurants(1);

        assertEquals(1, actual.size());
        assertEquals("Little Italy Pizza", actual.get(0).getName());
    }

    @Test
    void shouldFindRestaurantById() {
        Restaurant expected = new Restaurant(3,
                "testGoogleId",
                "Locanda Verde",
                "377 Greenwich St",
                "10013-2338",
                "http://locandaverdenyc.com/",
                "info@locandaverdenyc.com",
                "+1 212-925-3797",
                40.720085,
                -74.01022,
                "NY",
                "New York City",
                "Mon (5:00 PM - 10:45 PM) | Tue - Sun (5:00 PM - 11:00 PM) | "
                        + "Tue - Fri (7:00 AM - 11:00 AM) | Sat - Mon (7:00 AM - 3:00 PM)",
                "American,Italian");
        Restaurant actual = restaurantRepository.findByRestaurantId(3);

        assertEquals(expected, actual);
    }


    @Test
    void shouldAddRestaurant() {
        Restaurant restaurant = makeFakeRestaurant();
        Restaurant expected = makeFakeRestaurant();
        expected.setRestaurantId(7);

        Restaurant actual = restaurantRepository.add(restaurant);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void shouldUpdateRestaurant() {
        Restaurant restaurant = makeFirstRestaurant();
        restaurant.setName("Updated");

        assertTrue(restaurantRepository.update(restaurant));

        Restaurant actual = restaurantRepository.findByRestaurantId(1);

        assertEquals(restaurant, actual);
    }

    @Test
    void shouldNotUpdateMissingRestaurant() {
        Restaurant restaurant = makeFakeRestaurant();
        restaurant.setRestaurantId(55);

        assertFalse(restaurantRepository.update(restaurant));
    }

    @Test
    void shouldDeleteRestaurantById() {
        assertTrue(restaurantRepository.deleteById(2));

    }

    @Test
    void shouldNotDeleteMissingRestaurantById() {
        assertFalse(restaurantRepository.deleteById(55));
    }

    private Restaurant makeFakeRestaurant() {
        return new Restaurant(0,
                "testGoogleId",
                "Bojangles",
                "330 S Churton St",
                "27278-2509",
                "https://locations.bojangles.com/nc/hillsborough/330-s--churton-st.html",
                null,
                "+1 919-245-3720",
                36.062344,
                -79.10287,
                "NC",
                "Hillsborough",
                "Sun - Thu (5:30 AM - 10:00 PM) | Fri - Sat (5:30 AM - 11:00 PM)",
                "American,Fast Food");
    }

    private Restaurant makeFirstRestaurant() {
        return new Restaurant(1,
                "TestGoogleId",
                "Vivi Bubble Tea",
                "1324 2nd Ave",
                "10021-5408",
                null,
                "http://www.vivibubbletea.com/",
                "+1 646-858-0710",
                40.76726,
                -73.95927,
                "NY",
                "New York City",
                "Sun - Sat (11:00 AM - 9:00 PM)",
                "Healthy");
    }
}