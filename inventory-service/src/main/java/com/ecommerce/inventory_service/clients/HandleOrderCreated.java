package com.ecommerce.inventory_service.clients;

import com.ecommerce.inventory_service.event.OrderCreatedEvent;
import com.ecommerce.inventory_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HandleOrderCreated {

    private final ProductService productService;

    @KafkaListener(topics = "${kafka.topic.order-created-topic}")
    public void handleOrderCreated(OrderCreatedEvent orderCreatedEvent){
        productService.reduceStocks(orderCreatedEvent);
    }
}
