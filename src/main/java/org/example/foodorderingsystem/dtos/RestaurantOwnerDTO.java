package org.example.foodorderingsystem.dtos;


import lombok.Data;
import java.util.List;

@Data
public class RestaurantOwnerDTO extends UserDTO {
    private List<RestaurantDTO> restaurants;
}

