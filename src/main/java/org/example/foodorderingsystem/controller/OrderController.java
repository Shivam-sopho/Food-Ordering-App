package org.example.foodorderingsystem.controller;


import org.example.foodorderingsystem.dtos.OrderDTO;
import org.example.foodorderingsystem.dtos.OrderItemDTO;
import org.example.foodorderingsystem.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/place")
    public OrderDTO placeOrder(@RequestBody OrderDTO orderDTO) {
        return orderService.placeOrder(orderDTO);
    }

    @GetMapping("/{id}")
    public OrderDTO getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }


    @GetMapping
    public List<OrderDTO> getAllOrders() {
        return orderService.getAllOrders();
    }



    @PutMapping("/{orderId}/update")
    public OrderDTO updateOrder(@PathVariable Long orderId, @RequestBody List<OrderItemDTO> updatedItems) {
        return orderService.updateOrder(orderId, updatedItems);
    }

    @PutMapping("/item/{orderItemId}/dispatch")
    public void notifyItemDispatched(@PathVariable Long orderItemId) {
        orderService.notifyItemDispatched(orderItemId);
    }
}