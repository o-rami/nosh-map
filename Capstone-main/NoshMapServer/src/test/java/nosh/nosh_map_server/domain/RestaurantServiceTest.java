package nosh.nosh_map_server.domain;

import nosh.nosh_map_server.data.RestaurantRepository;
import nosh.nosh_map_server.models.Restaurant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
class RestaurantServiceTest {

    @MockBean
    RestaurantRepository repository;

    @Autowired
    RestaurantService service;

    @Test
    void shouldFindAllRestaurants() {
        List<Restaurant> restaurants = List.of(
                new Restaurant(1,"TestGoogleId", "Bojangles", "330 S Churton St", "27278-2509",
                        "https://locations.bojangles.com/nc/hillsborough/330-s--churton-st.html", "fake@fmail.com",
                        "+1 919-245-3720", 36.062344, -79.10287, "NC", "Hillsborough",
                        "Sun - Thu (5:30 AM - 10:00 PM) | Fri - Sat (5:30 AM - 11:00 PM)",
                        "American,Fast Food"),
                new Restaurant(1,"Fake Bar & Grill", "TestGoogleId", "Fake address", "00000-0000",
                        "https://definitely-a-place.com/ny/nyc/Fake-address", "fake@fmail.com",
                        "+1 555-555-5555", 35.000000, -79.00000, "ny", "nyc",
                        "Sun - Thu (5:30 AM - 10:00 PM) | Fri - Sat (5:30 AM - 11:00 PM)",
                        "Fake food")
        );

        when(repository.findAll()).thenReturn(restaurants);

        List<Restaurant> actual = service.findAll();
        assertEquals(2, actual.size());

        Restaurant restaurant = actual.get(0);
        assertEquals(1, restaurant.getRestaurantId());
        assertEquals("Bojangles", restaurant.getName());
        assertEquals("+1 919-245-3720", restaurant.getPhone());
        assertEquals("American,Fast Food", restaurant.getCuisineType());
    }

    @Test
    void shouldFindByRestaurantId() {
        Restaurant restaurant = makeFakeRestaurant();

        when(repository.findByRestaurantId(anyInt())).thenReturn(restaurant);

        Restaurant actual = service.findByRestaurantId(1);

        assertEquals(restaurant, actual);
    }


    @Test
    void shouldAddRestaurant() {
        Restaurant restaurant = makeFakeRestaurant();

        Restaurant expected = makeFakeRestaurant();
        expected.setRestaurantId(4);

        when(repository.add(restaurant)).thenReturn(expected);

        Result<Restaurant> actual = service.add(restaurant);
        assertTrue(actual.isSuccess());
        assertEquals(expected, actual.getPayload());
    }

    @Test
    void shouldNotAddNullMeal() {
        Result<Restaurant> actual = service.add(null);
        assertFalse(actual.isSuccess());
        assertEquals("restaurant cannot be null.", actual.getMessages().get(0));
    }

    @Test
    void shouldNotAddExistingMeal() {
        Result<Restaurant> actual = service.add(makeFirstRestaurant());
        assertFalse(actual.isSuccess());
        assertEquals(1, actual.getMessages().size());
        assertEquals("insert failed", actual.getMessages().get(0));
    }

    @Test
    void shouldNotAddNullTitle() {
        Restaurant restaurant = makeFakeRestaurant();
        restaurant.setName(null);

        Result<Restaurant> actual = service.add(restaurant);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getMessages().get(0).equalsIgnoreCase("Name cannot be null."));
    }

    @Test
    void shouldNotAddNullAddress() {
        Restaurant restaurant = makeFakeRestaurant();
        restaurant.setAddress(null);

        Result<Restaurant> actual = service.add(restaurant);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getMessages().get(0).equalsIgnoreCase("Address cannot be null."));
    }

    @Test
    void shouldNotAddNullZipcode() {
        Restaurant restaurant = makeFakeRestaurant();
        restaurant.setZipCode(null);

        Result<Restaurant> actual = service.add(restaurant);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getMessages().get(0).equalsIgnoreCase("Zipcode cannot be null."));
    }

    @Test
    void shouldNotAddInvalidLatitude() {
        Restaurant restaurant = makeFakeRestaurant();
        restaurant.setLatitude(-100);

        Result<Restaurant> actual = service.add(restaurant);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getMessages().get(0)
                .equalsIgnoreCase("Latitude must be between -90 and 90 degrees."));
    }

    @Test
    void shouldNotAddInvalidLongitude() {
        Restaurant restaurant = makeFakeRestaurant();
        restaurant.setLongitude(190);

        Result<Restaurant> actual = service.add(restaurant);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getMessages().get(0)
                .equalsIgnoreCase("Longitude must be between -180 and 180 degrees."));
    }

    @Test
    void shouldNotAddNullState() {
        Restaurant restaurant = makeFakeRestaurant();
        restaurant.setState(null);

        Result<Restaurant> actual = service.add(restaurant);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getMessages().get(0).equalsIgnoreCase("State cannot be null."));
    }

    @Test
    void shouldNotAddNullCity() {
        Restaurant restaurant = makeFakeRestaurant();
        restaurant.setCity(null);

        Result<Restaurant> actual = service.add(restaurant);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getMessages().get(0).equalsIgnoreCase("City cannot be null."));
    }

    @Test
    void shouldUpdateExistingRestaurant() {
        Restaurant toUpdate = makeFirstRestaurant();

        toUpdate.setName("Updated");

        when(repository.update(toUpdate)).thenReturn(true);

        Result<Restaurant> actual = service.update(toUpdate);
        assertTrue(actual.isSuccess());
        assertEquals(0, actual.getMessages().size());
    }

    @Test
    void shouldNotUpdateNullRestaurant() {
        Result<Restaurant> actual = service.update(null);
        assertFalse(actual.isSuccess());
        assertEquals(1, actual.getMessages().size());
        assertEquals("restaurant cannot be null.", actual.getMessages().get(0));
    }

    @Test
    void shouldNotUpdateNonExistingRestaurant() {
        Restaurant restaurant = makeFakeRestaurant();

        Result<Restaurant> actual = service.update(restaurant);

        assertFalse(actual.isSuccess());
        assertEquals(1, actual.getMessages().size());
        assertEquals(ActionStatus.NOT_FOUND, actual.getStatus());
        assertEquals("restaurant id `0` not found.", actual.getMessages().get(0));
    }

    @Test
    void shouldDeleteById() {
        when(repository.deleteById(anyInt())).thenReturn(true);

        Result<Restaurant> actual = service.deleteById(1);
        assertTrue(actual.isSuccess());
    }

    @Test
    void shouldNotDeleteMissingMeal() {
        when(repository.deleteById(anyInt())).thenReturn(false);
        Result<Restaurant> actual = service.deleteById(99);

        assertFalse(actual.isSuccess());
        assertEquals(ActionStatus.NOT_FOUND, actual.getStatus());
        assertEquals(1, actual.getMessages().size());
    }

    private Restaurant makeFakeRestaurant() {
        return new Restaurant(0,
                "TestGoogleId",
                "Bojangles",
                "330 S Churton St",
                "27278-2509",
                "https://locations.bojangles.com/nc/hillsborough/330-s--churton-st.html",
                "fake@fmail.com",
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
                "http://www.vivibubbletea.com/",
                null,
                "+1 646-858-0710",
                40.76726,
                -73.95927,
                "NY",
                "New York City",
                "Sun - Sat (11:00 AM - 9:00 PM)",
                "Healthy");
    }
}