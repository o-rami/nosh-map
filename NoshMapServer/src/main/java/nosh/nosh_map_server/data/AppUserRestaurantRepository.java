package nosh.nosh_map_server.data;

import nosh.nosh_map_server.models.AppUserRestaurant;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AppUserRestaurantRepository {
    @Transactional
    AppUserRestaurant addToBridge(AppUserRestaurant aur);

    @Transactional
    List<AppUserRestaurant> findByUserId(int appUserId);
}
