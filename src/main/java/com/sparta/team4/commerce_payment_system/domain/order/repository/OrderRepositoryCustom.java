package com.sparta.team4.commerce_payment_system.domain.order.repository; //QueryDSL 인터페이스

import com.sparta.team4.commerce_payment_system.domain.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {
    Page<Order> findMyOrders(Long memberId, Pageable pageable);
}