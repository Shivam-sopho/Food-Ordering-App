package org.example.foodorderingsystem.repository;

import org.example.foodorderingsystem.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
}

