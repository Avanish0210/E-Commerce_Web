package com.ecommerce.order_service.clients;

import com.ecommerce.order_service.dto.ShippingRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "shipping-Service" )
public interface ShippingOpenFeignClient {

    @PutMapping("/details/create-shipping")
    void createShipping(@RequestBody ShippingRequestDto shippingRequestDto);

}
