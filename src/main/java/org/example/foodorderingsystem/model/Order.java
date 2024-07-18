package org.example.foodorderingsystem.model;


import lombok.Data;
import org.example.foodorderingsystem.model.enums.OrderStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String customerId;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> items;

    private LocalDateTime orderCreatedAt;
    private LocalDateTime orderUpdatedAt;

    @PrePersist
    protected void onCreate() {
        orderCreatedAt = LocalDateTime.now();
        orderUpdatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        orderUpdatedAt = LocalDateTime.now();
    }
}

