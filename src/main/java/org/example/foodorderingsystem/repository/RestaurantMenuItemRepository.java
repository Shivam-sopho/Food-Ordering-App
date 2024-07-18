package org.example.foodorderingsystem.repository;

import org.example.foodorderingsystem.model.RestaurantMenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RestaurantMenuItemRepository extends JpaRepository<RestaurantMenuItem, Long> {
    @Query("SELECT rmi FROM RestaurantMenuItem rmi WHERE rmi.menuItem.id = :menuItemId AND rmi.availableQuantity > 0 ORDER BY rmi.price ASC")
    List<RestaurantMenuItem> findAvailableItemsByMenuItemId(@Param("menuItemId") Long menuItemId);

    @Query("SELECT SUM(rmi.availableQuantity) FROM RestaurantMenuItem rmi WHERE rmi.menuItem.id = :menuItemId")
    Integer getTotalAvailableQuantity(@Param("menuItemId") Long menuItemId);

    List<RestaurantMenuItem> findByMenuItemId(Long menuItemId);

}

