package org.example.foodorderingsystem.strategy;



import org.example.foodorderingsystem.dtos.OrderItemDTO;
import org.example.foodorderingsystem.model.RestaurantMenuItem;

import java.util.List;

public interface RestaurantSelectionStrategy {
    List<RestaurantMenuItem> selectRestaurants(List<OrderItemDTO> orderItems);
}

