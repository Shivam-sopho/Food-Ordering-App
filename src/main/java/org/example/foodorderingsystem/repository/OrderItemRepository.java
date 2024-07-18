package org.example.foodorderingsystem.repository;


import org.example.foodorderingsystem.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByRestaurantMenuItem_Restaurant_Id(Long restaurantId);
}

