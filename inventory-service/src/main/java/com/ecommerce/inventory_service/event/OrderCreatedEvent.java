package com.ecommerce.inventory_service.event;


import com.ecommerce.inventory_service.dto.OrderRequestItemDto;
import lombok.Data;

import java.util.List;

@Data
public class OrderCreatedEvent {
    Long orderId;
    Double totalPrice;
    List<OrderRequestItemDto> items;
}
