package com.example.Shipping_Service.entity;

public enum Status {
    ORDER_PLACED,        // Order created but not yet processed
    PROCESSING,          // Preparing items for shipment
    PACKED,              // Items packed and ready to ship
    SHIPPED,             // Handed over to courier
    IN_TRANSIT,          // Moving through delivery network
    OUT_FOR_DELIVERY,    // With delivery agent
    DELIVERED
}
