package com.ecommerce.inventory_service.clients;

import com.ecommerce.inventory_service.event.OrderCancelEvent;
import com.ecommerce.inventory_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HandleOrderCancel {

    private final ProductService productService;
    @KafkaListener(topics = "${kafka.topic.order-cancel-topic}")
    public void handleOrderCancel(OrderCancelEvent orderCancelEvent){
        productService.addStocks(orderCancelEvent);

    }

}
