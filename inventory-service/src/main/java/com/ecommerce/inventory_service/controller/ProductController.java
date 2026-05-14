package com.ecommerce.inventory_service.controller;

import com.ecommerce.inventory_service.clients.OrdersFeignClient;
import com.ecommerce.inventory_service.dto.OrderRequestItemDto;
import com.ecommerce.inventory_service.dto.ProductDto;
import com.ecommerce.inventory_service.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final ModelMapper modelMapper;

    private final OrdersFeignClient ordersFeignClient;

    @GetMapping("/fetchOrders")
    public String fetchFromOrderService(HttpServletRequest request) {
        log.info(request.getHeader("A-Custom_header"));
        return ordersFeignClient.helloOrders();

    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllInventory() {
        List<ProductDto> inventories = productService.getAllInventory();
        return ResponseEntity.ok(inventories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getInventoryById(@PathVariable Long id) {
        ProductDto inventory = productService.getProductById(id);
        return ResponseEntity.ok(inventory);
    }


//    @PutMapping("add-stocks")
//    public ResponseEntity<Double> addStocks(@RequestBody OrderRequestItemDto orderRequestItemDto) {
//        Double totalPrice = productService.addStocks(orderRequestItemDto);
//        return ResponseEntity.ok(totalPrice);
//    }

}
