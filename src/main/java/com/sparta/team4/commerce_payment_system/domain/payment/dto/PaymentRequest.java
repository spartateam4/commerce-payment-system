package com.sparta.team4.commerce_payment_system.domain.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record PaymentRequest(
        @NotNull
        Long orderId,
        @NotBlank
        String result,
        @NotNull
        @PositiveOrZero
        Integer amount
) {
}
