package org.example.foodorderingsystem.event;

import org.springframework.context.ApplicationEvent;

public class OrderEvent extends ApplicationEvent {
    private Long orderItemId;

    public OrderEvent(Object source, Long orderItemId) {
        super(source);
        this.orderItemId = orderItemId;
    }

    public Long getOrderItemId() {
        return orderItemId;
    }
}

