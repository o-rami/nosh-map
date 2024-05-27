package nosh.nosh_map_server.domain;

import nosh.nosh_map_server.data.AppUserMealRepository;
import nosh.nosh_map_server.models.AppUserMeal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class AppUserMealServiceTest {

    @MockBean
    AppUserMealRepository repository;

    @Autowired
    AppUserMealService service;

    @Test
    void shouldAddAppUserMeal() {
        AppUserMeal aum = new AppUserMeal(2, 3, "2-3");

        AppUserMeal expected = new AppUserMeal(2, 3, "2-3");

        when(repository.addToBridge(aum)).thenReturn(expected);

        Result<AppUserMeal> actual = service.addToBridge(aum);
        assertTrue(actual.isSuccess());
        assertEquals(expected, actual.getPayload());
    }

    @Test
    void shouldNotAddNullAppUserMeal() {
        Result<AppUserMeal> actual = service.addToBridge(null);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getMessages().contains("AppUserMeal cannot be null."));
    }

}