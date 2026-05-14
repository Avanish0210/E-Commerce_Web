package com.ecommerce.inventory_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDto {
    private Long orderId;
    private Double totalPrice;
    private List<OrderRequestItemDto> items;
}

