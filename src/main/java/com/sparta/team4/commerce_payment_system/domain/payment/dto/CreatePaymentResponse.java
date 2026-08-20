package com.sparta.team4.commerce_payment_system.domain.payment.dto;

import com.sparta.team4.commerce_payment_system.domain.order.entity.OrderStatus;
import com.sparta.team4.commerce_payment_system.domain.payment.entity.Payment;
import com.sparta.team4.commerce_payment_system.domain.payment.entity.PaymentStatus;

import java.time.LocalDateTime;

// TODO Order 도메인에서 Product 연동 필요 (사유: stock)
public record CreatePaymentResponse(Long paymentId, Long orderId, Integer stock, Integer amount,
                                    PaymentStatus paymentStatus, OrderStatus orderStatus, LocalDateTime paidAt) {
    public static CreatePaymentResponse from(Payment payment) {
        return new CreatePaymentResponse(payment.getId(), payment.getOrder().getId(),
                payment.getAmount(), payment.getAmount(), payment.getStatus(),
                payment.getOrder().getStatus(), payment.getPaidAt());
    }
}
