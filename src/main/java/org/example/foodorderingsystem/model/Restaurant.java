package org.example.foodorderingsystem.model;


import lombok.Data;
import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int maxCapacity;
    private int currentCapacity;
    private float rating;
    private String address; // This should be an class with fields like street, city, state, country, pincode etc.


    @ManyToOne
    @JoinColumn(name = "restaurant_owner_id")
    private RestaurantOwner restaurantOwner;

}
