package nosh.nosh_map_server.data;

import nosh.nosh_map_server.models.AppUserRestaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AppUserRestaurantJdbcTemplateRepositoryTest {

    @Autowired
    AppUserRestaurantJdbcTemplateRepository appUserRestaurantRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        jdbcTemplate.update("call set_known_good_state();");
    }

    @Test
    void shouldAddToBridgeTable() {
        AppUserRestaurant aur = new AppUserRestaurant(1, 1, "TestIdentifier");
        AppUserRestaurant expected = new AppUserRestaurant(1, 1, "TestIdentifier");

        AppUserRestaurant actual = appUserRestaurantRepository.addToBridge(aur);

        assertNotNull(actual);
        assertEquals(actual, expected);
    }

    @Test
    void shouldFindByUserId() {
        List<AppUserRestaurant> aurs = appUserRestaurantRepository.findByUserId(1);
        assertNotNull(aurs);
        assertTrue(aurs.size() > 0);
    }
}