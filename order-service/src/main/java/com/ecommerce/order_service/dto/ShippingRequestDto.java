package com.ecommerce.order_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ShippingRequestDto {

    private Long orderId;
}
