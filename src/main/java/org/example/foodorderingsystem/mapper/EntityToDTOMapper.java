package org.example.foodorderingsystem.mapper;

import org.example.foodorderingsystem.model.*;
import org.example.foodorderingsystem.dtos.*;

import java.util.stream.Collectors;

public class EntityToDTOMapper {

    public static UserDTO toUserDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getClass().getSimpleName());
        return dto;
    }

    public static RestaurantDTO toRestaurantDTO(Restaurant restaurant) {
        RestaurantDTO dto = new RestaurantDTO();
        dto.setId(restaurant.getId());
        dto.setName(restaurant.getName());
        dto.setMaxCapacity(restaurant.getMaxCapacity());
        dto.setCurrentCapacity(restaurant.getCurrentCapacity());
        dto.setRating(restaurant.getRating());
        //dto.setMenu(restaurant.getMenu().stream().map(EntityToDTOMapper::toRestaurantMenuItemDTO).collect(Collectors.toList()));
        return dto;
    }

    public static MenuItemDTO toMenuItemDTO(MenuItem menuItem) {
        MenuItemDTO dto = new MenuItemDTO();
        dto.setId(menuItem.getId());
        dto.setName(menuItem.getName());
        dto.setTotalAvailableQuantity(menuItem.getTotalAvailableQuantity());
        return dto;
    }

    public static RestaurantMenuItemDTO toRestaurantMenuItemDTO(RestaurantMenuItem restaurantMenuItem) {
        RestaurantMenuItemDTO dto = new RestaurantMenuItemDTO();
        dto.setId(restaurantMenuItem.getId());
        dto.setRestaurantId(restaurantMenuItem.getRestaurant().getId());
        dto.setMenuItemId(restaurantMenuItem.getMenuItem().getId());
        dto.setAvailableQuantity(restaurantMenuItem.getAvailableQuantity());
        dto.setPrice(restaurantMenuItem.getPrice());
        return dto;
    }

    public static OrderDTO toOrderDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        //dto.setId(order.getId());
        dto.setCustomerId(order.getCustomerId());
        dto.setStatus(order.getStatus());
        dto.setItems(order.getItems().stream().map(EntityToDTOMapper::toOrderItemDTO).collect(Collectors.toList()));
        return dto;
    }

    public static OrderItemDTO toOrderItemDTO(OrderItem orderItem) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(orderItem.getId());
        dto.setRestaurantMenuItemId(orderItem.getRestaurantMenuItem().getId());
        dto.setQuantity(orderItem.getQuantity());
        return dto;
    }
}
