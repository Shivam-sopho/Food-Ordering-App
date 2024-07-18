package org.example.foodorderingsystem.service;

import org.example.foodorderingsystem.dtos.OrderDTO;
import org.example.foodorderingsystem.dtos.OrderItemDTO;
import org.example.foodorderingsystem.dtos.RestaurantDTO;
import org.example.foodorderingsystem.dtos.RestaurantMenuItemDTO;
import org.example.foodorderingsystem.event.OrderEvent;
import org.example.foodorderingsystem.exceptions.BadRequestException;
import org.example.foodorderingsystem.exceptions.ResourceNotFoundException;
import org.example.foodorderingsystem.mapper.DTOToEntityMapper;
import org.example.foodorderingsystem.mapper.EntityToDTOMapper;
import org.example.foodorderingsystem.model.*;
import org.example.foodorderingsystem.repository.OrderItemRepository;
import org.example.foodorderingsystem.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderService orderService;

    @Autowired UserService userService;

    @Autowired MenuItemService menuItemService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

@Transactional
public RestaurantDTO registerRestaurant(String ownerUsername, RestaurantDTO restaurantDTO) {
    User owner = userService.findUserByUsername(ownerUsername)
            .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));

    if (!(owner instanceof RestaurantOwner)) {
        throw new BadRequestException("User is not authorized to register a restaurant");
    }

    Restaurant restaurant = new Restaurant();
    restaurant.setName(restaurantDTO.getName());
    restaurant.setAddress(restaurantDTO.getAddress());
    restaurant.setMaxCapacity(restaurantDTO.getMaxCapacity());
    restaurant.setCurrentCapacity(0);
    restaurant = restaurantRepository.save(restaurant);

    Restaurant finalRestaurant = restaurant;
    List<RestaurantMenuItem> restaurantMenuItems = restaurantDTO.getMenu().stream()
            .map(restaurantMenuItemDTO -> {
                MenuItem menuItem = menuItemService.getMenuItem(restaurantMenuItemDTO.getMenuItemId());
                RestaurantMenuItem restaurantMenuItem = new RestaurantMenuItem();
                restaurantMenuItem.setMenuItem(menuItem);
                restaurantMenuItem.setRestaurant(finalRestaurant);
                restaurantMenuItem.setPrice(restaurantMenuItemDTO.getPrice());
                restaurantMenuItem.setAvailableQuantity(restaurantMenuItem.getAvailableQuantity());
                return restaurantMenuItem;
            })
            .collect(Collectors.toList());

    restaurant.setMenu(restaurantMenuItems);
    restaurant = restaurantRepository.save(restaurant);

    return EntityToDTOMapper.toRestaurantDTO(restaurant);
}
    @Transactional
    public RestaurantDTO updateRestaurant(String ownerUsername, Long restaurantId, RestaurantDTO restaurantDTO) {
        User owner = userService.findUserByUsername(ownerUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));

        if (!(owner instanceof RestaurantOwner)) {
            throw new BadRequestException("User is not authorized to update the restaurant");
        }

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        if (!restaurant.getRestaurantOwner().getId().equals(owner.getId())) {
            throw new BadRequestException("Owner does not have access to update this restaurant");
        }

        restaurant.setName(restaurantDTO.getName());
        restaurant.setAddress(restaurantDTO.getAddress());
        restaurant.setMaxCapacity(restaurantDTO.getMaxCapacity());
        restaurant = restaurantRepository.save(restaurant);
        return EntityToDTOMapper.toRestaurantDTO(restaurant);
    }



    @Transactional
    public void processOrderItem(Long restaurantId, Long orderItemId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Order item not found"));

        if (!orderItem.getRestaurantMenuItem().getRestaurant().getId().equals(restaurantId)) {
            throw new BadRequestException("Order item does not belong to the specified restaurant");
        }

        // Simulate processing time
        // In a real system, this could be handled with a background job
        // For simplicity, we assume the item is processed immediately
        dispatchOrderItem(orderItemId);
    }

    @Transactional
    public void dispatchOrderItem(Long orderItemId) {
        eventPublisher.publishEvent(new OrderEvent(this, orderItemId));
    }

    public List<OrderDTO> getOrdersByRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        List<OrderItem> orderItems = orderItemRepository.findByRestaurantMenuItem_Restaurant_Id(restaurantId);
        List<Order> orders = orderItems.stream().map(OrderItem::getOrder).distinct().collect(Collectors.toList());

        return orders.stream().map(EntityToDTOMapper::toOrderDTO).collect(Collectors.toList());
    }

    public List<OrderItemDTO> getOrderItemsByRestaurant(Long restaurantId) {
        List<OrderItem> orderItems = orderItemRepository.findByRestaurantMenuItem_Restaurant_Id(restaurantId);
        return orderItems.stream().map(EntityToDTOMapper::toOrderItemDTO).collect(Collectors.toList());
    }
}
