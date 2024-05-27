package nosh.nosh_map_server.data;

import nosh.nosh_map_server.models.AppUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AppUserJdbcTemplateRepositoryTest {

    @Autowired
    AppUserJdbcTemplateRepository appUserRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        jdbcTemplate.update("call set_known_good_state();");
    }

    @Test
    void shouldFindByUsername() {
        AppUser rabbit = appUserRepository.findByUsername("white@rabbit.com");
        assertNotNull(rabbit);
    }

    @Test
    void shouldCreateAppUser() {
        AppUser expected = new AppUser(0, "username", "password", true, new ArrayList<>(Arrays.asList("USER")));
        AppUser actual = appUserRepository.create(expected);
        assertNotNull(actual);
    }

    @Test
    void shouldUpdateAppUser() {
        AppUser expected = new AppUser(1, "usernamechanged", "password", true, new ArrayList<>(Arrays.asList("USER")));
        assertTrue(appUserRepository.update(expected));
    }
}