package nosh.nosh_map_server.controllers;

import nosh.nosh_map_server.domain.AppUserMealService;
import nosh.nosh_map_server.domain.Result;
import nosh.nosh_map_server.models.AppUserMeal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin
@RequestMapping("/api/meal/appUser")
public class AppUserMealController {
    private final AppUserMealService service;

    public AppUserMealController(AppUserMealService service) {
        this.service = service;
    }

    @GetMapping("/{appUserId}")
    public List<AppUserMeal> findByUserId(@PathVariable int appUserId) {
        return service.findByUserId(appUserId);
    }

    @PostMapping
    public ResponseEntity<AppUserMeal> addToBridge(@RequestBody AppUserMeal aum) {
        Result<AppUserMeal> result = service.addToBridge(aum);
        return new ResponseEntity<>(result.getPayload(), getStatus(result, HttpStatus.CREATED));
    }

    private HttpStatus getStatus(Result<AppUserMeal> result, HttpStatus statusDefault) {
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
