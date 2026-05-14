package com.ecommerce.order_service.dto;

import com.ecommerce.order_service.entity.OrderStatus;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDto {
    private Long id;
    private List<OrderRequestItemDto> items;
    private Double totalPrice;
    private OrderStatus orderStatus;
}
