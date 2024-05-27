package nosh.nosh_map_server.data;

import nosh.nosh_map_server.models.Profile;

import java.util.List;

public interface ProfileRepository {
    List<Profile> findAll();

    Profile findByUserId(int appUserId);
    boolean add(Profile profile);

    boolean update(Profile profile);

    boolean deleteById(int profileId);
}
