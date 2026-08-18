package com.sparta.team4.commerce_payment_system.domain.order.dto;

import com.sparta.team4.commerce_payment_system.domain.order.entity.Order;
import lombok.Getter;

@Getter
public class OrderCancelResponse {
    private Long id;
    private String orderNumber;
    private String orderStatus;
    private String payStatus;

    public OrderCancelResponse(Order order, String payStatus) {
        this.id = order.getId();
        this.orderNumber = order.getOrderNumber();
        this.orderStatus = order.getStatus().name();
        this.payStatus = payStatus;
    }
}