package com.example.Shipping_Service.dto;

import com.example.Shipping_Service.entity.Status;
import lombok.Data;

@Data
public class ShippingDto {
    private Long id;
    private Status status;
    private Long orderId;
}
