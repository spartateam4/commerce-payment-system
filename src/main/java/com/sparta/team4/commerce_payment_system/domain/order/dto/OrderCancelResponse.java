package com.sparta.team4.commerce_payment_system.domain.order.dto;

import com.sparta.team4.commerce_payment_system.domain.order.entity.Order;
import com.sparta.team4.commerce_payment_system.domain.order.entity.OrderStatus;
import com.sparta.team4.commerce_payment_system.domain.payment.entity.PaymentStatus;
import lombok.Getter;

@Getter
public class OrderCancelResponse {
    private Long id;
    private String orderNumber;
    private OrderStatus orderStatus;
    private PaymentStatus payStatus;

    // 파라미터 타입도 String -> PaymentStatus로 변경
    public OrderCancelResponse(Order order, PaymentStatus payStatus) {
        this.id = order.getId();
        this.orderNumber = order.getOrderNumber();
        this.orderStatus = order.getStatus();
        this.payStatus = payStatus;
    }
}