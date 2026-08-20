package com.sparta.team4.commerce_payment_system.domain.payment.dto;

import com.sparta.team4.commerce_payment_system.domain.order.entity.OrderItem;
import com.sparta.team4.commerce_payment_system.domain.order.entity.OrderStatus;
import com.sparta.team4.commerce_payment_system.domain.payment.entity.Payment;
import com.sparta.team4.commerce_payment_system.domain.payment.entity.PaymentStatus;

import java.time.LocalDateTime;

public record CreatePaymentResponse(Long paymentId, Long orderId, Integer stock, Integer amount,
                                    PaymentStatus paymentStatus, OrderStatus orderStatus, LocalDateTime paidAt) {
    public static CreatePaymentResponse from(Payment payment) {

        OrderItem orderItem = payment.getOrder().getOrderItems().get(0);

        return new CreatePaymentResponse(payment.getId(), payment.getOrder().getId(),
                orderItem.getProduct().getStockQuantity(), payment.getAmount(), payment.getStatus(),
                payment.getOrder().getStatus(), payment.getPaidAt());
    }
}
