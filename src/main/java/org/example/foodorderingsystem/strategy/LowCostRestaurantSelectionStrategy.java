package org.example.foodorderingsystem.strategy;

import org.example.foodorderingsystem.dtos.OrderItemDTO;
import org.example.foodorderingsystem.model.RestaurantMenuItem;
import org.example.foodorderingsystem.repository.RestaurantMenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component("LOWEST_COST")
public class LowCostRestaurantSelectionStrategy implements RestaurantSelectionStrategy {

    @Autowired
    private RestaurantMenuItemRepository restaurantMenuItemRepository;

    @Override
    public List<RestaurantMenuItem> selectRestaurants(List<OrderItemDTO> orderItems) {
        return orderItems.stream().map(orderItem -> {
            List<RestaurantMenuItem> menuItems = restaurantMenuItemRepository.findByMenuItemId(orderItem.getRestaurantMenuItemId());
            return menuItems.stream().min(Comparator.comparingDouble(RestaurantMenuItem::getPrice)).orElse(null);
        }).collect(Collectors.toList());
    }


}

