package com.ecommerce.order_service.clients;

import com.ecommerce.order_service.dto.OrderRequestItemDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "inventory-service" , path = "/inventory")
public interface InventoryOpenFeignClient {

    @PutMapping("/products/add-stocks")
    Double addStocks(@RequestBody OrderRequestItemDto orderRequestItemDto);
}
