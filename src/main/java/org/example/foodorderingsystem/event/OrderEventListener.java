package org.example.foodorderingsystem.event;



import org.example.foodorderingsystem.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    @Autowired
    private OrderService orderService;

    @EventListener
    public void handleOrderEvent(OrderEvent event) {
        orderService.notifyItemDispatched(event.getOrderItemId());
    }
}

