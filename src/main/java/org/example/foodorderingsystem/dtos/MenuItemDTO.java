package org.example.foodorderingsystem.dtos;

import lombok.Data;

@Data
public class MenuItemDTO {
    private Long id;
    private String name;
    private int totalAvailableQuantity;
}
