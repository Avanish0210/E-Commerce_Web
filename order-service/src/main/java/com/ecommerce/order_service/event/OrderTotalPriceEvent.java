package com.ecommerce.order_service.event;

import lombok.Data;

@Data
public class OrderTotalPriceEvent {
    Long orderId;
    Double totalPrice;
}
