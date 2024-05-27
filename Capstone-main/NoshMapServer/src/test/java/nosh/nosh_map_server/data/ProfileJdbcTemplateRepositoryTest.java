package nosh.nosh_map_server.data;

import nosh.nosh_map_server.models.AppUser;
import nosh.nosh_map_server.models.Profile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProfileJdbcTemplateRepositoryTest {

    final static int NEXT_ID = 4;

    @Autowired
    ProfileJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @Autowired
    JdbcTemplate jdbcTemplate;

    // shouldFindProfileWithAppUserId() wasn't passing
//    @BeforeEach
//    void setUp() {
//        knownGoodState.set();
//    }

    @BeforeEach
    void setup() {
        jdbcTemplate.update("call set_known_good_state();");
    }

    @Test
    void shouldFindAllProfiles() {
        List<Profile> profiles = repository.findAll();
        assertNotNull(profiles);
        assertTrue(profiles.size() > 0);
    }

    @Test
    void shouldFindProfileWithAppUserId() {
        Profile profiles = repository.findByUserId(1);
        assertNotNull(profiles);
        assertEquals("Gloria", profiles.getFirstName());
    }

    @Test
    void shouldAddProfile() {
        Profile profile = makeProfile();
        profile.setAppUserId(makeAppUser().getAppUserId());
        assertTrue(repository.add(profile));
    }

    @Test
    void shouldUpdateProfileByAppUSerId() {
        Profile profile = makeProfile();
        profile.setAppUserId(makeAppUser().getAppUserId());
        assertTrue(repository.update(profile));
    }

    @Test
    void shouldNotUpdateProfileWithNonExistingAppUserId() {
        Profile profile = makeProfile();
        profile.setAppUserId(99);
        assertFalse(repository.update(profile));
    }

    @Test
    void shouldDeleteProfileByAppUserId() {
        assertTrue(repository.deleteById(2));
        assertFalse(repository.deleteById(2));
    }

    @Test
    void shouldNotDeleteProfileByNonExistingAppUserId() {
        assertFalse(repository.deleteById(NEXT_ID));
    }


    private AppUser makeAppUser() {
        AppUser appUser = new AppUser(1, "testUsername", "testPass", true, new ArrayList<>(List.of("USER")));
        return appUser;
    }
    private Profile makeProfile() {
        Profile profile = new Profile();
        profile.setFirstName("Test");
        profile.setLastName("LastName");
        profile.setAddress("address test");
        return profile;
    }

}