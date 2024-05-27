package nosh.nosh_map_server.domain;

import nosh.nosh_map_server.data.MealRepository;
import nosh.nosh_map_server.models.Meal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
class MealServiceTest {

    @MockBean
    MealRepository repository;

    @Autowired
    MealService service;

    @Test
    void shouldFindByMealId() {
        Meal meal = new Meal(1, "Taro Creme Brulee", new BigDecimal("7.75"),
                "https://vivigaithersburg.square.site/uploads/1/3/1/4/131496331/" +
                        "s411215047120057019_p15_i2_w1795.png?width=1200&optimize=medium",
                "Sweet potato bubble milk tea with creme.",
                LocalDateTime.of(2023, Month.JUNE, 25, 14, 27, 0),
                true, 1, 2);

        when(repository.findByMealId(anyInt())).thenReturn(meal);

        Meal actual = service.findByMealId(1);

        assertEquals(meal, actual);
        assertEquals(1, actual.getMealId());
        assertEquals("Taro Creme Brulee", actual.getTitle());
        assertEquals(BigDecimal.valueOf(7.75), actual.getPrice());
        assertEquals("Sweet potato bubble milk tea with creme.", actual.getDescription());
        assertEquals(LocalDateTime.of(2023, Month.JUNE, 25, 14, 27, 0),
                actual.getLastUpdated());
    }

    @Test
    void shouldFindByUserId() {
        List<Meal> meals = List.of(
                new Meal(1, "Void", new BigDecimal("1.00"), "url", "Fake",
                        LocalDateTime.of(2011, Month.JANUARY, 1, 11, 11, 11),
                        true, 1, 1),
                new Meal(2, "MeMe", new BigDecimal("2.00"), "url", "Also Fake",
                        LocalDateTime.of(2006, Month.MAY, 2, 12, 22, 22),
                        true, 1, 2),
                new Meal(3, "MeMeMe", new BigDecimal("3.00"), "url", "Yep Also Fake",
                        LocalDateTime.of(2006, Month.MAY, 3, 13, 33, 33),
                        false, 1, 2));

        when(repository.findByUserId(anyInt())).thenReturn(meals);

        List<Meal> actual = service.findByUserId(1);
        assertEquals(3, actual.size());
        assertEquals("Void", actual.get(0).getTitle());
        assertEquals("Yep Also Fake", actual.get(2).getDescription());
    }

    @Test
    void shouldGetPublicMeals() {
        List<Meal> meals = List.of(
                new Meal(1, "Void", new BigDecimal("1.00"), "url", "Fake",
                        LocalDateTime.of(2011, Month.JANUARY, 1, 11, 11, 11),
                        true, 1, 1),
                new Meal(2, "MeMe", new BigDecimal("2.00"), "url", "Also Fake",
                        LocalDateTime.of(2006, Month.MAY, 2, 12, 22, 22),
                        true, 1, 2));

        when(repository.getPublic(1)).thenReturn(meals);

        List<Meal> actual = service.getPublic(1);
        assertEquals(2, actual.size());
        assertEquals("MeMe", actual.get(1).getTitle());
        assertEquals("2011-01-01T11:11:11", actual.get(0).getLastUpdated().toString());
    }

    @Test
    void shouldAddMeal() {
        Meal meal = new Meal(0, "Papadia", new BigDecimal("8.00"), "url", "Ranch on ranch on ranch",
                LocalDateTime.of(2023, Month.MARCH, 23, 23, 33, 33),
                true, 1, 4);
        Meal expected = new Meal(4, "Papadia", new BigDecimal("8.00"), "url", "Ranch on ranch on ranch",
                LocalDateTime.of(2023, Month.MARCH, 23, 23, 33, 33),
                true, 1, 4);

        when(repository.add(meal)).thenReturn(expected);
        Result<Meal> actual = service.add(meal);
        assertTrue(actual.isSuccess());
        assertEquals(expected, actual.getPayload());
    }

    @Test
    void shouldNotAddNullMeal() {
        Result<Meal> actual = service.add(null);
        assertFalse(actual.isSuccess());
        assertEquals("meal cannot be null.", actual.getMessages().get(0));
    }

    @Test
    void shouldNotAddExistingMeal() {
        Result<Meal> actual = service.add(new Meal(1, "Taro Creme Brulee", new BigDecimal("7.75"), "url",
                "Sweet potato bubble milk tea with creme.",
                LocalDateTime.of(2023, Month.JUNE, 25, 14, 27, 0),
                true, 1, 2));
        assertFalse(actual.isSuccess());
        assertEquals(1, actual.getMessages().size());
        assertEquals("insert failed", actual.getMessages().get(0));
    }

    @Test
    void shouldNotAddNullTitle() {
        Result<Meal> actual = service.add(new Meal(0, null, new BigDecimal("8.00"), "url",
                "Sweet potato bubble milk tea with creme.",
                LocalDateTime.of(2023, Month.MARCH, 23, 23, 33, 33),
                true, 1, 4));
        assertFalse(actual.isSuccess());
        assertEquals(2, actual.getMessages().size());
        assertTrue(actual.getMessages().contains("title cannot be null."));
    }

    @Test
    void shouldNotAddEmptyTitle() {
        Result<Meal> actual = service.add(new Meal(0, "", new BigDecimal("8.00"), "url",
                "Sweet potato bubble milk tea with creme.",
                LocalDateTime.of(2023, Month.MARCH, 23, 23, 33, 33),
                true, 1, 4));
        assertFalse(actual.isSuccess());
        assertEquals(1, actual.getMessages().size());
        assertTrue(actual.getMessages().contains("title cannot be blank."));
    }

