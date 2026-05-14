package com.ecommerce.inventory_service.event;

import lombok.Data;

@Data
public class OrderCancelEvent {
    private Long orderId;
    private Long productId;
    private Integer quantity;
}
