package com.sparta.team4.commerce_payment_system.domain.order.dto;

import com.sparta.team4.commerce_payment_system.domain.order.entity.Order;
import com.sparta.team4.commerce_payment_system.domain.order.entity.OrderStatus;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class OrderCreateResponse {
    private Long id;
    private String orderNumber;
    private Integer totalPrice;
    private OrderStatus status;
    private LocalDateTime createdAt;

    public OrderCreateResponse(Order order) {
        this.id = order.getId();
        this.orderNumber = order.getOrderNumber();
        this.totalPrice = order.getTotalAmount();
        this.status = order.getStatus();
        this.createdAt = order.getCreatedAt();
    }
}