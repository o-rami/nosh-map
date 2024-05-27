package nosh.nosh_map_server.domain;

import nosh.nosh_map_server.data.ProfileRepository;
import nosh.nosh_map_server.models.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileService {
    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public List<Profile> findAll() {
        return profileRepository.findAll();
    }

    public Profile findById(int appUserId) {
        return profileRepository.findByUserId(appUserId);
    }

    public Result<Profile> add(Profile profile){
        Result<Profile> result = validate(profile);
        if(!result.isSuccess()){
            System.out.println(result.getMessages());
            return result;
        }

        if(profile.getAppUserId() < 0) {
            result.addMessage(ActionStatus.INVALID, "appUserId cannot be negative for `add` operation");
            System.out.println(result.getMessages());
            return result;
        }

        boolean isAdded = profileRepository.add(profile);

        if(!isAdded) {
            result.addMessage(ActionStatus.INVALID, "Failed to add profile");
            System.out.println("in !isAdded " + result.getMessages());
            return result;
        }
        result.setPayload(profile);
        return result;

    }

    public Result<Profile> update(Profile profile){
        Result<Profile> result = validate(profile);
        if(!result.isSuccess()) {
            return result;
        }

        if(profile.getAppUserId() <= 0) {
            result.addMessage(ActionStatus.INVALID, "appUserId must be set for `update` operation");
            return result;
        }
        if(!profileRepository.update(profile)){
            String msg = String.format("profileId: %s, not found", profile.getAppUserId());
            result.addMessage(ActionStatus.NOT_FOUND, msg);
        }

        return result;
    }

    public boolean deleteById(int appUserId){
        return profileRepository.deleteById(appUserId);
    }

    private Result<Profile> validate(Profile profile){
        Result<Profile> result = new Result<>();
        if(profile == null) {
            result.addMessage(ActionStatus.INVALID, "profile cannot be null");
            return result;
        }

        if(Validations.isNullOrBlank(profile.getFirstName())){
            result.addMessage(ActionStatus.INVALID, "first Name is required");
        }
        if(Validations.isNullOrBlank(profile.getLastName())){
            result.addMessage(ActionStatus.INVALID, "last name is required");
        }
        if(Validations.isNullOrBlank(profile.getAddress())){
            result.addMessage(ActionStatus.INVALID, "address is required");
        }
        System.out.println(result.getMessages());
        return result;
    }

}