package nosh.nosh_map_server.domain;

import nosh.nosh_map_server.data.AppUserRestaurantRepository;
import nosh.nosh_map_server.models.AppUserRestaurant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class AppUserRestaurantServiceTest {

    @MockBean
    AppUserRestaurantRepository repository;

    @Autowired
    AppUserRestaurantService service;

    @Test
    void shouldAddAppUserRestaurant() {
        AppUserRestaurant aur = new AppUserRestaurant(1, 1, "Test");

        AppUserRestaurant expected = new AppUserRestaurant(1, 1, "Test");

        when(repository.addToBridge(aur)).thenReturn(expected);

        Result<AppUserRestaurant> actual = service.addToBridge(aur);
        assertTrue(actual.isSuccess());
        assertEquals(expected, actual.getPayload());
    }

    @Test
    void shouldNotAddNullAppUserRestaurant() {
        Result<AppUserRestaurant> actual = service.addToBridge(null);
        assertFalse(actual.isSuccess());
        assertEquals("AppUserRestaurant cannot be null.", actual.getMessages().get(0));
    }

}