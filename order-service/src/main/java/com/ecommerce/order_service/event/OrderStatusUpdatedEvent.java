package com.ecommerce.order_service.event;

import lombok.Data;

@Data
public class OrderStatusUpdatedEvent {
    private Long orderId;
    private String status;
    private String message;
}
