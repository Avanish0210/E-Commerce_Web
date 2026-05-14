package com.ecommerce.order_service.service;

import com.ecommerce.order_service.clients.ShippingOpenFeignClient;
import com.ecommerce.order_service.dto.OrderRequestDto;
import com.ecommerce.order_service.dto.ShippingRequestDto;
import com.ecommerce.order_service.entity.OrderItem;
import com.ecommerce.order_service.entity.OrderStatus;
import com.ecommerce.order_service.entity.Orders;
import com.ecommerce.order_service.event.OrderCancelEvent;
import com.ecommerce.order_service.event.OrderCreatedEvent;
import com.ecommerce.order_service.event.OrderStatusUpdatedEvent;
import com.ecommerce.order_service.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrdersService {

    private final OrdersRepository orderRepository;
    private final ModelMapper modelMapper;
    private final ShippingOpenFeignClient shippingOpenFeignClient;

    @Value("${kafka.topic.order-created-topic}")
    private String KAFKA_ORDER_CREATED_TOPIC;

    @Value("${kafka.topic.order-cancel-topic}")
    private String KAFKA_ORDER_CANCEL_TOPIC;

    private final KafkaTemplate<Long, OrderCreatedEvent> kafkaTemplate;
    private final KafkaTemplate<Long , OrderCancelEvent> kafkaCancelTemplate;

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

    public OrderRequestDto createOrder(OrderRequestDto orderRequestDto) {
        log.info("calling the createOrder method");

        Double totalPrice = orderRequestDto.getTotalPrice();

        Orders orders = modelMapper.map(orderRequestDto, Orders.class);
        for(OrderItem orderItem: orders.getItems()) {
            orderItem.setOrder(orders);
        }
        orders.setTotalPrice(totalPrice);
        orders.setOrderStatus(OrderStatus.PENDING);

        Orders savedOrder = orderRepository.save(orders);
        log.info("Order with ID: {}", savedOrder.getId());

        OrderCreatedEvent orderCreatedEvent = modelMapper.map(savedOrder, OrderCreatedEvent.class);
        orderCreatedEvent.setOrderId(savedOrder.getId());
        kafkaTemplate.send(KAFKA_ORDER_CREATED_TOPIC , savedOrder.getId() , orderCreatedEvent);

        return modelMapper.map(savedOrder, OrderRequestDto.class);
    }

    public void updateOrderStatus(OrderStatusUpdatedEvent orderStatusUpdatedEvent) {
        Orders order = orderRepository.findById(orderStatusUpdatedEvent.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        OrderStatus orderStatus = OrderStatus.valueOf(orderStatusUpdatedEvent.getStatus());
        order.setOrderStatus(orderStatus);
        orderRepository.save(order);

        if(orderStatus == OrderStatus.FULFILLED) {
            ShippingRequestDto shippingRequestDto = new ShippingRequestDto();
            shippingRequestDto.setOrderId(order.getId());
            shippingOpenFeignClient.createShipping(shippingRequestDto);
        }
    }

    public void cancelOrder(Long id) {
        Orders order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));

        if(order.getOrderStatus() == OrderStatus.FULFILLED || order.getOrderStatus() == OrderStatus.CONFIRMED) {
            for(OrderItem orderItem: order.getItems()) {
                OrderCancelEvent orderCancelEvent = modelMapper.map(orderItem, OrderCancelEvent.class);
                orderCancelEvent.setOrderId(order.getId());
                kafkaCancelTemplate.send(KAFKA_ORDER_CANCEL_TOPIC ,  order.getId() , orderCancelEvent);
            }
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }
}
