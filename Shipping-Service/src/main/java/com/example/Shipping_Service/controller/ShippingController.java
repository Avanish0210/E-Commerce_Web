package com.example.Shipping_Service.controller;

import com.example.Shipping_Service.dto.ShippingRequestDto;
import com.example.Shipping_Service.service.ShippingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/details")
@Slf4j
public class ShippingController {
    private final ModelMapper modelMapper;
    private final ShippingService shippingService;

    @PutMapping("/create-shipping")
    public void  createShipping(@RequestBody ShippingRequestDto shippingRequestDto) {
        log.info("Received in controller: {}", shippingRequestDto.getOrderId());
        shippingService.createShipping(shippingRequestDto);
    }
}
