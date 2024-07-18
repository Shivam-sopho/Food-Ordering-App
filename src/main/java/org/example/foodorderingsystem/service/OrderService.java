package org.example.foodorderingsystem.service;

import org.example.foodorderingsystem.config.StrategyConfiguration;
import org.example.foodorderingsystem.dtos.OrderDTO;
import org.example.foodorderingsystem.dtos.OrderItemDTO;
import org.example.foodorderingsystem.event.OrderEvent;
import org.example.foodorderingsystem.exceptions.BadRequestException;
import org.example.foodorderingsystem.exceptions.ResourceNotFoundException;
import org.example.foodorderingsystem.mapper.EntityToDTOMapper;
import org.example.foodorderingsystem.model.Order;
import org.example.foodorderingsystem.model.OrderItem;
import org.example.foodorderingsystem.model.RestaurantMenuItem;
import org.example.foodorderingsystem.model.enums.OrderStatus;
import org.example.foodorderingsystem.repository.OrderRepository;
import org.example.foodorderingsystem.repository.OrderItemRepository;
import org.example.foodorderingsystem.repository.RestaurantMenuItemRepository;
import org.example.foodorderingsystem.strategy.RestaurantSelectionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private RestaurantMenuItemRepository restaurantMenuItemRepository;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private StrategyConfiguration strategyConfiguration;



    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Transactional
    public synchronized OrderDTO placeOrder(OrderDTO orderDTO) {
        RestaurantSelectionStrategy restaurantSelectionStrategy = applicationContext.getBean(strategyConfiguration.getSelectionStrategy(), RestaurantSelectionStrategy.class);

        List<OrderItemDTO> items = orderDTO.getItems();

        // Check if all items in the order can be fulfilled by at least one restaurant
        for (OrderItemDTO item : items) {
            List<RestaurantMenuItem> restaurantMenuItems = restaurantMenuItemRepository.findByMenuItemId(item.getRestaurantMenuItemId());
            if (restaurantMenuItems.isEmpty() || restaurantMenuItems.stream().noneMatch(rmi -> rmi.getAvailableQuantity() >= item.getQuantity())) {
                throw new BadRequestException("One or more items in the order cannot be fulfilled by any restaurant");
            }
        }

        Order order = new Order();
        order.setCustomerId(orderDTO.getCustomerId());
        order.setStatus(OrderStatus.PROCESSED);

        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemDTO itemDTO : items) {
            int quantityNeeded = itemDTO.getQuantity();
            List<RestaurantMenuItem> restaurantMenuItems = restaurantSelectionStrategy.selectRestaurants(List.of(itemDTO));

            for (RestaurantMenuItem restaurantMenuItem : restaurantMenuItems) {
                if (quantityNeeded <= 0) break;

                int quantityToTake = Math.min(restaurantMenuItem.getAvailableQuantity(), quantityNeeded);
                if (restaurantMenuItem.getRestaurant().getCurrentCapacity() >= restaurantMenuItem.getRestaurant().getMaxCapacity()) {
                    continue; // Skip restaurants that have reached maximum capacity
                }

                restaurantMenuItem.setAvailableQuantity(restaurantMenuItem.getAvailableQuantity() - quantityToTake);
                restaurantMenuItem.getRestaurant().setCurrentCapacity(restaurantMenuItem.getRestaurant().getCurrentCapacity() + 1);
                restaurantMenuItemRepository.save(restaurantMenuItem);

                OrderItem orderItem = new OrderItem();
                orderItem.setRestaurantMenuItem(restaurantMenuItem);
                orderItem.setQuantity(quantityToTake);
                orderItem.setOrder(order);
                orderItem.setStatus(OrderItem.OrderItemStatus.PROCESSING);
                orderItems.add(orderItem);

                quantityNeeded -= quantityToTake;
            }

            if (quantityNeeded > 0) {
                throw new BadRequestException("Unable to fulfill the requested quantity for item ID " + itemDTO.getRestaurantMenuItemId());
            }
        }

        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);

        // Submit call to the restaurant service to process each order item
        for (OrderItem orderItem : savedOrder.getItems()) {
            eventPublisher.publishEvent(new OrderEvent(this, orderItem.getId()));
        }

        return EntityToDTOMapper.toOrderDTO(savedOrder);
    }

    public OrderDTO getOrderById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.map(EntityToDTOMapper::toOrderDTO).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(EntityToDTOMapper::toOrderDTO)
                .collect(Collectors.toList());
    }
