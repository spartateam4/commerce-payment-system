package com.sparta.team4.commerce_payment_system.domain.payment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sparta.team4.commerce_payment_system.domain.payment.entity.Payment;
import com.sparta.team4.commerce_payment_system.domain.payment.entity.PaymentStatus;

import java.time.LocalDateTime;

public record GetPaymentResponse(Integer amount, PaymentStatus status,
                                 @JsonProperty("paid_at") LocalDateTime paidAt) {
    public static GetPaymentResponse from(Payment payment) {
        return new GetPaymentResponse(payment.getAmount(), payment.getStatus(), payment.getPaidAt());
    }
}
