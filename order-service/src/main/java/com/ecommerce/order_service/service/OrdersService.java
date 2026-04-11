package com.ecommerce.order_service.service;

import com.ecommerce.order_service.clients.InventoryOpenFeignClient;
import com.ecommerce.order_service.clients.ShippingOpenFeignClient;
import com.ecommerce.order_service.dto.OrderRequestDto;
import com.ecommerce.order_service.dto.OrderRequestItemDto;
import com.ecommerce.order_service.dto.ShippingRequestDto;
import com.ecommerce.order_service.entity.OrderItem;
import com.ecommerce.order_service.entity.OrderStatus;
import com.ecommerce.order_service.entity.Orders;
import com.ecommerce.order_service.repository.OrdersRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrdersService {

    private final OrdersRepository orderRepository;
    private final ModelMapper modelMapper;
    private final InventoryOpenFeignClient inventoryOpenFeignClient;
    private final ShippingOpenFeignClient shippingOpenFeignClient;

    public List<OrderRequestDto> getAllOrders() {
        log.info("Fetching all orders");
        List<Orders> orders = orderRepository.findAll();
        return orders.stream().map(order -> modelMapper.map(order, OrderRequestDto.class)).toList();
    }

    public OrderRequestDto getOrderById(Long id) {
        log.info("Fetching order with ID: {}", id);
        Orders order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        return modelMapper.map(order, OrderRequestDto.class);
    }

//    @Retry(name = "inventoryRetry" , fallbackMethod = "createOrderFallback")
    @CircuitBreaker(name = "inventoryCircuitBreaker" , fallbackMethod = "createOrderFallback")
//    @RateLimiter(name = "inventoryRateLimiter" , fallbackMethod = "createOrderFallback")
    public OrderRequestDto createOrder(OrderRequestDto orderRequestDto) {
        log.info("calling the createOrder method");
        Double totalPrice = inventoryOpenFeignClient.reduceStocks(orderRequestDto);

        Orders orders = modelMapper.map(orderRequestDto, Orders.class);
        for(OrderItem orderItem: orders.getItems()) {
            orderItem.setOrder(orders);
        }
        orders.setTotalPrice(totalPrice);
        orders.setOrderStatus(OrderStatus.CONFIRMED);

        Orders savedOrder = orderRepository.save(orders);
        log.info("Order with ID: {}", savedOrder.getId());

        ShippingRequestDto shippingRequestDto = new ShippingRequestDto();
        shippingRequestDto.setOrderId(savedOrder.getId());
        shippingOpenFeignClient.createShipping(shippingRequestDto);

        return modelMapper.map(savedOrder, OrderRequestDto.class);
    }

    public OrderRequestDto createOrderFallback(OrderRequestDto orderRequestDto , Throwable throwable) {
        log.error("Falling back occurred to : {}", throwable.getMessage());
        return new OrderRequestDto();
    }

    public void cancelOrder(Long id) {
        Orders order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));

        for(OrderItem orderItem: order.getItems()) {
            OrderRequestItemDto orderRequestDto = modelMapper.map(orderItem, OrderRequestItemDto.class);
            inventoryOpenFeignClient.addStocks(orderRequestDto);
        }
        orderRepository.delete(order);
    }
}
