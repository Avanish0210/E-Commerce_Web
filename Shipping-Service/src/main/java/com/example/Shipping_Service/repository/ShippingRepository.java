package com.example.Shipping_Service.repository;

import com.example.Shipping_Service.entity.ShippingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingRepository extends JpaRepository<ShippingEntity , Long> {
}
