package com.sparta.team4.commerce_payment_system.domain.payment.dto;

import com.sparta.team4.commerce_payment_system.domain.payment.entity.Payment;
import com.sparta.team4.commerce_payment_system.domain.payment.entity.PaymentStatus;

import java.time.LocalDateTime;

// TODO Order 도메인 구현 후 orderId, orderStatus 연결
public record CreatePaymentResponse(Long paymentId, Long orderId, Integer stock, Integer amount,
                                    PaymentStatus pay_status, PaymentStatus order_status, LocalDateTime paid_at) {
    public static CreatePaymentResponse from(Payment payment) {
        return new CreatePaymentResponse(payment.getId(), payment.getId(), payment.getAmount(),
                payment.getAmount(), payment.getStatus(), payment.getStatus(), payment.getPaidAt());
    }
}
