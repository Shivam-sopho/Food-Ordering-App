package org.example.foodorderingsystem.dtos;


import lombok.Data;
import org.example.foodorderingsystem.model.enums.OrderStatus;

import java.util.List;

@Data
public class OrderDTO {
    private String customerId;
    private OrderStatus status;
    private List<OrderItemDTO> items;
}

