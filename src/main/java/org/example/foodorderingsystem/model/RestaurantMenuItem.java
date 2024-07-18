package org.example.foodorderingsystem.model;


import lombok.Data;
import javax.persistence.*;

@Entity
@Data
public class RestaurantMenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "menu_item_id")
    private MenuItem menuItem;

    private int availableQuantity;
    private float price;
}

