package nosh.nosh_map_server.data;

import nosh.nosh_map_server.models.Restaurant;

import java.util.List;

public interface RestaurantRepository {

    List<Restaurant> findAll();

    Restaurant findByRestaurantId(int restaurantId);
    List<Restaurant> getFavRestaurants(int appUserId);
    List<Restaurant> findByUserId(int userId);

    Restaurant add(Restaurant profile);

    boolean update(Restaurant profile);

    boolean deleteById(int restaurantId);
}
