package com.sparta.team4.commerce_payment_system.domain.order.repository;

import com.sparta.team4.commerce_payment_system.domain.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
