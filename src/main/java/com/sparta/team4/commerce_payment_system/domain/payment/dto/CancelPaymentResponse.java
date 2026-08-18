package com.sparta.team4.commerce_payment_system.domain.payment.dto;

import com.sparta.team4.commerce_payment_system.domain.order.entity.OrderStatus;
import com.sparta.team4.commerce_payment_system.domain.payment.entity.Payment;
import com.sparta.team4.commerce_payment_system.domain.payment.entity.PaymentStatus;

public record CancelPaymentResponse(Long paymentId, Long orderId, Integer amount,
                                    PaymentStatus pay_status, OrderStatus order_status) {
    public static CancelPaymentResponse from(Payment payment) {
        return new CancelPaymentResponse(payment.getId(), payment.getOrder().getId(),
                payment.getAmount(), payment.getStatus(), payment.getOrder().getStatus());
    }
}
