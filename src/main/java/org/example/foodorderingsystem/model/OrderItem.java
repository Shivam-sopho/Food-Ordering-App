package org.example.foodorderingsystem.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import javax.persistence.*;

@Entity
@Data
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "restaurant_menu_item_id")
    private RestaurantMenuItem restaurantMenuItem;

    private int quantity;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private Order order;

    @Enumerated(EnumType.STRING)
    private OrderItemStatus status;

    public enum OrderItemStatus {
        PROCESSING,
        DISPATCHED
    }
}

