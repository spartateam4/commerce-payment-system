package com.sparta.team4.commerce_payment_system.domain.payment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sparta.team4.commerce_payment_system.domain.order.entity.OrderStatus;
import com.sparta.team4.commerce_payment_system.domain.payment.entity.Payment;
import com.sparta.team4.commerce_payment_system.domain.payment.entity.PaymentStatus;

public record CancelPaymentResponse(Long paymentId, Long orderId, Integer amount,
                                    @JsonProperty("pay_status") PaymentStatus paymentStatus,
                                    @JsonProperty("order_status") OrderStatus orderStatus) {
    public static CancelPaymentResponse from(Payment payment) {
        return new CancelPaymentResponse(payment.getId(), payment.getOrder().getId(),
                payment.getAmount(), payment.getStatus(), payment.getOrder().getStatus());
    }
}
