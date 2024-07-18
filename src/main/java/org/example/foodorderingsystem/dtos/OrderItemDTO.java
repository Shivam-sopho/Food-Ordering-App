package org.example.foodorderingsystem.dtos;

import lombok.Data;

@Data
public class OrderItemDTO {
    private Long id;
    private Long restaurantMenuItemId;
    private int quantity;
}

