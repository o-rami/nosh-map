package nosh.nosh_map_server.data;

import nosh.nosh_map_server.models.AppUser;
import nosh.nosh_map_server.models.Meal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MealJdbcTemplateRepositoryTest {

    @Autowired
    MealJdbcTemplateRepository mealRepository;

    @Autowired
    AppUserJdbcTemplateRepository appUserRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        jdbcTemplate.update("call set_known_good_state();");
    }

    @Test
    void shouldFindMealByMealId() {
        Meal expected = new Meal(1, "Taro Creme Brulee", new BigDecimal("7.75"),
                "https://vivigaithersburg.square.site/uploads/1/3/1/4/131496331/" +
                        "s411215047120057019_p15_i2_w1795.png?width=1200&optimize=medium",
                "Sweet potato bubble milk tea with creme.",
                LocalDateTime.of(2023, Month.JUNE, 25, 14, 27, 0),
                true, 2, 1);

        Meal actual = mealRepository.findByMealId(1);

        assertEquals(expected, actual);
    }

    @Test
    void shouldFindAllMealsByUserId() {
        List<Meal> actual = mealRepository.findByUserId(2);

        assertTrue(actual.size() >= 1);
    }

    @Test
    void shouldFindAllPublicMeals() {
        AppUser user = appUserRepository.findByUsername("white@rabbit.com");

        List<Meal> actual = mealRepository.getPublic(user.getAppUserId());
        assertTrue(actual.size() >= 2);
    }

    @Test
    void shouldAddMeal() {
        Meal meal = new Meal(0, "Coffee Jelly Bubble Milk Tea", new BigDecimal("7.25"),
                "testUrl", "coffeecoffeecoffee--",
                LocalDateTime.of(2023, Month.JUNE, 25, 19, 30, 40),
                true, 2, 1);
        Meal expected = new Meal(10, "Coffee Jelly Bubble Milk Tea", new BigDecimal("7.25"),
                "testUrl","coffeecoffeecoffee--",
                LocalDateTime.of(2023, Month.JUNE, 25, 19, 30, 40),
                true, 2, 1);

        Meal actual = mealRepository.add(meal);

        assertNotNull(actual);
        assertEquals(actual, expected);
    }

    @Test
    void shouldUpdateMeal() {
        Meal meal = new Meal(2, "Updated", new BigDecimal("5.75"),
                "https://viviaurora.square.site/uploads/1/3/1/7/131791451/" +
                        "s687728388830480539_p7_i1_w2090.png?width=1200&optimize=medium",
                "Thai bubble milk tea",
                LocalDateTime.of(2023, Month.MAY, 25, 14, 27, 0),
                true, 2, 1);

        assertTrue(mealRepository.update(meal));


        Meal actual = mealRepository.findByMealId(2);

        assertEquals(meal, actual);
    }

    @Test
    void shouldNotUpdateMissingMeal() {
        Meal meal = new Meal(55, "Void", new BigDecimal("6.28"), "url", "Vivi's Secret Tea",
                LocalDateTime.of(2006, Month.MAY, 5, 13, 0, 0),
                true, 2, 1);
        assertFalse(mealRepository.update(meal));
    }

    @Test
    void shouldDeleteMealById() {
        assertTrue(mealRepository.deleteById(3));
    }

    @Test
    void shouldNotDeleteMissingMealById() {
        assertFalse(mealRepository.deleteById(55));
    }
}