package com.ecommerce.order_service.clients;

import com.ecommerce.order_service.event.OrderStatusUpdatedEvent;
import com.ecommerce.order_service.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HandleOrderCreated {

    private final OrdersService ordersService;

    @KafkaListener(topics = "${kafka.topic.order-status-updated-topic}")
    public void handleOrderStatusUpdated(OrderStatusUpdatedEvent orderStatusUpdatedEvent) {
        ordersService.updateOrderStatus(orderStatusUpdatedEvent);
    }
}
