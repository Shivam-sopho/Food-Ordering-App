package org.example.foodorderingsystem.dtos;


import lombok.Data;

@Data
public class RestaurantMenuItemDTO {
    private Long id;
    private Long restaurantId;
    private Long menuItemId;
    private int availableQuantity;
    private float price;
}

