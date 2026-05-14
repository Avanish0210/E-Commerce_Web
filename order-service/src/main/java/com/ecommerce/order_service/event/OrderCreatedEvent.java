package com.ecommerce.order_service.event;

import com.ecommerce.order_service.dto.OrderRequestItemDto;
import lombok.Data;

import java.util.List;

@Data
public class OrderCreatedEvent {
    Long orderId;
    Double totalPrice;
    List<OrderRequestItemDto> items;
}
