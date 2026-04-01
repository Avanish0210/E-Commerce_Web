package com.ecommerce.inventory_service.dto;

import lombok.Data;

import java.nio.file.LinkOption;

@Data
public class ProductDto {

    private Long id;
    private String name;
    private Double price;
    private Integer stock;
}
