package com.sparta.team4.commerce_payment_system.domain.order.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sparta.team4.commerce_payment_system.domain.order.entity.Order;
import com.sparta.team4.commerce_payment_system.domain.order.entity.OrderStatus;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@JsonPropertyOrder({"id", "orderNumber", "totalAmount", "status", "createdAt"})
public class OrderCreateResponse {
    private Long id;
    private String orderNumber;
    private Integer totalAmount;
    private OrderStatus status;
    private LocalDateTime createdAt;

    public OrderCreateResponse(Order order) {
        this.id = order.getId();
        this.orderNumber = order.getOrderNumber();
        this.totalAmount = order.getTotalAmount();
        this.status = order.getStatus();
        this.createdAt = order.getCreatedAt();
    }
}