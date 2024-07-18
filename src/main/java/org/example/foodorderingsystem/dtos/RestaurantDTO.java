package org.example.foodorderingsystem.dtos;


import lombok.Data;
import java.util.List;

@Data
public class RestaurantDTO {
    private Long id;
    private String name;
    private int maxCapacity;
    private int currentCapacity;
    private float rating;
    private List<RestaurantMenuItemDTO> menu;
    private String address; // This should be an class with fields like street, city, state, country, pincode etc.
}
