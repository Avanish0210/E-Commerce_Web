package com.example.Shipping_Service.service;

import com.example.Shipping_Service.dto.ShippingRequestDto;
import com.example.Shipping_Service.entity.ShippingEntity;
import com.example.Shipping_Service.entity.Status;
import com.example.Shipping_Service.repository.ShippingRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShippingService {
    private final ShippingRepository shippingRepository;
    @Transactional
    public void createShipping(ShippingRequestDto shippingRequestDto) {

        ShippingEntity shippingEntity = new ShippingEntity();
        shippingEntity.setOrderId(shippingRequestDto.getOrderId());
        shippingEntity.setStatus(Status.ORDER_PLACED);

        shippingRepository.save(shippingEntity);

    }
}
