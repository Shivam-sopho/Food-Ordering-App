package org.example.foodorderingsystem.mapper;

import org.example.foodorderingsystem.dtos.MenuItemDTO;
import org.example.foodorderingsystem.dtos.RestaurantDTO;
import org.example.foodorderingsystem.dtos.RestaurantMenuItemDTO;
import org.example.foodorderingsystem.model.MenuItem;
import org.example.foodorderingsystem.model.Restaurant;
import org.example.foodorderingsystem.model.RestaurantMenuItem;

import java.util.List;
import java.util.stream.Collectors;

public class DTOToEntityMapper {

    public static Restaurant toRestaurant(RestaurantDTO restaurantDTO) {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantDTO.getId());
        restaurant.setName(restaurantDTO.getName());
        restaurant.setMaxCapacity(restaurantDTO.getMaxCapacity());
        restaurant.setCurrentCapacity(restaurantDTO.getCurrentCapacity());
        restaurant.setRating(restaurantDTO.getRating());
        restaurant.setAddress(restaurantDTO.getAddress());
        return restaurant;
    }



    public static List<RestaurantMenuItem> toMenuItemList(List<RestaurantMenuItemDTO> menuItemDTOs, Restaurant restaurant) {
        return menuItemDTOs.stream()
                .map(dto -> toMenuItem(dto, restaurant))
                .collect(Collectors.toList());
    }

    public static RestaurantMenuItem toMenuItem(RestaurantMenuItemDTO menuItemDTO, Restaurant restaurant) {
        RestaurantMenuItem menuItem = new RestaurantMenuItem();
        menuItem.setId(menuItemDTO.getId());
       // menuItem.setName(menuItemDTO.getName());
        menuItem.setPrice(menuItemDTO.getPrice());
        menuItem.setAvailableQuantity(menuItemDTO.getAvailableQuantity());
        menuItem.setRestaurant(restaurant);
        return menuItem;
    }

    public static MenuItem getMenuItem(MenuItemDTO menuItemDTO){
        MenuItem menuItem = new MenuItem();
        menuItem.setId(menuItemDTO.getId());
        menuItem.setName(menuItemDTO.getName());
        menuItem.setTotalAvailableQuantity(menuItemDTO.getTotalAvailableQuantity());
        return menuItem;
    }
}


