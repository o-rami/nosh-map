package nosh.nosh_map_server.controllers;

import nosh.nosh_map_server.models.AppUser;
import nosh.nosh_map_server.models.Comment;
import nosh.nosh_map_server.security.AppUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequestMapping("/api/appUser")
public class AppUserController {

    private final AppUserService service;

    public AppUserController(AppUserService service) {
        this.service = service;
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDetails> findbyUsername(@PathVariable String username) {
        UserDetails user = service.loadUserByUsername(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }
}