// Future Scope
//    public OrderDTO updateOrderStatus(Long orderId, OrderStatus status) {
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
//        order.setStatus(status);
//        Order updatedOrder = orderRepository.save(order);
//        return EntityToDTOMapper.toOrderDTO(updatedOrder);
//    }

    @Transactional
    public synchronized OrderDTO updateOrder(Long orderId, List<OrderItemDTO> updatedItems) {
        RestaurantSelectionStrategy restaurantSelectionStrategy = applicationContext.getBean(strategyConfiguration.getSelectionStrategy(), RestaurantSelectionStrategy.class);


        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (Duration.between(order.getOrderCreatedAt(), LocalDateTime.now()).toMinutes() > 1) {
            throw new BadRequestException("Order can only be updated within 1 minute of creation");
        }

        List<OrderItem> existingOrderItems = order.getItems();
        existingOrderItems.forEach(orderItem -> {
            RestaurantMenuItem restaurantMenuItem = orderItem.getRestaurantMenuItem();
            restaurantMenuItem.setAvailableQuantity(restaurantMenuItem.getAvailableQuantity() + orderItem.getQuantity());
            restaurantMenuItem.getRestaurant().setCurrentCapacity(restaurantMenuItem.getRestaurant().getCurrentCapacity() - 1);
            restaurantMenuItemRepository.save(restaurantMenuItem);
        });

        orderItemRepository.deleteAll(existingOrderItems);
        order.setItems(new ArrayList<>());

        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemDTO itemDTO : updatedItems) {
            int quantityNeeded = itemDTO.getQuantity();
            List<RestaurantMenuItem> restaurantMenuItems = restaurantSelectionStrategy.selectRestaurants(List.of(itemDTO));

            for (RestaurantMenuItem restaurantMenuItem : restaurantMenuItems) {
                if (quantityNeeded <= 0) break;

                int quantityToTake = Math.min(restaurantMenuItem.getAvailableQuantity(), quantityNeeded);
                if (restaurantMenuItem.getRestaurant().getCurrentCapacity() >= restaurantMenuItem.getRestaurant().getMaxCapacity()) {
                    continue; // Skip restaurants that have reached maximum capacity
                }

                restaurantMenuItem.setAvailableQuantity(restaurantMenuItem.getAvailableQuantity() - quantityToTake);
                restaurantMenuItem.getRestaurant().setCurrentCapacity(restaurantMenuItem.getRestaurant().getCurrentCapacity() + 1);
                restaurantMenuItemRepository.save(restaurantMenuItem);

                OrderItem orderItem = new OrderItem();
                orderItem.setRestaurantMenuItem(restaurantMenuItem);
                orderItem.setQuantity(quantityToTake);
                orderItem.setOrder(order);
                orderItem.setStatus(OrderItem.OrderItemStatus.PROCESSING);
                orderItems.add(orderItem);

                quantityNeeded -= quantityToTake;
            }

            if (quantityNeeded > 0) {
                throw new BadRequestException("Unable to fulfill the requested quantity for item ID " + itemDTO.getRestaurantMenuItemId());
            }
        }

        order.setItems(orderItems);
        Order updatedOrder = orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);

        // Submit call to the restaurant service to process each order item
        for (OrderItem orderItem : updatedOrder.getItems()) {
                eventPublisher.publishEvent(new OrderEvent(this, orderItem.getId()));
        }

        return EntityToDTOMapper.toOrderDTO(updatedOrder);
    }

    @Transactional
    public void notifyItemDispatched(Long orderItemId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Order item not found"));

        if (orderItem.getStatus() == OrderItem.OrderItemStatus.DISPATCHED) {
            throw new BadRequestException("Order item already dispatched");
        }

        orderItem.setStatus(OrderItem.OrderItemStatus.DISPATCHED);
        orderItemRepository.save(orderItem);

        RestaurantMenuItem restaurantMenuItem = orderItem.getRestaurantMenuItem();
        restaurantMenuItem.getRestaurant().setCurrentCapacity(restaurantMenuItem.getRestaurant().getCurrentCapacity() - 1);
        restaurantMenuItemRepository.save(restaurantMenuItem);
    }
}
