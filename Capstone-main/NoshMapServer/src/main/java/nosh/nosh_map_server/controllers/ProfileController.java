package nosh.nosh_map_server.controllers;

import nosh.nosh_map_server.domain.ProfileService;
import nosh.nosh_map_server.domain.Result;
import nosh.nosh_map_server.models.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService service;

    public ProfileController(ProfileService service) {
        this.service = service;
    }

    @GetMapping
    public List<Profile> findAll() {
        return service.findAll();
    }

    @GetMapping("/{appUserId}")
    public ResponseEntity<Profile> findByUserId(@PathVariable int appUserId) {
        Profile profile = service.findById(appUserId);
        if(profile == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(profile);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody Profile profile){
        Result<Profile> result = service.add(profile);
        System.out.println(result.getMessages());
        if(result.isSuccess()){
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @PutMapping("/{appUserId}")
    public ResponseEntity<Object> update(@PathVariable int appUserId, @RequestBody Profile profile){
        if(appUserId != profile.getAppUserId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        Result<Profile> result = service.update(profile);
        if(result.isSuccess()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{appUserId}")
    public ResponseEntity<Void> deleteById(@PathVariable int appUserId){
        if(service.deleteById(appUserId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}