    @Test
    void shouldNotAddNullPrice() {
        Result<Meal> actual = service.add(new Meal(0, "Taro Creme Brulee", null, "url",
                "Sweet potato bubble milk tea with creme.",
                LocalDateTime.of(2023, Month.MARCH, 23, 23, 33, 33),
                true, 1, 4));
        assertFalse(actual.isSuccess());
        assertEquals(1, actual.getMessages().size());
        assertTrue(actual.getMessages().contains("price cannot be null."));
    }

    @Test
    void shouldNotAddNegativePrice() {
        Result<Meal> actual = service.add(new Meal(0, "Taro Creme Brulee", new BigDecimal("-8.00"), "url",
                "Sweet potato bubble milk tea with creme.",
                LocalDateTime.of(2023, Month.MARCH, 23, 23, 33, 33),
                true, 1, 4));
        assertFalse(actual.isSuccess());
        assertEquals(1, actual.getMessages().size());
        assertTrue(actual.getMessages().contains("price cannot be less than zero."));
    }

    /*@Test
    void shouldNotAddNullMealDate() {
        Result<Meal> actual = service.add(new Meal(0, "Taro Creme Brulee", new BigDecimal("8.00"), "url",
                "Sweet potato bubble milk tea with creme.",
                null,
                true, 1, 4));
        assertFalse(actual.isSuccess());
        assertEquals(1, actual.getMessages().size());
        assertTrue(actual.getMessages().contains("date and time cannot be null."));
    }*/

    /*@Test
    void shouldNotAddFutureMeal() {
        Result<Meal> actual = service.add(new Meal(0, "Taro Creme Brulee", new BigDecimal("8.00"), "url",
                "Sweet potato bubble milk tea with creme.",
                LocalDateTime.now().plusWeeks(1),
                true, 1, 4));
        assertFalse(actual.isSuccess());
        assertEquals(1, actual.getMessages().size());
        assertTrue(actual.getMessages().contains("date cannot be in the future."));
    }*/

    @Test
    void shouldNotAddInvalidRestaurantId() {
        Result<Meal> actual = service.add(new Meal(0, "Taro Creme Brulee", new BigDecimal("8.00"), "url",
                "Sweet potato bubble milk tea with creme.",
                LocalDateTime.of(2023, Month.MARCH, 23, 23, 33, 33),
                true, 1, 0));
        assertFalse(actual.isSuccess());
        assertEquals(1, actual.getMessages().size());
        assertTrue(actual.getMessages().contains("restaurantId cannot be less than 1."));
    }

    /*@Test
    void shouldNotAddInvalidImageUrl() {
        Result<Meal> actual = service.add(new Meal(0, "Taro Creme Brulee", new BigDecimal("8.00"), null,
                "Sweet potato bubble milk tea with creme.",
                LocalDateTime.of(2023, Month.MARCH, 23, 23, 33, 33),
                true, 1, 1));
        assertFalse(actual.isSuccess());
        assertEquals(2, actual.getMessages().size());
        assertTrue(actual.getMessages().contains("image URL cannot be null."));
        assertTrue(actual.getMessages().contains("image URL cannot be blank."));
    }*/

    @Test
    void shouldNotAddInvalidUserId() {
        Result<Meal> actual = service.add(new Meal(0, "Taro Creme Brulee", new BigDecimal("8.00"), "url",
                "Sweet potato bubble milk tea with creme.",
                LocalDateTime.of(2023, Month.MARCH, 23, 23, 33, 33),
                true, 0, 4));
        assertFalse(actual.isSuccess());
        assertEquals(1, actual.getMessages().size());
        assertTrue(actual.getMessages().contains("appUserId cannot be less than 1."));
    }

    @Test
    void shouldUpdateExistingMeal() {
        Meal toUpdate = new Meal(1, "Taro Creme Brulee", new BigDecimal("7.75"), "url",
                "Sweet potato bubble milk tea with creme.",
                LocalDateTime.of(2023, Month.JUNE, 25, 14, 27, 0),
                true, 1, 2);

        toUpdate.setPrice(new BigDecimal("6.75"));

        when(repository.update(toUpdate)).thenReturn(true);

        Result<Meal> actual = service.update(toUpdate);
        assertTrue(actual.isSuccess());
        assertEquals(0, actual.getMessages().size());
    }

    @Test
    void shouldNotUpdateNullMeal() {
        Result<Meal> actual = service.update(null);
        assertFalse(actual.isSuccess());
        assertEquals(1, actual.getMessages().size());
        assertEquals("meal cannot be null.", actual.getMessages().get(0));
    }

    @Test
    void shouldNotUpdateNonExistingMeal() {
        Meal meal = new Meal(0, "Title", new BigDecimal("7.77"), "Description", "url",
                LocalDateTime.now().minusDays(1), true, 1, 1);

        Result<Meal> actual = service.update(meal);

        assertFalse(actual.isSuccess());
        assertEquals(1, actual.getMessages().size());
        assertEquals(ActionStatus.NOT_FOUND, actual.getStatus());
        assertEquals("meal id `0` not found.", actual.getMessages().get(0));
    }

    @Test
    void shouldDeleteById() {
        when(repository.deleteById(anyInt())).thenReturn(true);

        Result<Meal> actual = service.deleteById(1);
        assertTrue(actual.isSuccess());
    }

    @Test
    void shouldNotDeleteMissingMeal() {
        when(repository.deleteById(anyInt())).thenReturn(false);
        Result<Meal> actual = service.deleteById(99);

        assertFalse(actual.isSuccess());
        assertEquals(ActionStatus.NOT_FOUND, actual.getStatus());
        assertEquals(1, actual.getMessages().size());
    }

}