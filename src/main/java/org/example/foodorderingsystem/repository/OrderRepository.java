package org.example.foodorderingsystem.repository;

import org.example.foodorderingsystem.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}

