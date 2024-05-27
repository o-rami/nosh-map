package nosh.nosh_map_server.controllers;

import nosh.nosh_map_server.domain.AppUserRestaurantService;
import nosh.nosh_map_server.domain.Result;
import nosh.nosh_map_server.models.AppUserRestaurant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin
@RequestMapping("/api/restaurant/appUser")
public class AppUserRestaurantController {

    private final AppUserRestaurantService service;

    public AppUserRestaurantController(AppUserRestaurantService service) {
        this.service = service;
    }

    @GetMapping("/{appUserId}")
    public List<AppUserRestaurant> findByUserId(@PathVariable int appUserId) {
        var result = service.findByUserId(appUserId);
        return result;
    }

    @PostMapping
    public ResponseEntity<AppUserRestaurant> addToBridge(@RequestBody AppUserRestaurant aur) {
        Result<AppUserRestaurant> result = service.addToBridge(aur);
        return new ResponseEntity<>(result.getPayload(), getStatus(result, HttpStatus.CREATED));
    }

    private HttpStatus getStatus(Result<AppUserRestaurant> result, HttpStatus statusDefault) {
        switch (result.getStatus()) {
            case INVALID:
                return HttpStatus.BAD_REQUEST;
            case DUPLICATE:
                return HttpStatus.FORBIDDEN;
            case NOT_FOUND:
                return HttpStatus.NOT_FOUND;
        }
        return statusDefault;
    }


}
