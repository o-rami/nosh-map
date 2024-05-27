package nosh.nosh_map_server.domain;

import nosh.nosh_map_server.data.ProfileRepository;
import nosh.nosh_map_server.models.AppUser;
import nosh.nosh_map_server.models.Profile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProfileServiceTest {

    @MockBean
    ProfileRepository repository;

    @Autowired
    ProfileService service;


    @Test
    void shouldAddHappyPath() {
        Profile profile = makeProfile();
        when(repository.add(profile)).thenReturn(true);
        Result<Profile> actual = service.add(profile);
        assertTrue(actual.isSuccess());
        assertEquals(ActionStatus.SUCCESS, actual.getStatus());
        assertNotNull(actual.getPayload());
        assertEquals(profile, actual.getPayload());
    }

    @Test
    void shouldNotAddInvalidFirstName() {
        Profile profile = makeProfile();
        profile.setAppUserId(makeAppUser().getAppUserId());
        profile.setFirstName("  ");
        Result<Profile> actual = service.add(profile);
        assertEquals(ActionStatus.INVALID, actual.getStatus());
    }

    @Test
    void shouldNotAddInvalidLastName() {
        Profile profile = makeProfile();
        profile.setAppUserId(makeAppUser().getAppUserId());
        profile.setLastName(null);
        Result<Profile> actual = service.add(profile);
        assertEquals(ActionStatus.INVALID, actual.getStatus());
    }

    @Test
    void shouldNotAddNegativeUserId() {
        Profile profile = makeProfile();
        profile.setAppUserId(-1);
        Result<Profile> actual = service.add(profile);
        assertEquals(ActionStatus.INVALID, actual.getStatus());

    }

    @Test
    void shouldNotAddInvalidAddress() {
        Profile profile = makeProfile();
        profile.setAppUserId(makeAppUser().getAppUserId());
        profile.setAddress("  ");
        Result<Profile> actual = service.add(profile);
        assertEquals(ActionStatus.INVALID, actual.getStatus());
    }

    @Test
    void shouldNotAddNullProfile() {
        Result<Profile> profile = service.add(null);
        assertFalse(profile.isSuccess());
    }

    @Test
    void shouldUpdateHappyPath() {
        Profile profile = makeProfile();
        profile.setAppUserId(makeAppUser().getAppUserId());
        when(repository.update(profile)).thenReturn(true);
        Result<Profile> actual = service.update(profile);
        assertEquals(ActionStatus.SUCCESS, actual.getStatus());

    }

    @Test
    void shouldNotUpdateInvalidAppUserId() {
        Profile profile = makeProfile();
        profile.setAppUserId(makeAppUser().getAppUserId());
        makeAppUser().setAppUserId(99);
        Result<Profile> actual = service.update(profile);
        assertEquals(ActionStatus.NOT_FOUND, actual.getStatus());
    }

    @Test
    void deleteById() {
        assertFalse(service.deleteById(3));
        when(repository.deleteById(anyInt())).thenReturn(true);
        assertTrue(service.deleteById(3));
    }

    private AppUser makeAppUser() {
        return new AppUser(1, "testUsername", "testPass", true, new ArrayList<>(List.of("USER")));
    }

    private Profile makeProfile() {
        Profile profile = new Profile();
        profile.setFirstName("Test");
        profile.setLastName("LastName");
        profile.setAddress("address test");
        profile.setAppUserId(makeAppUser().getAppUserId());
        return profile;
    }
}