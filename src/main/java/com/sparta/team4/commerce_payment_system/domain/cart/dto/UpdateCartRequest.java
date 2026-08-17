package com.sparta.team4.commerce_payment_system.domain.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

public record UpdateCartRequest(
        @Min(value = 1, message = "수량은 1 이상이어야 합니다")
        int quantity
) {

}
