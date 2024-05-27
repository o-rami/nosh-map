package nosh.nosh_map_server.data;

import nosh.nosh_map_server.models.AppUserMeal;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AppUserMealRepository {
    @Transactional
    AppUserMeal addToBridge(AppUserMeal aum);

    @Transactional
    List<AppUserMeal> findByUserId(int appUserId);
}
