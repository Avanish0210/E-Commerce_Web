package com.ecommerce.inventory_service.event;

import lombok.Data;

@Data
public class OrderTotalPriceEvent {
    Long orderId;
    Double totalPrice;
}
