package org.example.foodorderingsystem.repository;


import org.example.foodorderingsystem.model.RestaurantOwner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantOwnerRepository extends JpaRepository<RestaurantOwner, Long> {
}

