package org.example.foodorderingsystem.controller;

import org.example.foodorderingsystem.dtos.RestaurantDTO;
import org.example.foodorderingsystem.dtos.OrderDTO;
import org.example.foodorderingsystem.dtos.OrderItemDTO;
import org.example.foodorderingsystem.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping("/register/{username}")
    public RestaurantDTO registerRestaurant(@PathVariable String username,@RequestBody RestaurantDTO restaurantDTO) {
        return restaurantService.registerRestaurant(username,restaurantDTO);
    }

    @PutMapping("/{restaurantId}/{username}/update")
    public RestaurantDTO updateRestaurant(@PathVariable String username,@PathVariable Long restaurantId, @RequestBody RestaurantDTO restaurantDTO) {
        return restaurantService.updateRestaurant(username,restaurantId, restaurantDTO);
    }

    @PutMapping("/{restaurantId}/process/{orderItemId}")
    public void processOrderItem(@PathVariable Long restaurantId, @PathVariable Long orderItemId) {
        restaurantService.processOrderItem(restaurantId, orderItemId);
    }

    @PutMapping("/{restaurantId}/dispatch/{orderItemId}")
    public void dispatchOrderItem(@PathVariable Long restaurantId, @PathVariable Long orderItemId) {
        restaurantService.dispatchOrderItem(orderItemId);
    }

    @GetMapping("/{restaurantId}/orders")
    public List<OrderDTO> getOrdersByRestaurant(@PathVariable Long restaurantId) {
        return restaurantService.getOrdersByRestaurant(restaurantId);
    }

    @GetMapping("/{restaurantId}/order-items")
    public List<OrderItemDTO> getOrderItemsByRestaurant(@PathVariable Long restaurantId) {
        return restaurantService.getOrderItemsByRestaurant(restaurantId);
    }
}
