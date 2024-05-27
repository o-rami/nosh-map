package nosh.nosh_map_server.data;

import nosh.nosh_map_server.models.AppUserMeal;
import nosh.nosh_map_server.models.AppUserRestaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AppUserMealJdbcTemplateRepositoryTest {

    @Autowired
    AppUserMealJdbcTemplateRepository appUserMealRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        jdbcTemplate.update("call set_known_good_state();");
    }

    @Test
    void shouldAddToBridgeTable() {
        AppUserMeal meal = new AppUserMeal(2, 4, "2-4");
        AppUserMeal expected = appUserMealRepository.addToBridge(meal);

        assertNotNull(expected);
    }

    @Test
    void shouldFindByUserId() {
        List<AppUserMeal> aums = appUserMealRepository.findByUserId(1);
        assertNotNull(aums);
        assertTrue(aums.size() > 0);
    }


}