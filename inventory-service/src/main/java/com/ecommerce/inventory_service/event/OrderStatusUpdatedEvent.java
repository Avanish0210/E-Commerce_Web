package com.ecommerce.inventory_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusUpdatedEvent {
    private Long orderId;
    private String status;
    private String message;
}
